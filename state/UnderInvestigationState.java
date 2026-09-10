package com.example.oopproject.state;

public class UnderInvestigationState implements CaseState {
    public String getName() { return "UNDER_INVESTIGATION"; }

    public void review(CaseContext c) {
        throw new IllegalStateException("Investigation is already in progress.");
    }

    public void assign(CaseContext c) {
        throw new IllegalStateException("Case is already assigned.");
    }

    public void startInvestigation(CaseContext c) {
        throw new IllegalStateException("Investigation already started.");
    }

    public void resolve(CaseContext c) {
        c.changeState(new ResolvedState());
    }
}
