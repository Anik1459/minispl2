package com.example.oopproject.observer;

public class InvestigatorNotification implements CaseObserver {
    private final String investigatorName;

    public InvestigatorNotification(String investigatorName) {
        this.investigatorName = investigatorName;
    }

    @Override
    public void update(int caseId, String oldStatus, String newStatus) {
        System.out.println(
                "INVESTIGATOR " + investigatorName +
                ": Case " + caseId +
                " changed from " + oldStatus + " to " + newStatus);
    }
}
