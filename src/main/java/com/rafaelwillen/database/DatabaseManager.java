package com.rafaelwillen.database;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton class that handles the initialization of the database.
 * By initialization, this class is creates all table if they don't already exist
 */
public class DatabaseManager {
    private DatabaseManager(){

    }

    /**
     * Checks if the application is running in the first time. By first time means that the initial setup was not completed or the database file does not exists.
     *
     * @return true if it is the first time.
     * @throws SQLException If there was an query error or the connection was not possible
     */
    public static boolean firstTimeUsage() throws SQLException {
        String sql = "SELECT setup_complete FROM config";
        Connection connection = SQLiteConnection.connect();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        boolean setupComplete = resultSet.getInt(1) == 1;
        SQLiteConnection.closeConnection(connection, statement, resultSet);
        return setupComplete;
    }

    /**
     * Creates the database table
     */
    public static void initDatabase() throws Exception {
        throw new Exception("Not implemented");
    }

    /**
     * Set the setupComplete to true in the database. This mean that the user finished the application setup.
     *
     * @throws SQLException If there was an query error or the connection was not possible
     */
    public static void setSetupComplete() throws SQLException {
        String sql = "UPDATE config SET setup_complete=1";
        Connection connection = SQLiteConnection.connect();
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        SQLiteConnection.closeConnection(connection, statement);
    }

    /**
     * Deletes the SQLite database file
     */
    public static void deleteDatabase() {
        File databaseFile = new File("database.db");
        if (databaseFile.exists() && databaseFile.delete()) {
            System.out.println("Database deleted");
        }
    }

}
