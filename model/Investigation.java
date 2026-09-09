

public class Investigation {

    private int investigationId;
    private int reportId;
    private int investigatorId;

    private String startDate;
    private String findings;
    private String status;

    public Investigation() {
    }

    public Investigation(int investigationId,
                         int reportId,
                         int investigatorId,
                         String startDate,
                         String findings,
                         String status) {

        this.investigationId = investigationId;
        this.reportId = reportId;
        this.investigatorId = investigatorId;
        this.startDate = startDate;
        this.findings = findings;
        this.status = status;
    }

    public Investigation(int reportId,
                         int investigatorId,
                         String startDate,
                         String findings,
                         String status) {

        this.reportId = reportId;
        this.investigatorId = investigatorId;
        this.startDate = startDate;
        this.findings = findings;
        this.status = status;
    }

    public int getInvestigationId() {
        return investigationId;
    }

    public void setInvestigationId(int investigationId) {
        this.investigationId = investigationId;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getInvestigatorId() {
        return investigatorId;
    }

    public void setInvestigatorId(int investigatorId) {
        this.investigatorId = investigatorId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
