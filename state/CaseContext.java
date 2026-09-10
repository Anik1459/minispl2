package com.example.oopproject.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CaseContext {
    private final int caseId;
    private CaseState state;
    private final List<String> history = new ArrayList<>();

    public CaseContext(int caseId) {
        if (caseId <= 0) {
            throw new IllegalArgumentException("Case ID must be positive.");
        }

        this.caseId = caseId;
        this.state = new SubmittedState();
        history.add(state.getName());
    }

    public int getCaseId() {
        return caseId;
    }

    public CaseState getState() {
        return state;
    }

    public String getStatus() {
        return state.getName();
    }

    public List<String> getHistory() {
        return Collections.unmodifiableList(history);
    }

    void changeState(CaseState newState) {
        this.state = newState;
        history.add(newState.getName());
    }

    public void review() {
        state.review(this);
    }

    public void assign() {
        state.assign(this);
    }

    public void startInvestigation() {
        state.startInvestigation(this);
    }

    public void resolve() {
        state.resolve(this);
    }
}
