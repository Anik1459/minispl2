package com.example.oopproject.model;

public class Investigation {
    private int id;
    private int caseId;
    private int investigatorId;
    private String notes;
    private String startedAt;
    private String completedAt;

    public Investigation() {}

    public Investigation(int id, int caseId, int investigatorId,
                         String notes, String startedAt, String completedAt) {
        this.id = id;
        this.caseId = caseId;
        this.investigatorId = investigatorId;
        this.notes = notes;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCaseId() { return caseId; }
    public void setCaseId(int caseId) { this.caseId = caseId; }

    public int getInvestigatorId() { return investigatorId; }
    public void setInvestigatorId(int investigatorId) {
        this.investigatorId = investigatorId;
    }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getStartedAt() { return startedAt; }
    public void setStartedAt(String startedAt) { this.startedAt = startedAt; }

    public String getCompletedAt() { return completedAt; }
    public void setCompletedAt(String completedAt) { this.completedAt = completedAt; }
}
