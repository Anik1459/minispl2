package com.example.oopproject.strategy;

import com.example.oopproject.model.CrimeReport;
import com.example.oopproject.model.Investigator;

import java.util.List;

public interface InvestigatorAssignmentStrategy {
    Investigator selectInvestigator(
            CrimeReport report, List<Investigator> investigators);
}
