package com.rafaelwillen.database.dao.family;

import com.rafaelwillen.database.SQLiteConnection;
import com.rafaelwillen.database.dao.AccessObject;
import com.rafaelwillen.database.dao.finance.CostDAO;
import com.rafaelwillen.model.family.Pet;
import com.rafaelwillen.model.family.Sex;
import com.rafaelwillen.model.finance.IndividualCost;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;

public class PetDao implements AccessObject<Pet> {

    private final static String TABLE_NAME = "animal";
    private final static String FIELD_ID = "id_animal";
    private final static String FIELD_NAME = "nome";
    private final static String FIELD_ANIMAL_TYPE = "tipo_animal";
    private final static String FIELD_BIRTH_DATE = "data_nascimento";
    private final static String FIELD_SEX = "sexo";
    private final static PetDao petDao = new PetDao();
    private static Connection connection;
    private static String sqlStatement;

    private PetDao() {
    }

    public static PetDao getInstance() {
        return petDao;
    }

    public LinkedList<Pet> getAll() throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s", TABLE_NAME);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStatement);
        LinkedList<Pet> pets = new LinkedList<>();
        while (resultSet.next()) {
            pets.add(buildPet(resultSet));
        }
        SQLiteConnection.closeConnection(connection, statement, resultSet);
        return pets;
    }

    private Pet buildPet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(FIELD_ID);
        String name = resultSet.getString(FIELD_NAME);
        String animalType = resultSet.getString(FIELD_ANIMAL_TYPE);
        LocalDate birthDate = LocalDate.parse(resultSet.getString(FIELD_BIRTH_DATE));
        Sex sex = Sex.valueOf(resultSet.getString(FIELD_SEX));
        Pet pet = new Pet(id, animalType, name, birthDate, sex);
        pet.getCosts().addAll(CostDAO.getInstance().getAllFromMember(id));
        return pet;
    }

    @Override
    public void delete(Object id) throws SQLException {
        if (!(id instanceof Integer)) throw new IllegalArgumentException("The argument 'id' must be an integer");
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("DELETE FROM %s WHERE %s=?", TABLE_NAME, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, (Integer) id);
        int result = preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    @Override
    public void add(Pet object) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("INSERT INTO %s (%s,%s,%s,%s,%s) VALUES (?,?,?,?,?)", TABLE_NAME, FIELD_ID, FIELD_NAME, FIELD_ANIMAL_TYPE, FIELD_BIRTH_DATE, FIELD_SEX);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, object.getId());
        preparedStatement.setString(2, object.getName());
        preparedStatement.setString(3, object.getAnimalType());
        preparedStatement.setString(4, object.getBirthDate().toString());
        preparedStatement.setString(5, object.getSex().name());
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
        if (object.getCosts().size() > 0) {
            for (IndividualCost cost : object.getCosts()) {
                CostDAO.getInstance().add(cost);
            }
        }
    }

    @Override
    public void update(Object id, Pet object) throws SQLException {
        if (!(id instanceof Integer)) throw new IllegalArgumentException("The argument 'id' must be an integer");
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("UPDATE %s SET %s=?,%s=?,%s=?,%s=? WHERE %s=?", TABLE_NAME, FIELD_NAME, FIELD_ANIMAL_TYPE, FIELD_BIRTH_DATE, FIELD_SEX, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, object.getName());
        preparedStatement.setString(2, object.getAnimalType());
        preparedStatement.setString(3, object.getBirthDate().toString());
        preparedStatement.setString(4, object.getSex().name());
        preparedStatement.setInt(5, (Integer) id);
        int result = preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    @Override
    public Pet get(Object id) throws SQLException {
        if (!(id instanceof Integer)) throw new IllegalArgumentException("The argument 'id' must be an integer");
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_NAME, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) return null;
        Pet pet = buildPet(resultSet);
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return pet;
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
        if (object instanceof Pet) {
            id = ((Pet) object).getId();
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
}
