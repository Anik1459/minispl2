package com.example.oopproject.strategy;

import com.example.oopproject.model.CrimeReport;
import com.example.oopproject.model.Investigator;

import java.util.List;

public class LocationBasedAssignment
        implements InvestigatorAssignmentStrategy {

    @Override
    public Investigator selectInvestigator(
            CrimeReport report, List<Investigator> investigators) {

        if (report == null || investigators == null) return null;

        for (Investigator investigator : investigators) {
            if (investigator.isAvailable()
                    && investigator.getDivision() != null
                    && investigator.getDivision()
                    .equalsIgnoreCase(report.getLocation())) {
                return investigator;
            }
        }

        return investigators.stream()
                .filter(Investigator::isAvailable)
                .findFirst()
                .orElse(null);
    }
}
