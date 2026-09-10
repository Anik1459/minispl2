package com.example.oopproject.strategy;

import com.example.oopproject.model.CrimeReport;
import com.example.oopproject.model.Investigator;

import java.util.List;

public class InvestigatorAssignmentService {
    private InvestigatorAssignmentStrategy strategy;

    public InvestigatorAssignmentService(
            InvestigatorAssignmentStrategy strategy) {
        setStrategy(strategy);
    }

    public void setStrategy(InvestigatorAssignmentStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Assignment strategy is required.");
        }
        this.strategy = strategy;
    }

    public Investigator assign(
            CrimeReport report, List<Investigator> investigators) {

        Investigator selected = strategy.selectInvestigator(
                report, investigators);

        if (selected == null) {
            throw new IllegalStateException(
                    "No suitable available investigator found.");
        }

        return selected;
    }
}
