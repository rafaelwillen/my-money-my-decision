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
    private DatabaseManager() {

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
        String[] sqlStatments = new String[11];

        sqlStatments[0] = "PRAGMA foreign_keys = ON";
        sqlStatments[1] = "CREATE TABLE IF NOT EXISTS animal(" +
                "id_animal INTEGER NOT NULL UNIQUE," +
                "nome TEXT NOT NULL," +
                "tipo_animal TEXT NOT NULL," +
                "data_nascimento	TEXT NOT NULL," +
                "sexo TEXT NOT NULL," +
                "PRIMARY KEY(\"id_animal\")" +
                ")";

        sqlStatments[2] = "CREATE TABLE IF NOT EXISTS credito(" +
                "id_credito INTEGER NOT NULL," +
                "valor_concedido REAL NOT NULL," +
                "juros INTEGER NOT NULL," +
                "data_pedido TEXT NOT NULL," +
                "prazo_pagamento TEXT NOT NULL," +
                "descricao TEXT NOT NULL," +
                "PRIMARY KEY (\"id_credito\")" +
                ")";
        sqlStatments[3] = "CREATE TABLE IF NOT EXISTS pessoa(" +
                "username TEXT NOT NULL," +
                "nome TEXT NOT NULL," +
                "data_nascimento TEXT NOT NULL," +
                "password TEXT NOT NULL UNIQUE," +
                "sexo TEXT NOT NULL," +
                "tipo_pessoa TEXT NOT NULL," +
                "PRIMARY KEY (\"username\")" +
                ")";
        sqlStatments[4] = "CREATE TABLE IF NOT EXISTS email(" +
                "email TEXT NOT NULL," +
                "username TEXT NOT NULL," +
                "PRIMARY KEY (\"email\")," +
                "FOREIGN KEY (\"username\")" +
                "    REFERENCES pessoa (\"username\")" +
                "    ON DELETE CASCADE ON UPDATE CASCADE" +
                ")";
        sqlStatments[5] = "CREATE TABLE IF NOT EXISTS telefone(" +
                "telefone TEXT NOT NULL," +
                "username TEXT NOT NULL," +
                "PRIMARY KEY (\"telefone\")," +
                "FOREIGN KEY (\"username\")" +
                "    REFERENCES pessoa (\"username\")" +
                "    ON DELETE CASCADE ON UPDATE CASCADE" +
                ")";
        sqlStatments[6] = "CREATE TABLE IF NOT EXISTS familia(" +
                "nome TEXT NOT NULL," +
                "endereco TEXT NOT NULL," +
                "telefone_casa TEXT NOT NULL," +
                "PRIMARY KEY (\"nome\")" +
                ")";
        sqlStatments[7] = "CREATE TABLE IF NOT EXISTS gasto( " +
                " id_gasto INTEGER NOT NULL," +
                " nome_gasto TEXT NOT NULL," +
                " valor REAL NOT NULL," +
                " data_add TEXT," +
                " local TEXT," +
                " descricao TEXT," +
                " tipo_gasto TEXT NOT NULL," +
                " username_pessoa TEXT," +
                " nome_familia TEXT," +
                " id_animal INTEGER," +
                " id_previsao INTEGER NOT NULL," +
                " PRIMARY KEY (\"id_gasto\")," +
                " FOREIGN KEY (\"username_pessoa\")" +
                "     REFERENCES pessoa (\"username\")" +
                "     ON DELETE CASCADE ON UPDATE CASCADE," +
                " FOREIGN KEY (\"id_animal\")" +
                "     REFERENCES animal (\"id_animal\")" +
                "     ON DELETE CASCADE ON UPDATE CASCADE," +
                " FOREIGN KEY (\"nome_familia\")" +
                "     REFERENCES familia (\"nome\")" +
                "     ON DELETE CASCADE ON UPDATE CASCADE," +
                " FOREIGN KEY (\"id_previsao\")" +
                "     REFERENCES previsao_mensal (\"id_previsao\")" +
                "     ON DELETE CASCADE ON UPDATE CASCADE" +
                ")";

        sqlStatments[8] = "CREATE TABLE IF NOT EXISTS previsao_mensal(" +
                "id_previsao INTEGER NOT NULL," +
                "nome TEXT NOT NULL," +
                "descricao TEXT," +
                "valor_previsto REAL NOT NULL," +
                "mes INTEGER NOT NULL," +
                "ano INTEGER NOT NULL," +
                "data_add TEXT," +
                "PRIMARY KEY (\"id_previsao\")" +
                ")";

        sqlStatments[9] = "CREATE TABLE IF NOT EXISTS rendimento(" +
                "id_rendimento INTEGER NOT NULL," +
                "nome TEXT NOT NULL," +
                "valor REAL NOT NULL," +
                "data_add TEXT," +
                "descricao TEXT," +
                "local TEXT NOT NULL," +
                "username_pessoa TEXT NOT NULL," +
                "PRIMARY KEY (\"id_rendimento\")," +
                "FOREIGN KEY (\"username_pessoa\")" +
                "   REFERENCES pessoa (\"username\")" +
                "    ON DELETE CASCADE ON UPDATE CASCADE" +
                ")";

        sqlStatments[10] = "CREATE TABLE IF NOT EXISTS config(" +
                "first_time_run INTEGER NOT NULL" +
                ")";

        Connection connection = SQLiteConnection.connect();
        Statement statement =  connection.createStatement();
        for(String sqlStatement : sqlStatments){
            statement.executeUpdate(sqlStatement);
        }
        if (configTableExists()){
            statement.executeUpdate("INSERT INTO config VALUES(0)");
        }
        SQLiteConnection.closeConnection(connection, statement);
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

    private static boolean configTableExists(){
        String sql = "SELECT COUNT() FROM config";
        try {
            Connection connection = SQLiteConnection.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            int countResult = resultSet.getInt("first_time_run");
            SQLiteConnection.closeConnection(connection, statement, resultSet);
            return countResult == 1;
        } catch (SQLException e){
            return false;
        }
    }
}
