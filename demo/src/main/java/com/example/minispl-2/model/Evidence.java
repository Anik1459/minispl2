package com.example.oopproject.model;

public class Evidence {
    private int id;
    private int caseId;
    private String evidenceType;
    private String description;
    private String collectedAt;

    public Evidence() {}

    public Evidence(int id, int caseId, String evidenceType,
                    String description, String collectedAt) {
        this.id = id;
        this.caseId = caseId;
        this.evidenceType = evidenceType;
        this.description = description;
        this.collectedAt = collectedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCaseId() { return caseId; }
    public void setCaseId(int caseId) { this.caseId = caseId; }

    public String getEvidenceType() { return evidenceType; }
    public void setEvidenceType(String evidenceType) {
        this.evidenceType = evidenceType;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCollectedAt() { return collectedAt; }
    public void setCollectedAt(String collectedAt) {
        this.collectedAt = collectedAt;
    }
}
