package com.rafaelwillen.database.dao.family;

import com.rafaelwillen.database.SQLiteConnection;
import com.rafaelwillen.database.dao.AccessObject;
import com.rafaelwillen.model.family.Parent;
import com.rafaelwillen.model.family.Person;
import com.rafaelwillen.model.family.Sex;
import com.rafaelwillen.model.family.Son;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;

public class PersonDAO implements AccessObject<Person> {

    private final static String TABLE_NAME = "pessoa";
    private final static String FIELD_USERNAME = "username";
    private final static String FIELD_PERSON_NAME = "nome";
    private final static String FIELD_BIRTH_DATE = "data_nascimento";
    private final static String FIELD_PASSWORD = "password";
    private final static String FIELD_SEX = "sexo";
    private final static String FIELD_PERSON_TYPE = "tipo_pessoa";
    private final static PersonDAO personDAO = new PersonDAO();
    private static Connection connection;
    private static String sqlStatement;

    private PersonDAO() {
    }

    public static PersonDAO getInstance() {
        return personDAO;
    }

    public LinkedList<Person> getAll() throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s", TABLE_NAME);
        LinkedList<Person> people = new LinkedList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStatement);
        while (resultSet.next()) {
            people.add(buildPerson(resultSet));
        }
        SQLiteConnection.closeConnection(connection, statement, resultSet);
        return people;
    }

    private Person buildPerson(ResultSet resultSet) throws SQLException {
        String username = resultSet.getString(FIELD_USERNAME);
        String name = resultSet.getString(FIELD_PERSON_NAME);
        LocalDate birthDate = LocalDate.parse(resultSet.getString(FIELD_BIRTH_DATE));
        String password = resultSet.getString(FIELD_PASSWORD);
        Sex sex = Sex.valueOf(resultSet.getString(FIELD_SEX));

        Person person;
        switch (resultSet.getString(FIELD_PERSON_TYPE)) {
            case "PARENTE":
                if (sex.equals(Sex.FEMININO)) {
                    person = Parent.buildMother(username, name, birthDate, password);
                } else {
                    person = Parent.buildFather(username, name, birthDate, password);
                }
                break;
            case "FILHO":
                person = new Son(username, name, birthDate, password, sex);
                break;
            default:
                throw new NullPointerException("The variable 'person' was not initialized");
        }
        if (person.getEmails().size() > 0)
        person.getEmails().addAll(PhoneNumberDAO.getInstance().getAllFromPessoa(username));
        if (person.getPhoneNumbers().size() > 0)
        person.getEmails().addAll(EmailDAO.getInstance().getAllFromPessoa(username));
        return person;
    }

    @Override
    public void delete(Object id) throws SQLException {
        if (!(id instanceof String)) {
            throw new IllegalArgumentException("The argument 'id' must be a String");
        }
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("DELETE FROM %s WHERE %s=?", TABLE_NAME, FIELD_USERNAME);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, (String) id);
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
    }

    @Override
    public void add(Person object) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?)", TABLE_NAME, FIELD_USERNAME, FIELD_PERSON_NAME, FIELD_BIRTH_DATE, FIELD_PASSWORD, FIELD_SEX, FIELD_PERSON_TYPE);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, object.getUsername());
        preparedStatement.setString(2, object.getName());
        preparedStatement.setString(3, object.getBirthDate().toString());
        preparedStatement.setString(4, object.getPassword());
        preparedStatement.setString(5, object.getSex().name());
        preparedStatement.setString(6, object instanceof Parent ? "PARENTE" : "FILHO");
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
        PhoneNumberDAO.getInstance().addAll(object.getUsername(), object.getPhoneNumbers());
        EmailDAO.getInstance().addAll(object.getUsername(), object.getEmails());

    }

    @Override
    public void update(Object id, Person object) throws SQLException {
        if (!(id instanceof String)) {
            throw new IllegalArgumentException("The argument 'id' must be a String");
        }
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("UPDATE %s SET %s=?,%s=?,%s=?,%s=? WHERE %s=?", TABLE_NAME, FIELD_PERSON_NAME, FIELD_BIRTH_DATE, FIELD_PASSWORD, FIELD_SEX, FIELD_USERNAME);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, object.getName());
        preparedStatement.setString(2, object.getBirthDate().toString());
        preparedStatement.setString(3, object.getPassword());
        preparedStatement.setString(4, object.getSex().name());
        preparedStatement.setString(5, id.toString());
        preparedStatement.executeUpdate();
        SQLiteConnection.closeConnection(connection, preparedStatement);
        // TODO: After adding the emailDAO and phoneNumberDAO, check if it will delete
    }

    @Override
    public Person get(Object id) throws SQLException {
        if (!(id instanceof String)) {
            throw new IllegalArgumentException("The argument 'id' must be a String");
        }
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_NAME, FIELD_USERNAME);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, (String) id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Person person = buildPerson(resultSet);
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return person;
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
        String username;
        if (object instanceof Person) {
            username = ((Person) object).getUsername();
        } else if (!(object instanceof String)) {
            throw new IllegalArgumentException("The argument 'id' must be a String");
        } else {
            username = (String) object;
        }
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT COUNT() FROM %s WHERE %s=?", TABLE_NAME, FIELD_USERNAME);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        boolean result = resultSet.getInt(1) != 0;
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return result;
    }

    public boolean exists(String username, String password) throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT COUNT() FROM %s WHERE %s=? OR %s=?", TABLE_NAME, FIELD_USERNAME, FIELD_PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        boolean result = resultSet.getInt(1) != 0;
        SQLiteConnection.closeConnection(connection, preparedStatement, resultSet);
        return result;
    }

    public Parent getMother() throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s WHERE %s='PARENTE' AND %s='FEMININO'", TABLE_NAME, FIELD_PERSON_TYPE, FIELD_SEX);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStatement);
        if (!resultSet.next()) return null;
        Parent mother = (Parent) buildPerson(resultSet);
        SQLiteConnection.closeConnection(connection, statement, resultSet);
        return mother;
    }

    public Parent getFather() throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s WHERE %s='PARENTE' AND %s='MASCULINO'", TABLE_NAME, FIELD_PERSON_TYPE, FIELD_SEX);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStatement);
        if (!resultSet.next()) return null;
        Parent father = (Parent) buildPerson(resultSet);
        SQLiteConnection.closeConnection(connection, statement, resultSet);
        return father;
    }

    public LinkedList<Son> getAllSons() throws SQLException {
        connection = SQLiteConnection.connect();
        sqlStatement = String.format("SELECT * FROM %s WHERE %s='FILHO'", TABLE_NAME, FIELD_PERSON_TYPE);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStatement);
        LinkedList<Son> sons = new LinkedList<>();
        while (resultSet.next()) {
            sons.add((Son) buildPerson(resultSet));
        }
        return sons;
    }

    @Override
    public int getLastID() {
        try {
            throw new IllegalAccessException("This method must no be called");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
