package com.example.oopproject.dao;

import com.example.oopproject.database.DatabaseConnection;
import com.example.oopproject.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public int create(User user) throws SQLException {
        String sql = "INSERT INTO users(name,email,password,role) VALUES(?,?,?,?)";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, user.getName());
            p.setString(2, user.getEmail());
            p.setString(3, user.getPassword());
            p.setString(4, user.getRole());
            p.executeUpdate();

            try (ResultSet r = p.getGeneratedKeys()) {
                if (r.next()) {
                    user.setId(r.getInt(1));
                    return user.getId();
                }
            }
        }
        return 0;
    }

    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id=?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email=?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, email);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    public List<User> findAll() throws SQLException {
        List<User> result = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) result.add(map(r));
        }
        return result;
    }

    public boolean update(User user) throws SQLException {
        String sql = "UPDATE users SET name=?,email=?,password=?,role=? WHERE id=?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, user.getName());
            p.setString(2, user.getEmail());
            p.setString(3, user.getPassword());
            p.setString(4, user.getRole());
            p.setInt(5, user.getId());
            return p.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(
                     "DELETE FROM users WHERE id=?")) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        }
    }

    private User map(ResultSet r) throws SQLException {
        return new User(
                r.getInt("id"),
                r.getString("name"),
                r.getString("email"),
                r.getString("password"),
                r.getString("role")
        );
    }
}
