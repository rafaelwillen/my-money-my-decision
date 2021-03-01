package com.rafaelwillen.database.dao.finance;

import com.rafaelwillen.database.SQLiteConnection;
import com.rafaelwillen.database.dao.AccessObject;
import com.rafaelwillen.model.finance.Income;
import com.rafaelwillen.model.finance.MonthlyPrevision;

import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedList;

public class MonthlyPrevisionDAO implements AccessObject<MonthlyPrevision> {

    private final static String TABLE_NAME = "previsao_mensal";
    private final static String FIELD_ID = "id_previsao";
    private final static String FIELD_NAME = "nome_previsao";
    private final static String FIELD_DESCRIPTION = "descricao";
    private final static String FIELD_VALUE = "valor_previsto";
    private final static String FIELD_MONTH = "mes";
    private final static String FIELD_YEAR = "ano";
    private final static String FIELD_ADDED_DATE = "data_add";
    private final static MonthlyPrevisionDAO monthlyPrevision = new MonthlyPrevisionDAO();
    private static Connection connection;
    private static String sqlStatement;

    private MonthlyPrevisionDAO() {
    }

    public static MonthlyPrevisionDAO getInstance() {
        return monthlyPrevision;
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
    public void add(MonthlyPrevision object) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?,?)", TABLE_NAME, FIELD_ID, FIELD_NAME, FIELD_DESCRIPTION, FIELD_VALUE, FIELD_MONTH, FIELD_YEAR, FIELD_ADDED_DATE);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, object.getId());
        preparedStatement.setString(2, object.getName());
        preparedStatement.setString(3, object.getDescription());
        preparedStatement.setDouble(4, object.getPredictedValue());
        preparedStatement.setString(5, object.getMonth().name());
        preparedStatement.setInt(6, object.getYear());
        preparedStatement.setString(7, object.getAddedDate().toString());
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    @Override
    public void update(Object id, MonthlyPrevision object) throws SQLException {
        if (!(id instanceof Integer)) throw new IllegalArgumentException("The argument 'id' must be a int");
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("UPDATE %s SET %s=?,%s=?,%s=?,%s=?,%s=?,%s=? WHERE %s=?", TABLE_NAME, FIELD_NAME, FIELD_DESCRIPTION, FIELD_VALUE, FIELD_MONTH, FIELD_YEAR, FIELD_ADDED_DATE, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, object.getName());
        preparedStatement.setString(2, object.getDescription());
        preparedStatement.setDouble(3, object.getPredictedValue());
        preparedStatement.setString(4, object.getMonth().name());
        preparedStatement.setInt(5, object.getYear());
        preparedStatement.setString(6, object.getAddedDate().toString());
        preparedStatement.setInt(7, object.getId());
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    @Override
    public MonthlyPrevision get(Object id) throws SQLException {
        if (!(id instanceof Integer)) throw new IllegalArgumentException("The argument 'id' must be a int");
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_NAME, FIELD_ID);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, (Integer) id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) return null;
        MonthlyPrevision monthlyPrevision = buildMonthlyPrevision(resultSet);
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return monthlyPrevision;
    }

    public LinkedList<MonthlyPrevision> getAll() throws SQLException{
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s", TABLE_NAME);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStatement);
        LinkedList<MonthlyPrevision> monthlyPrevisions = new LinkedList<>();
        while (resultSet.next()) {
            monthlyPrevisions.add(buildMonthlyPrevision(resultSet));
        }
        SQLiteConnection.closeConnection(connection, statement, resultSet);
        return monthlyPrevisions;
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

    public boolean exists(Month month, int year) throws SQLException{
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT COUNT() FROM %s WHERE %s=? AND %s=?", TABLE_NAME, FIELD_MONTH, FIELD_YEAR);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, month.name());
        preparedStatement.setInt(2, year);
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

    private MonthlyPrevision buildMonthlyPrevision(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(FIELD_ID);
        String name = resultSet.getString(FIELD_NAME);
        String description = resultSet.getString(FIELD_DESCRIPTION);
        double value = resultSet.getDouble(FIELD_VALUE);
        Month month = Month.valueOf(resultSet.getString(FIELD_MONTH));
        int year = resultSet.getInt(FIELD_YEAR);
        LocalDate date = LocalDate.parse(resultSet.getString(FIELD_ADDED_DATE));
        MonthlyPrevision monthlyPrevision = new MonthlyPrevision(id, name, value, month, year, date);
        monthlyPrevision.setDescription(description);
        monthlyPrevision.getCosts().addAll(CostDAO.getInstance().getAllFromPrevision(id));
        return monthlyPrevision;
    }
}
