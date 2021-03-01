package com.rafaelwillen.database.dao.family;

import com.rafaelwillen.database.SQLiteConnection;
import com.rafaelwillen.database.dao.AccessObject;
import com.rafaelwillen.database.dao.finance.CostDAO;
import com.rafaelwillen.model.family.Family;
import com.rafaelwillen.model.family.Pet;
import com.rafaelwillen.model.family.Son;
import com.rafaelwillen.model.finance.GeneralCost;

import java.sql.*;

public class FamilyDAO implements AccessObject<Family> {
    private final static String TABLE_NAME = "familia";
    private final static String FIELD_ID = "id_familia";
    private final static String FIELD_FAMILY_NAME = "nome";
    private final static String FIELD_ADDRESS = "endereco";
    private final static String FIELD_HOME_PHONE_NUMBER = "telefone_casa";
    private static final FamilyDAO familyDAO = new FamilyDAO();
    private static Connection connection;
    private static String sqlStatement;

    private FamilyDAO() {
    }

    public static FamilyDAO getInstance() {
        return familyDAO;
    }

    @Override
    public void delete(Object id) {
        try {
            throw new IllegalAccessException("This must not be called");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void add(Family object) throws SQLException {
        if (alreadyHasFamily()) {
            throw new IllegalAccessError("There must be only one family");
        }
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("INSERT INTO %s (%s,%s,%s,%s) VALUES (?,?,?,?)", TABLE_NAME, FIELD_ID, FIELD_FAMILY_NAME, FIELD_ADDRESS, FIELD_HOME_PHONE_NUMBER);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, object.getId());
        preparedStatement.setString(2, object.getName());
        preparedStatement.setString(3, object.getAddressString());
        preparedStatement.setString(4, object.getHousePhoneNumber());
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
        if (object.getFather() != null) PersonDAO.getInstance().add(object.getFather());
        if (object.getMother() != null) PersonDAO.getInstance().add(object.getMother());
        if (object.getSons().size() > 0) {
            for (Son son : object.getSons()) {
                PersonDAO.getInstance().add(son);
            }
        }
        if (object.getPets().size() > 0) {
            for (Pet pet : object.getPets()) {
                PetDao.getInstance().add(pet);
            }
        }
        if (object.getCosts().size() > 0){
            for (GeneralCost cost : object.getCosts()){
                CostDAO.getInstance().add(cost);
            }
        }
    }

    @Override
    public void update(Object id, Family object) throws SQLException {
        if (!(id instanceof Integer)) {
            throw new IllegalArgumentException("The argument 'id' must be an integer");
        }
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("UPDATE %s SET %s=?,%s=?,%s=? WHERE %s=?", TABLE_NAME, FIELD_FAMILY_NAME, FIELD_ADDRESS, FIELD_HOME_PHONE_NUMBER, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, object.getName());
        preparedStatement.setString(2, object.getAddressString());
        preparedStatement.setString(3, object.getHousePhoneNumber());
        preparedStatement.setInt(4, (Integer) id);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    @Override
    public Family get(Object id) throws SQLException {
        if (!(id instanceof Integer)) {
            throw new IllegalArgumentException("The argument 'id' must be an integer");
        }
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_NAME, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, (int) id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Family family = buildFamily(resultSet);
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return family;
    }

    private Family buildFamily(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(FIELD_ID);
        String name = resultSet.getString(FIELD_FAMILY_NAME);
        String homePhoneNumber = resultSet.getString(FIELD_HOME_PHONE_NUMBER);
        String address = resultSet.getString(FIELD_ADDRESS);

        Family newFamily = new Family(id, name, homePhoneNumber, address);
        newFamily.setFather(PersonDAO.getInstance().getFather());
        newFamily.setMother(PersonDAO.getInstance().getMother());
        newFamily.getSons().addAll(PersonDAO.getInstance().getAllSons());
        newFamily.getPets().addAll(PetDao.getInstance().getAll());
        newFamily.getCosts().addAll(CostDAO.getInstance().getAllFromFamily());
        return newFamily;
    }

    @Override
    public boolean isTableEmpty() throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT COUNT() FROM %s", TABLE_NAME);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStatement);
        resultSet.next();
        int numberRows = resultSet.getInt(1);
        SQLiteConnection.closeConnection(connection, statement, resultSet);
        return numberRows == 0;
    }

    @Override
    public boolean exists(Object object) throws SQLException {
        int id;
        if (object instanceof Family) {
            id = ((Family) object).getId();
        } else if (!(object instanceof Integer)) {
            throw new IllegalArgumentException("The argument 'object' must be an integer or an instance of family");
        } else {
            id = (int) object;
        }

        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT COUNT() FROM %s WHERE %s=?", TABLE_NAME, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        boolean result = resultSet.getInt(1) != 0;
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return result;
    }

    @Override
    public int getLastID() {
        try {
            throw new IllegalAccessException("This method must not be called");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private boolean alreadyHasFamily() throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT COUNT() FROM %s", TABLE_NAME);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStatement);
        resultSet.next();
        int countResult = resultSet.getInt(1);
        SQLiteConnection.closeConnection(connection, statement, resultSet);
        return countResult >= 1;
    }
}
