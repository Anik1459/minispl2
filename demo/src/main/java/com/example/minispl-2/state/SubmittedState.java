package com.example.oopproject.state;

public class SubmittedState implements CaseState {
    public String getName() { return "SUBMITTED"; }

    public void review(CaseContext c) {
        c.changeState(new UnderReviewState());
    }

    public void assign(CaseContext c) {
        throw new IllegalStateException("Review the case first.");
    }

    public void startInvestigation(CaseContext c) {
        throw new IllegalStateException("Assign the case first.");
    }

    public void resolve(CaseContext c) {
        throw new IllegalStateException("Case cannot be resolved yet.");
    }
}
