package com.example.oopproject.model;

public class CrimeReport {
    private int id;
    private int userId;
    private String crimeType;
    private String description;
    private String location;
    private String reportDate;
    private String status;
    private Integer assignedInvestigatorId;

    public CrimeReport() {}

    public CrimeReport(int id, int userId, String crimeType, String description,
                       String location, String reportDate, String status,
                       Integer assignedInvestigatorId) {
        this.id = id;
        this.userId = userId;
        this.crimeType = crimeType;
        this.description = description;
        this.location = location;
        this.reportDate = reportDate;
        this.status = status;
        this.assignedInvestigatorId = assignedInvestigatorId;
    }

    public CrimeReport(int userId, String crimeType, String description,
                       String location, String reportDate) {
        this(0, userId, crimeType, description, location, reportDate,
                "SUBMITTED", null);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getCrimeType() { return crimeType; }
    public void setCrimeType(String crimeType) { this.crimeType = crimeType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getReportDate() { return reportDate; }
    public void setReportDate(String reportDate) { this.reportDate = reportDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getAssignedInvestigatorId() { return assignedInvestigatorId; }
    public void setAssignedInvestigatorId(Integer id) {
        this.assignedInvestigatorId = id;
    }

    @Override
    public String toString() {
        return "#" + id + " - " + crimeType + " [" + status + "]";
    }
}
