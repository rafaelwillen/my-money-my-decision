package com.rafaelwillen.database.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface AccessObject<T> {

    void delete(Object id) throws SQLException;

    void add(T object) throws SQLException;

    void update(Object id, T object) throws SQLException;

    T get(Object id) throws SQLException;

    boolean isTableEmpty() throws SQLException;

    boolean exists(Object object) throws SQLException;

    int getLastID() throws SQLException;

}
