package com.example.oopproject.dao;

import com.example.oopproject.database.DatabaseConnection;
import com.example.oopproject.model.CrimeReport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CrimeReportDAO {
    public int create(CrimeReport report) throws SQLException {
        String sql = "INSERT INTO crime_reports " +
                "(user_id,crime_type,description,location,report_date,status) " +
                "VALUES(?,?,?,?,?,'SUBMITTED')";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {
            p.setInt(1, report.getUserId());
            p.setString(2, report.getCrimeType());
            p.setString(3, report.getDescription());
            p.setString(4, report.getLocation());
            p.setString(5, report.getReportDate());
            p.executeUpdate();

            try (ResultSet r = p.getGeneratedKeys()) {
                if (r.next()) {
                    report.setId(r.getInt(1));
                    return report.getId();
                }
            }
        }
        return 0;
    }

    public CrimeReport findById(int id) throws SQLException {
        String sql = "SELECT * FROM crime_reports WHERE id=?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    public List<CrimeReport> findByUserId(int userId) throws SQLException {
        return queryByInt(
                "SELECT * FROM crime_reports WHERE user_id=? ORDER BY id DESC",
                userId);
    }

    public List<CrimeReport> findByStatus(String status) throws SQLException {
        return queryByString(
                "SELECT * FROM crime_reports WHERE status=? ORDER BY id DESC",
                status);
    }

    public List<CrimeReport> search(String keyword) throws SQLException {
        String value = "%" + (keyword == null ? "" : keyword.trim()) + "%";
        List<CrimeReport> result = new ArrayList<>();

        String sql = "SELECT * FROM crime_reports " +
                "WHERE crime_type LIKE ? OR location LIKE ? OR description LIKE ? " +
                "ORDER BY id DESC";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, value);
            p.setString(2, value);
            p.setString(3, value);

            try (ResultSet r = p.executeQuery()) {
                while (r.next()) result.add(map(r));
            }
        }
        return result;
    }

    public boolean update(CrimeReport report) throws SQLException {
        String sql = "UPDATE crime_reports SET crime_type=?,description=?," +
                "location=?,report_date=?,status=?,assigned_investigator_id=? WHERE id=?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, report.getCrimeType());
            p.setString(2, report.getDescription());
            p.setString(3, report.getLocation());
            p.setString(4, report.getReportDate());
            p.setString(5, report.getStatus());

            if (report.getAssignedInvestigatorId() == null) {
                p.setNull(6, Types.INTEGER);
            } else {
                p.setInt(6, report.getAssignedInvestigatorId());
            }

            p.setInt(7, report.getId());
            return p.executeUpdate() > 0;
        }
    }

    public boolean updateStatus(int id, String status) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(
                     "UPDATE crime_reports SET status=? WHERE id=?")) {
            p.setString(1, status);
            p.setInt(2, id);
            return p.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(
                     "DELETE FROM crime_reports WHERE id=?")) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        }
    }

    private List<CrimeReport> queryByInt(String sql, int value) throws SQLException {
        List<CrimeReport> result = new ArrayList<>();

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, value);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) result.add(map(r));
            }
        }
        return result;
    }

    private List<CrimeReport> queryByString(String sql, String value)
            throws SQLException {
        List<CrimeReport> result = new ArrayList<>();

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, value);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) result.add(map(r));
            }
        }
        return result;
    }

    private CrimeReport map(ResultSet r) throws SQLException {
        int investigatorId = r.getInt("assigned_investigator_id");
        Integer assigned = r.wasNull() ? null : investigatorId;

        return new CrimeReport(
                r.getInt("id"),
                r.getInt("user_id"),
                r.getString("crime_type"),
                r.getString("description"),
                r.getString("location"),
                r.getString("report_date"),
                r.getString("status"),
                assigned
        );
    }
}
