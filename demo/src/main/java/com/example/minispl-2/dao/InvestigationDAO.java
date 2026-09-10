package com.example.oopproject.dao;

import com.example.oopproject.database.DatabaseConnection;
import com.example.oopproject.model.Investigation;

import java.sql.*;

public class InvestigationDAO {
    public int create(Investigation investigation) throws SQLException {
        String sql = "INSERT INTO investigations(case_id,investigator_id,notes) " +
                "VALUES(?,?,?)";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {
            p.setInt(1, investigation.getCaseId());
            p.setInt(2, investigation.getInvestigatorId());
            p.setString(3, investigation.getNotes());
            p.executeUpdate();

            try (ResultSet r = p.getGeneratedKeys()) {
                if (r.next()) {
                    investigation.setId(r.getInt(1));
                    return investigation.getId();
                }
            }
        }
        return 0;
    }

    public Investigation findByCaseId(int caseId) throws SQLException {
        String sql = "SELECT * FROM investigations WHERE case_id=?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, caseId);

            try (ResultSet r = p.executeQuery()) {
                if (!r.next()) return null;

                return new Investigation(
                        r.getInt("id"),
                        r.getInt("case_id"),
                        r.getInt("investigator_id"),
                        r.getString("notes"),
                        r.getString("started_at"),
                        r.getString("completed_at")
                );
            }
        }
    }

    public boolean updateNotes(int id, String notes) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(
                     "UPDATE investigations SET notes=? WHERE id=?")) {
            p.setString(1, notes);
            p.setInt(2, id);
            return p.executeUpdate() > 0;
        }
    }

    public boolean complete(int id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(
                     "UPDATE investigations SET completed_at=CURRENT_TIMESTAMP WHERE id=?")) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        }
    }
}
