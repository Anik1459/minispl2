package com.example.oopproject.dao;

import com.example.oopproject.database.DatabaseConnection;
import com.example.oopproject.model.Evidence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvidenceDAO {
    public int create(Evidence evidence) throws SQLException {
        String sql = "INSERT INTO evidence(case_id,evidence_type,description) " +
                "VALUES(?,?,?)";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {
            p.setInt(1, evidence.getCaseId());
            p.setString(2, evidence.getEvidenceType());
            p.setString(3, evidence.getDescription());
            p.executeUpdate();

            try (ResultSet r = p.getGeneratedKeys()) {
                if (r.next()) {
                    evidence.setId(r.getInt(1));
                    return evidence.getId();
                }
            }
        }
        return 0;
    }

    public List<Evidence> findByCaseId(int caseId) throws SQLException {
        List<Evidence> result = new ArrayList<>();

        String sql = "SELECT * FROM evidence WHERE case_id=? ORDER BY id DESC";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, caseId);

            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    result.add(new Evidence(
                            r.getInt("id"),
                            r.getInt("case_id"),
                            r.getString("evidence_type"),
                            r.getString("description"),
                            r.getString("collected_at")
                    ));
                }
            }
        }
        return result;
    }

    public boolean delete(int id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(
                     "DELETE FROM evidence WHERE id=?")) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        }
    }
}
