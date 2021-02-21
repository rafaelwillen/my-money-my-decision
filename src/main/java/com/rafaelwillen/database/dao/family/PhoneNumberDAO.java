package com.rafaelwillen.database.dao.family;

import com.rafaelwillen.database.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class PhoneNumberDAO {
    private final static String TABLE_NAME = "telefone";
    private final static String FIELD_PHONE_NUMBER = "telefone";
    private final static String FK_USERNAME = "username";

    private static Connection connection;
    private static String sqlStatement;
    private static final PhoneNumberDAO phoneNumberDAO = new PhoneNumberDAO();

    private PhoneNumberDAO() {
    }

    public static PhoneNumberDAO getInstance() {
        return phoneNumberDAO;
    }

    public LinkedList<String> getAllFromPessoa(String username) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT %s FROM %s WHERE %s=?", FIELD_PHONE_NUMBER, TABLE_NAME, FK_USERNAME);
        LinkedList<String> phoneNumbers = new LinkedList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            phoneNumbers.add(resultSet.getString(FIELD_PHONE_NUMBER));
        }
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return phoneNumbers;
    }

    public boolean exists(String phoneNumber) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT COUNT() FROM %s WHERE %s=?", TABLE_NAME, FIELD_PHONE_NUMBER);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, phoneNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        boolean result = resultSet.getInt(1) == 1;
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return result;
    }

    public void delete(String phoneNumber) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("DELETE FROM %s WHERE %s=?", TABLE_NAME, FIELD_PHONE_NUMBER);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, phoneNumber);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    public void add(String phoneNumber, String username) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("INSERT INTO %s (%s,%s) VALUES (?,?)", TABLE_NAME, FIELD_PHONE_NUMBER, FK_USERNAME);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, phoneNumber);
        preparedStatement.setString(2, username);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    public void addAll(String username, LinkedList<String> phoneNumbers) throws SQLException {
        for (String phoneNumber : phoneNumbers){
            add(phoneNumber, username);
        }
    }

    public void deleteAllFrom(String username) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("DELETE FROM %s WHERE %s=?", TABLE_NAME, FK_USERNAME);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, username);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }
}
