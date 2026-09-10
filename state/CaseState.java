package com.example.oopproject.state;

public interface CaseState {
    String getName();
    void review(CaseContext context);
    void assign(CaseContext context);
    void startInvestigation(CaseContext context);
    void resolve(CaseContext context);
}
