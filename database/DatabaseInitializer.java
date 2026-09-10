package com.example.oopproject.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {
    private DatabaseInitializer() {}

    public static void initialize() throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS investigators (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    specialization TEXT NOT NULL,
                    division TEXT,
                    available INTEGER NOT NULL DEFAULT 1 CHECK (available IN (0,1))
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS crime_reports (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    crime_type TEXT NOT NULL,
                    description TEXT NOT NULL,
                    location TEXT NOT NULL,
                    report_date TEXT NOT NULL,
                    status TEXT NOT NULL DEFAULT 'SUBMITTED',
                    assigned_investigator_id INTEGER,
                    FOREIGN KEY (user_id) REFERENCES users(id),
                    FOREIGN KEY (assigned_investigator_id) REFERENCES investigators(id)
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS investigations (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    case_id INTEGER NOT NULL UNIQUE,
                    investigator_id INTEGER NOT NULL,
                    notes TEXT,
                    started_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    completed_at TEXT,
                    FOREIGN KEY (case_id) REFERENCES crime_reports(id),
                    FOREIGN KEY (investigator_id) REFERENCES investigators(id)
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS evidence (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    case_id INTEGER NOT NULL,
                    evidence_type TEXT NOT NULL,
                    description TEXT NOT NULL,
                    collected_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (case_id) REFERENCES crime_reports(id)
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS case_status_history (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    case_id INTEGER NOT NULL,
                    old_status TEXT,
                    new_status TEXT NOT NULL,
                    changed_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (case_id) REFERENCES crime_reports(id)
                )
                """);
        }
    }
}
