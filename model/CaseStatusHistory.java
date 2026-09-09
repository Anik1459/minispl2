package com.crimemanagement.model;

public class CaseStatusHistory {

    private int historyId;
    private int reportId;

    private String oldStatus;
    private String newStatus;
    private String changedAt;

    public CaseStatusHistory() {
    }

    public CaseStatusHistory(int historyId,
                             int reportId,
                             String oldStatus,
                             String newStatus,
                             String changedAt) {

        this.historyId = historyId;
        this.reportId = reportId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedAt = changedAt;
    }

    public CaseStatusHistory(int reportId,
                             String oldStatus,
                             String newStatus,
                             String changedAt) {

        this.reportId = reportId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedAt = changedAt;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(String changedAt) {
        this.changedAt = changedAt;
    }
}
