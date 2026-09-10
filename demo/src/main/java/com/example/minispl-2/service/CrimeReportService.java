package com.example.oopproject.service;

import com.example.oopproject.dao.CrimeReportDAO;
import com.example.oopproject.model.CrimeReport;

import java.sql.SQLException;
import java.util.List;

public class CrimeReportService {
    private final CrimeReportDAO dao;

    public CrimeReportService() {
        this(new CrimeReportDAO());
    }

    public CrimeReportService(CrimeReportDAO dao) {
        this.dao = dao;
    }

    public int submitReport(CrimeReport report) throws SQLException {
        validate(report);
        return dao.create(report);
    }

    public CrimeReport getCase(int id) throws SQLException {
        return dao.findById(id);
    }

    public List<CrimeReport> getUserCases(int userId) throws SQLException {
        return dao.findByUserId(userId);
    }

    public List<CrimeReport> getCasesByStatus(String status) throws SQLException {
        return dao.findByStatus(status);
    }

    public List<CrimeReport> searchCases(String keyword) throws SQLException {
        return dao.search(keyword);
    }

    public boolean update(CrimeReport report) throws SQLException {
        validate(report);
        return dao.update(report);
    }

    public boolean delete(int id) throws SQLException {
        return dao.delete(id);
    }

    private void validate(CrimeReport report) {
        if (report == null) throw new IllegalArgumentException("Report is required.");
        if (report.getUserId() <= 0)
            throw new IllegalArgumentException("Valid user ID is required.");
        if (isBlank(report.getCrimeType()))
            throw new IllegalArgumentException("Crime type is required.");
        if (isBlank(report.getDescription()))
            throw new IllegalArgumentException("Description is required.");
        if (isBlank(report.getLocation()))
            throw new IllegalArgumentException("Location is required.");
        if (isBlank(report.getReportDate()))
            throw new IllegalArgumentException("Report date is required.");
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
