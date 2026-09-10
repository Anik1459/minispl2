package com.example.oopproject.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:crime_management.db";

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
