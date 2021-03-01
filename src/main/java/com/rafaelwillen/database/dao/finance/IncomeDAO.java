package com.rafaelwillen.database.dao.finance;

import com.rafaelwillen.database.SQLiteConnection;
import com.rafaelwillen.database.dao.AccessObject;
import com.rafaelwillen.model.family.Person;
import com.rafaelwillen.model.finance.Income;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;

public class IncomeDAO implements AccessObject<Income> {

    private final static String TABLE_NAME = "rendimento";
    private final static String FIELD_ID = "id_rendimento";
    private final static String FIELD_NAME = "nome";
    private final static String FIELD_VALUE = "valor";
    private final static String FIELD_ADDED_DATE = "data_add";
    private final static String FIELD_DESCRIPTION = "descricao";
    private final static String FIELD_LOCAL = "local";
    private final static String FK_USERNAME = "username_pessoa";
    private final static IncomeDAO incomeDao = new IncomeDAO();
    private static Connection connection;
    private static String sqlStatement;


    private IncomeDAO() {
    }

    public static IncomeDAO getInstance() {
        return incomeDao;
    }

    @Override
    public void delete(Object id) throws SQLException {
        if (!(id instanceof Integer)) throw new IllegalArgumentException("The argument 'id' must be a String");
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("DELETE FROM %s WHERE %s=?", TABLE_NAME, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, (Integer) id);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    @Override
    public void add(Income object) throws SQLException {
        throw new IllegalAccessError("This method must not be called");
    }

    public void add(Income object, String username) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?,?)", TABLE_NAME, FIELD_ID, FIELD_NAME, FIELD_VALUE, FIELD_ADDED_DATE, FIELD_DESCRIPTION, FIELD_LOCAL, FK_USERNAME);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, object.getId());
        preparedStatement.setString(2, object.getName());
        preparedStatement.setDouble(3, object.getValue());
        preparedStatement.setString(4, object.getAddedDate().toString());
        if (object.getDescription().isEmpty())
            preparedStatement.setNull(5, Types.NVARCHAR);
        else
            preparedStatement.setString(5, object.getDescription());
        preparedStatement.setString(6, object.getLocal());
        preparedStatement.setString(7, username);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    @Override
    public void update(Object id, Income object) throws SQLException {
        if (!(id instanceof Integer)) throw new IllegalArgumentException("The argument 'id' must be a String");
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("UPDATE %s SET %s=?,%s=?,%s=?,%s=?,%s=? WHERE %s=?", TABLE_NAME, FIELD_NAME, FIELD_VALUE, FIELD_ADDED_DATE, FIELD_DESCRIPTION, FIELD_LOCAL, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, object.getName());
        preparedStatement.setDouble(2, object.getValue());
        preparedStatement.setString(3, object.getAddedDate().toString());
        if (object.getDescription().isEmpty())
            preparedStatement.setNull(4, Types.NVARCHAR);
        else
            preparedStatement.setString(4, object.getDescription());
        preparedStatement.setString(5, object.getLocal());
        preparedStatement.setInt(6, (Integer) id);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    @Override
    public Income get(Object id) throws SQLException {
        if (!(id instanceof Integer)) throw new IllegalArgumentException("The argument 'id' must be a int");
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_NAME, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, (Integer) id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) return null;
        Income income = buildIncome(resultSet);
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return income;
    }

    public LinkedList<Income> getAll() throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s", TABLE_NAME);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStatement);
        LinkedList<Income> incomes = new LinkedList<>();
        while (resultSet.next()) {
            incomes.add(buildIncome(resultSet));
        }
        SQLiteConnection.closeConnection(connection, statement, resultSet);
        return incomes;
    }

    public LinkedList<Income> getAllFromPerson(Person person) throws SQLException {
        return getAllFromPerson(person.getUsername());
    }

    public LinkedList<Income> getAllFromPerson(String username) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_NAME, FK_USERNAME);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        LinkedList<Income> incomes = new LinkedList<>();
        while (resultSet.next()) {
            incomes.add(buildIncome(resultSet));
        }
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return incomes;
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
        if (object instanceof Income) {
            id = ((Income) object).getId();
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

    private Income buildIncome(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(FIELD_ID);
        String name = resultSet.getString(FIELD_NAME);
        double value = resultSet.getDouble(FIELD_VALUE);
        LocalDate addedDate = LocalDate.parse(resultSet.getString(FIELD_ADDED_DATE));
        String description = resultSet.getString(FIELD_DESCRIPTION);
        String local = resultSet.getString(FIELD_LOCAL);
        Income income = new Income(id, name, value, addedDate);
        income.setLocal(local);
        income.setDescription(description);
        return income;
    }
}
