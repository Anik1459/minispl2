package com.example.oopproject.state;

public class UnderReviewState implements CaseState {
    public String getName() { return "UNDER_REVIEW"; }

    public void review(CaseContext c) {
        throw new IllegalStateException("Case is already under review.");
    }

    public void assign(CaseContext c) {
        c.changeState(new AssignedState());
    }

    public void startInvestigation(CaseContext c) {
        throw new IllegalStateException("Assign the case first.");
    }

    public void resolve(CaseContext c) {
        throw new IllegalStateException("Case cannot be resolved during review.");
    }
}
