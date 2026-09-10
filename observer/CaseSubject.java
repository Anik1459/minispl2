package com.example.oopproject.observer;

import java.util.ArrayList;
import java.util.List;

public class CaseSubject {
    private final int caseId;
    private String status;
    private final List<CaseObserver> observers = new ArrayList<>();

    public CaseSubject(int caseId, String initialStatus) {
        if (caseId <= 0) throw new IllegalArgumentException("Invalid case ID.");
        if (initialStatus == null || initialStatus.isBlank()) {
            throw new IllegalArgumentException("Initial status is required.");
        }

        this.caseId = caseId;
        this.status = initialStatus;
    }

    public void addObserver(CaseObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(CaseObserver observer) {
        observers.remove(observer);
    }

    public void changeStatus(String newStatus) {
        if (newStatus == null || newStatus.isBlank()) {
            throw new IllegalArgumentException("New status is required.");
        }

        String oldStatus = status;
        status = newStatus;

        for (CaseObserver observer : List.copyOf(observers)) {
            observer.update(caseId, oldStatus, newStatus);
        }
    }

    public int getCaseId() {
        return caseId;
    }

    public String getStatus() {
        return status;
    }
}
