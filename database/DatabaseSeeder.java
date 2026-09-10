package com.example.oopproject.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DatabaseSeeder {
    private DatabaseSeeder() {}

    public static void seed() throws SQLException {
        seedUser("Demo User", "user@example.com", "user123", "USER");
        seedUser("System Admin", "admin@example.com", "admin123", "ADMIN");
        seedUser("Demo Investigator", "investigator@example.com",
                "investigator123", "INVESTIGATOR");

        seedInvestigator("Rahim Ahmed", "Robbery", "Dhaka");
        seedInvestigator("Karim Hasan", "Fraud", "Dhaka");
        seedInvestigator("Nusrat Jahan", "Cybercrime", "Chittagong");
    }

    private static void seedUser(String name, String email,
                                 String password, String role) throws SQLException {
        String sql = "INSERT OR IGNORE INTO users(name,email,password,role) VALUES(?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, name);
            p.setString(2, email);
            p.setString(3, password);
            p.setString(4, role);
            p.executeUpdate();
        }
    }

    private static void seedInvestigator(String name, String specialization,
                                         String division) throws SQLException {
        String sql = "INSERT INTO investigators(name,specialization,division,available) " +
                "SELECT ?,?,?,1 WHERE NOT EXISTS " +
                "(SELECT 1 FROM investigators WHERE name=? AND specialization=?)";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, name);
            p.setString(2, specialization);
            p.setString(3, division);
            p.setString(4, name);
            p.setString(5, specialization);
            p.executeUpdate();
        }
    }
}
