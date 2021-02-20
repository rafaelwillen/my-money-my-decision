package com.rafaelwillen.database;

import java.sql.*;

/**
 * Singleton class responsible for the SQLite database connection with the application.
 * Handles the connection and the closing methods with the database.
 */
public class SQLiteConnection {
    private final static String CONNECTION_STRING = "jdbc:sqlite:database.db";

    private SQLiteConnection(){

    }

    /**
     * Start a new connection with the database
     *
     * @return A Connection object used to communicate to the database
     * @throws SQLException if it was not possible to connect to the database
     */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(CONNECTION_STRING);
    }

    /**
     * Closes the connection with the database. Always call this method after finished communicating with the database
     *
     * @param connection The connection
     * @throws SQLException if it was not possible to connect to the database
     */
    public static void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    /**
     * Closes the connection and the preparedStatement with the database. Always call this method after finished communicating with the database
     *
     * @param connection        The connection
     * @param preparedStatement The preparedStatement used to pass SQL
     * @throws SQLException if it was not possible to connect to the database
     */
    public static void closeConnection(Connection connection, PreparedStatement preparedStatement) throws SQLException {
        closeConnection(connection);
        preparedStatement.close();
    }

    /**
     * Closes the connection and the statement with the database. Always call this method after finished communicating with the database
     *
     * @param connection The connection
     * @param statement  The statement used to pass SQL
     * @throws SQLException if it was not possible to connect to the database
     */
    public static void closeConnection(Connection connection, Statement statement) throws SQLException {
        closeConnection(connection);
        statement.close();
    }
    /**
     * Closes the connection, preparedStatement and the resultSet with the database. Always call this method after finished communicating with the database
     *
     * @param connection        The connection
     * @param preparedStatement The preparedStatement used to pass SQL
     * @param resultSet         The resultSet that stores the query result
     * @throws SQLException if it was not possible to connect to the database
     */
    public static void closeConnection(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) throws SQLException {
        closeConnection(connection, preparedStatement);
        resultSet.close();
    }

    /**
     * Closes the connection, statement and the resultSet with the database. Always call this method after finished communicating with the database
     *
     * @param connection The connection
     * @param statement  The statement used to pass SQL
     * @param resultSet  The resultSet that stores the query result
     * @throws SQLException if it was not possible to connect to the database
     */
    public static void closeConnection(Connection connection, Statement statement, ResultSet resultSet) throws SQLException {
        closeConnection(connection, statement);
        resultSet.close();
    }

    /**
     * Checks if it's possible communicate to the database. Same as opening and closing the database connection object
     *
     * @return true if the connection is successful
     */
    public static boolean testConnection() {
        try {
            Connection connection = connect();
            closeConnection(connection);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
}
