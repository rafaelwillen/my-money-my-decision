package com.rafaelwillen.database.dao.finance;

import com.rafaelwillen.database.SQLiteConnection;
import com.rafaelwillen.database.dao.AccessObject;
import com.rafaelwillen.model.finance.Credit;
import com.rafaelwillen.model.finance.Income;

import java.sql.*;
import java.time.LocalDate;


public class CreditDAO implements AccessObject<Credit> {

    private static final String TABLE_NAME = "credito";
    private static final String FIELD_ID = "id_credito";
    private static final String FIELD_GRANTED_VALUE = "valor_concedido";
    private static final String FIELD_FEE = "juros";
    private static final String FIELD_REQUEST_DATE = "data_pedido";
    private static final String FIELD_DEADLINE = "prazo_pagamento";
    private static final String FIELD_DESCRIPTION = "descricao";
    private static final String FIELD_PAID = "pago";
    private static final CreditDAO creditDao = new CreditDAO();
    private static Connection connection;
    private static String sqlStatement;

    private CreditDAO(){}

    public static CreditDAO getInstance(){
        return creditDao;
    }

    @Override
    public void delete(Object id) throws SQLException {
        if (!(id instanceof String)) {
            throw new IllegalArgumentException("The argument 'id' must be a String");
        }
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("DELETE FROM %s WHERE %s=?", TABLE_NAME, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, (String) id);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    @Override
    public void add(Credit object) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?,0)", TABLE_NAME, FIELD_ID, FIELD_GRANTED_VALUE, FIELD_FEE, FIELD_REQUEST_DATE, FIELD_DEADLINE, FIELD_DESCRIPTION, FIELD_PAID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, object.getId());
        preparedStatement.setDouble(2, object.getGrantedValue());
        preparedStatement.setInt(3, object.getFees());
        preparedStatement.setString(4, object.getResquestDate().toString());
        preparedStatement.setString(5, object.getDeadline().toString());
        if (object.getDescription().isEmpty())
            preparedStatement.setNull(6, Types.NVARCHAR);
        else
            preparedStatement.setString(6, object.getDescription());
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    @Override
    public void update(Object id, Credit object) throws SQLException {
        if (!(id instanceof Integer)) {
            throw new IllegalArgumentException("The argument 'id' must be a int");
        }
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("UPDATE %s SET %s=?,%s=?,%s=?,%s=?,%s=? WHERE %s=?", TABLE_NAME, FIELD_GRANTED_VALUE, FIELD_FEE, FIELD_REQUEST_DATE, FIELD_DEADLINE, FIELD_DESCRIPTION, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setDouble(1, object.getGrantedValue());
        preparedStatement.setInt(2, object.getFees());
        preparedStatement.setString(3, object.getResquestDate().toString());
        preparedStatement.setString(4, object.getDeadline().toString());
        if (object.getDescription().isEmpty())
            preparedStatement.setNull(5, Types.NVARCHAR);
        else
            preparedStatement.setString(5, object.getDescription());
        preparedStatement.setInt(6, (Integer) id);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    @Override
    public Credit get(Object id) throws SQLException {
        if (!(id instanceof Integer)) {
            throw new IllegalArgumentException("The argument 'id' must be a int");
        }
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_NAME, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, (Integer) id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Credit credit = buildCredit(resultSet);
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return credit;
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
        if (object instanceof Credit){
            id = ((Credit) object).getId();
        } else if (!(object instanceof Integer)){
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

    public boolean isPaid(Income income) throws SQLException{
        return isPaid(income.getId());
    }

    public boolean isPaid(int id) throws SQLException{
        if (!exists(id)) throw new SQLDataException("There is no credit with the corresponding id = " + id);
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT %s FROM %s WHERE %s=?", FIELD_PAID, TABLE_NAME, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        boolean isPaid = resultSet.getInt(1) == 1;
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return isPaid;
    }

    public void setPaid(Income income) throws  SQLException{
        setPaid(income.getId());
    }

    public void setPaid(int id) throws SQLException{
        if (!exists(id)) throw new SQLDataException("There is no credit with the corresponding id = " + id);
        if (isPaid(id)) throw new SQLDataException("The credit is already paid");
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("UPDATE %s SET %s=1 WHERE %s=?", TABLE_NAME, FIELD_PAID, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    private Credit buildCredit(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(FIELD_ID);
        double grantedValue = resultSet.getDouble(FIELD_GRANTED_VALUE);
        int fee = resultSet.getInt(FIELD_FEE);
        LocalDate requestDate = LocalDate.parse(resultSet.getString(FIELD_REQUEST_DATE));
        LocalDate deadline = LocalDate.parse(resultSet.getString(FIELD_DEADLINE));
        String description = resultSet.getString(FIELD_DESCRIPTION);
        Credit credit;
        credit = new Credit(id, grantedValue, fee, requestDate, deadline);
        if (description != null && !description.isEmpty()){
           credit.setDescription(description);
        }
        return credit;
    }
}
