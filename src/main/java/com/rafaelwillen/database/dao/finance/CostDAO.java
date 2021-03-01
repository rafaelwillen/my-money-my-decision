package com.rafaelwillen.database.dao.finance;

import com.rafaelwillen.database.SQLiteConnection;
import com.rafaelwillen.database.dao.AccessObject;
import com.rafaelwillen.model.family.Person;
import com.rafaelwillen.model.family.Pet;
import com.rafaelwillen.model.finance.Cost;
import com.rafaelwillen.model.finance.Credit;
import com.rafaelwillen.model.finance.GeneralCost;
import com.rafaelwillen.model.finance.IndividualCost;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;

public class CostDAO implements AccessObject<Cost> {

    private final static String TABLE_NAME = "gasto";
    private final static String FIELD_ID = "id_gasto";
    private final static String FIELD_NAME = "nome_gasto";
    private final static String FIELD_VALUE = "valor";
    private final static String FIELD_ADD_DATE = "data_add";
    private final static String FIELD_LOCAL = "local";
    private final static String FIELD_DESCRIPTION = "descricao";
    private final static String FIELD_COST_TYPE = "tipo_gasto";
    private final static String FK_USERNAME = "username";
    private final static String FK_FAMILY = "id_familia";
    private final static String FK_PET = "id_animal";
    private final static String FK_PREVISION = "id_previsao";
    private static final CostDAO costDao = new CostDAO();
    private static Connection connection;
    private static String sqlStatement;

    private CostDAO() {

    }

    public static CostDAO getInstance() {
        return costDao;
    }

    @Override
    public void delete(Object id) throws SQLException {
        if (!(id instanceof String)) {
            throw new IllegalArgumentException("The argument 'id' must be a int");
        }
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("DELETE FROM %s WHERE %s=?", TABLE_NAME, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, (String) id);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    @Override
    public void add(Cost object) throws SQLException {
        try {
            throw new IllegalAccessException("This method must not be called");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void add(IndividualCost object, Person person, int previsionID) throws SQLException {
        add(object, person.getUsername(), previsionID);
    }

    public void add(IndividualCost object, String username, int previsionID) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?,?,?,?)", TABLE_NAME, FIELD_ID, FIELD_NAME, FIELD_VALUE, FIELD_ADD_DATE, FIELD_LOCAL, FIELD_DESCRIPTION, FIELD_COST_TYPE, FK_USERNAME, FK_PREVISION);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, object.getId());
        preparedStatement.setString(2, object.getName());
        preparedStatement.setDouble(3, object.getValue());
        preparedStatement.setString(4, object.getAddedDate().toString());
        preparedStatement.setString(5, object.getLocal());
        preparedStatement.setString(6, object.getDescription());
        preparedStatement.setString(7, "INDIVIDUAL");
        preparedStatement.setString(8, username);
        preparedStatement.setInt(9, previsionID);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    public void add(IndividualCost object, Pet pet, int previsionID) throws SQLException {
        add(object, pet.getId(), previsionID);
    }

    public void add(IndividualCost object, int id, int previsionID) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?,?,?,?)", TABLE_NAME, FIELD_ID, FIELD_NAME, FIELD_VALUE, FIELD_ADD_DATE, FIELD_LOCAL, FIELD_DESCRIPTION, FIELD_COST_TYPE, FK_PET, FK_PREVISION);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, object.getId());
        preparedStatement.setString(2, object.getName());
        preparedStatement.setDouble(3, object.getValue());
        preparedStatement.setString(4, object.getAddedDate().toString());
        preparedStatement.setString(5, object.getLocal());
        preparedStatement.setString(6, object.getDescription());
        preparedStatement.setString(7, "INDIVIDUAL");
        preparedStatement.setInt(8, id);
        preparedStatement.setInt(9, previsionID);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    public void add(GeneralCost object, int previsionID) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?,?,?,?)", TABLE_NAME, FIELD_ID, FIELD_NAME, FIELD_VALUE, FIELD_ADD_DATE, FIELD_LOCAL, FIELD_DESCRIPTION, FIELD_COST_TYPE, FK_FAMILY, FK_PREVISION);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, object.getId());
        preparedStatement.setString(2, object.getName());
        preparedStatement.setDouble(3, object.getValue());
        preparedStatement.setString(4, object.getAddedDate().toString());
        preparedStatement.setString(5, object.getLocal());
        preparedStatement.setString(6, object.getDescription());
        preparedStatement.setString(7, "GERAL");
        preparedStatement.setInt(8, 1);
        preparedStatement.setInt(9, previsionID);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }


    @Override
    public void update(Object id, Cost object) throws SQLException {
        if (!(id instanceof Integer)) throw new IllegalArgumentException("The argument 'id' must be an integer");
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("UPDATE %s SET %s=?,%s=?,%s=?,%s=?,%s=? WHERE %s=?", TABLE_NAME, FIELD_NAME, FIELD_VALUE, FIELD_ADD_DATE, FIELD_LOCAL, FIELD_DESCRIPTION, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, object.getName());
        preparedStatement.setDouble(2, object.getValue());
        preparedStatement.setString(3, object.getAddedDate().toString());
        preparedStatement.setString(4, object.getLocal());
        preparedStatement.setString(5, object.getDescription());
        preparedStatement.setInt(6, (Integer) id);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    @Override
    public Cost get(Object id) throws SQLException {
        if (!(id instanceof Integer)) throw new IllegalArgumentException("The argument 'id' must be an integer");
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_NAME, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) return null;
        Cost cost = buildCost(resultSet);
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return cost;
    }

    public LinkedList<Cost> getAll() throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s", TABLE_NAME);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStatement);
        LinkedList<Cost> costs = new LinkedList<>();
        while (resultSet.next()) {
            costs.add(buildCost(resultSet));
        }
        SQLiteConnection.closeConnection(connection, statement, resultSet);
        return costs;
    }

    public LinkedList<Cost> getAllFromPrevision(int previsaoId) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_NAME, FK_PREVISION);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, previsaoId);
        ResultSet resultSet = preparedStatement.executeQuery(sqlStatement);
        LinkedList<Cost> costs = new LinkedList<>();
        while (resultSet.next()) {
            costs.add(buildCost(resultSet));
        }
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return costs;
    }

    public LinkedList<IndividualCost> getAllFromMember(String username) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_NAME, FK_USERNAME);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery(sqlStatement);
        LinkedList<IndividualCost> costs = new LinkedList<>();
        while (resultSet.next()) {
            costs.add((IndividualCost) buildCost(resultSet));
        }
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return costs;
    }

    public LinkedList<IndividualCost> getAllFromMember(int id) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_NAME, FK_PET);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery(sqlStatement);
        LinkedList<IndividualCost> costs = new LinkedList<>();
        while (resultSet.next()) {
            costs.add((IndividualCost) buildCost(resultSet));
        }
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return costs;
    }

    public LinkedList<GeneralCost> getAllFromFamily() throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_NAME, FK_FAMILY);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, 1);
        ResultSet resultSet = preparedStatement.executeQuery(sqlStatement);
        LinkedList<GeneralCost> costs = new LinkedList<>();
        while (resultSet.next()) {
            costs.add((GeneralCost) buildCost(resultSet));
        }
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return costs;
    }

    @Override
    public boolean isTableEmpty() throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT COUNT() FROM %s", TABLE_NAME);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStatement);
        resultSet.next();
        int countResult = resultSet.getInt(1);
        SQLiteConnection.closeConnection(connection, statement, resultSet);
        return countResult == 0;
    }

    @Override
    public boolean exists(Object object) throws SQLException {
        int id;
        if (object instanceof Credit) {
            id = ((Credit) object).getId();
        } else if (!(object instanceof Integer)) {
            throw new IllegalArgumentException("The argument 'id' must be an integer");
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
    public int getLastID() throws SQLException {
        if (isTableEmpty()) return 0;
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT %s FROM %s ORDER BY %s DESC LIMIT 1", FIELD_ID, TABLE_NAME, FIELD_ID);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStatement);
        resultSet.next();
        int lastId = resultSet.getInt(1);
        SQLiteConnection.closeConnection(connection, statement, resultSet);
        return lastId;
    }

    private Cost buildCost(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(FIELD_ID);
        String name = resultSet.getString(FIELD_NAME);
        double value = resultSet.getDouble(FIELD_VALUE);
        LocalDate addDate = LocalDate.parse(resultSet.getString(FIELD_ADD_DATE));
        String local = resultSet.getString(FIELD_LOCAL);
        Cost cost;
        switch (resultSet.getString(FIELD_COST_TYPE)) {
            case "GERAL":
                cost = new GeneralCost(id, name, value, addDate);
                break;
            case "INDIVIDUAL":
                cost = new IndividualCost(id, name, value, addDate);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + resultSet.getString(FIELD_COST_TYPE));
        }
        String description = resultSet.getString(FIELD_DESCRIPTION);
        if (description != null && !description.isEmpty()) {
            cost.setDescription(description);
        }
        cost.setLocal(local);
        return cost;
    }
}
