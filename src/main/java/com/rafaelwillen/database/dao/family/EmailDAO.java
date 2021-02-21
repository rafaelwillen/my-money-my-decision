package com.rafaelwillen.database.dao.family;

import com.rafaelwillen.database.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class EmailDAO {
    private final static String TABLE_NAME ="email";
    private final static String FIELD_EMAIL = "email";
    private final static String FK_USERNAME = "username";

    private static Connection connection;
    private static String sqlStatement;
    private static final EmailDAO emailDAOInstance = new EmailDAO();

    private EmailDAO() {
    }

    public static EmailDAO getInstance() {
        return emailDAOInstance;
    }

    public LinkedList<String> getAllFromPessoa(String username) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT %s FROM %s WHERE %s=?", FIELD_EMAIL, TABLE_NAME, FK_USERNAME);
        LinkedList<String> emails = new LinkedList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            emails.add(resultSet.getString(FIELD_EMAIL));
        }
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return emails;
    }

    public boolean exists(String email) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT COUNT() FROM %s WHERE %s=?", TABLE_NAME, FIELD_EMAIL);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        boolean result = resultSet.getInt(1) == 1;
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return result;
    }

    public void delete(String email) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("DELETE FROM %s WHERE %s=?", TABLE_NAME, FIELD_EMAIL);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, email);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    public void add(String email, String username) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("INSERT INTO %s (%s,%s) VALUES (?,?)", TABLE_NAME, FIELD_EMAIL, FK_USERNAME);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, username);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    public void addAll(String username, LinkedList<String> emails) throws SQLException {
        for (String email : emails){
            add(email, username);
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
