package com.example.oopproject.dao;

import com.example.oopproject.database.DatabaseConnection;
import com.example.oopproject.model.Investigator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvestigatorDAO {
    public int create(Investigator investigator) throws SQLException {
        String sql = "INSERT INTO investigators " +
                "(name,specialization,division,available) VALUES(?,?,?,?)";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, investigator.getName());
            p.setString(2, investigator.getSpecialization());
            p.setString(3, investigator.getDivision());
            p.setInt(4, investigator.isAvailable() ? 1 : 0);
            p.executeUpdate();

            try (ResultSet r = p.getGeneratedKeys()) {
                if (r.next()) {
                    investigator.setId(r.getInt(1));
                    return investigator.getId();
                }
            }
        }
        return 0;
    }

    public Investigator findById(int id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(
                     "SELECT * FROM investigators WHERE id=?")) {
            p.setInt(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    public List<Investigator> findAll() throws SQLException {
        return query("SELECT * FROM investigators ORDER BY id");
    }

    public List<Investigator> findAvailable() throws SQLException {
        return query("SELECT * FROM investigators WHERE available=1 ORDER BY id");
    }

    public boolean setAvailability(int id, boolean available) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(
                     "UPDATE investigators SET available=? WHERE id=?")) {
            p.setInt(1, available ? 1 : 0);
            p.setInt(2, id);
            return p.executeUpdate() > 0;
        }
    }

    private List<Investigator> query(String sql) throws SQLException {
        List<Investigator> result = new ArrayList<>();

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) result.add(map(r));
        }
        return result;
    }

    private Investigator map(ResultSet r) throws SQLException {
        return new Investigator(
                r.getInt("id"),
                r.getString("name"),
                r.getString("specialization"),
                r.getString("division"),
                r.getInt("available") == 1
        );
    }
}
