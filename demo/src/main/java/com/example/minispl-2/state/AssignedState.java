package com.example.oopproject.state;

public class AssignedState implements CaseState {
    public String getName() { return "ASSIGNED"; }

    public void review(CaseContext c) {
        throw new IllegalStateException("Case is already assigned.");
    }

    public void assign(CaseContext c) {
        throw new IllegalStateException("Case is already assigned.");
    }

    public void startInvestigation(CaseContext c) {
        c.changeState(new UnderInvestigationState());
    }

    public void resolve(CaseContext c) {
        throw new IllegalStateException("Start the investigation first.");
    }
}
