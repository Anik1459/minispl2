package com.example.oopproject.observer;

public class AdminNotification implements CaseObserver {
    @Override
    public void update(int caseId, String oldStatus, String newStatus) {
        System.out.println(
                "ADMIN: Case " + caseId +
                " changed from " + oldStatus + " to " + newStatus);
    }
}
