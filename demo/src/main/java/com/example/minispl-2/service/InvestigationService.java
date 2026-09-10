package com.example.oopproject.service;

import com.example.oopproject.dao.CrimeReportDAO;
import com.example.oopproject.dao.InvestigationDAO;
import com.example.oopproject.dao.InvestigatorDAO;
import com.example.oopproject.model.CrimeReport;
import com.example.oopproject.model.Investigation;
import com.example.oopproject.model.Investigator;

import java.sql.SQLException;

public class InvestigationService {
    private final CrimeReportDAO caseDAO;
    private final InvestigatorDAO investigatorDAO;
    private final InvestigationDAO investigationDAO;

    public InvestigationService() {
        this(new CrimeReportDAO(), new InvestigatorDAO(), new InvestigationDAO());
    }

    public InvestigationService(CrimeReportDAO caseDAO,
                                 InvestigatorDAO investigatorDAO,
                                 InvestigationDAO investigationDAO) {
        this.caseDAO = caseDAO;
        this.investigatorDAO = investigatorDAO;
        this.investigationDAO = investigationDAO;
    }

    public void assignInvestigator(int caseId, int investigatorId)
            throws SQLException {
        CrimeReport report = caseDAO.findById(caseId);
        Investigator investigator = investigatorDAO.findById(investigatorId);

        if (report == null) throw new IllegalArgumentException("Case not found.");
        if (investigator == null)
            throw new IllegalArgumentException("Investigator not found.");
        if (!investigator.isAvailable())
            throw new IllegalArgumentException("Investigator is not available.");

        report.setAssignedInvestigatorId(investigatorId);
        report.setStatus("ASSIGNED");
        caseDAO.update(report);

        Investigation existing = investigationDAO.findByCaseId(caseId);
        if (existing == null) {
            investigationDAO.create(new Investigation(
                    0, caseId, investigatorId,
                    "Investigation assigned.", null, null));
        }

        investigatorDAO.setAvailability(investigatorId, false);
    }

    public void updateNotes(int investigationId, String notes)
            throws SQLException {
        if (notes == null || notes.isBlank())
            throw new IllegalArgumentException("Notes cannot be empty.");

        investigationDAO.updateNotes(investigationId, notes);
    }

    public void completeInvestigation(int caseId, int investigatorId)
            throws SQLException {
        Investigation investigation = investigationDAO.findByCaseId(caseId);

        if (investigation == null)
            throw new IllegalArgumentException("Investigation not found.");
        if (investigation.getInvestigatorId() != investigatorId)
            throw new IllegalArgumentException("Investigator does not own this case.");

        investigationDAO.complete(investigation.getId());
        caseDAO.updateStatus(caseId, "RESOLVED");
        investigatorDAO.setAvailability(investigatorId, true);
    }
}
