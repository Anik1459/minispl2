package com.example.oopproject.observer;

public interface CaseObserver {
    void update(int caseId, String oldStatus, String newStatus);
}
