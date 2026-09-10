package com.example.oopproject.state;

public class ResolvedState implements CaseState {
    public String getName() { return "RESOLVED"; }

    public void review(CaseContext c) {
        throw new IllegalStateException("Resolved case cannot be reviewed.");
    }

    public void assign(CaseContext c) {
        throw new IllegalStateException("Resolved case cannot be assigned.");
    }

    public void startInvestigation(CaseContext c) {
        throw new IllegalStateException("Resolved case cannot restart.");
    }

    public void resolve(CaseContext c) {
        throw new IllegalStateException("Case is already resolved.");
    }
}
