package com.crimemanagement.model;

public class CrimeReport {

    private int reportId;
    private int userId;

    private String crimeType;

    private String division;
    private String district;
    private String thana;

    private String date;
    private String time;

    private String description;

    private String status;

    public CrimeReport() {
    }

    public CrimeReport(int reportId,
                       int userId,
                       String crimeType,
                       String division,
                       String district,
                       String thana,
                       String date,
                       String time,
                       String description,
                       String status) {

        this.reportId = reportId;
        this.userId = userId;
        this.crimeType = crimeType;
        this.division = division;
        this.district = district;
        this.thana = thana;
        this.date = date;
        this.time = time;
        this.description = description;
        this.status = status;
    }

    public CrimeReport(int userId,
                       String crimeType,
                       String division,
                       String district,
                       String thana,
                       String date,
                       String time,
                       String description,
                       String status) {

        this.userId = userId;
        this.crimeType = crimeType;
        this.division = division;
        this.district = district;
        this.thana = thana;
        this.date = date;
        this.time = time;
        this.description = description;
        this.status = status;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCrimeType() {
        return crimeType;
    }

    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getThana() {
        return thana;
    }

    public void setThana(String thana) {
        this.thana = thana;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
