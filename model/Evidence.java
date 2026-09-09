
public class Evidence {

    private int evidenceId;
    private int investigationId;

    private String type;
    private String description;
    private String status;

    public Evidence() {
    }

    public Evidence(int evidenceId,
                    int investigationId,
                    String type,
                    String description,
                    String status) {

        this.evidenceId = evidenceId;
        this.investigationId = investigationId;
        this.type = type;
        this.description = description;
        this.status = status;
    }

    public Evidence(int investigationId,
                    String type,
                    String description,
                    String status) {

        this.investigationId = investigationId;
        this.type = type;
        this.description = description;
        this.status = status;
    }

    public int getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(int evidenceId) {
        this.evidenceId = evidenceId;
    }

    public int getInvestigationId() {
        return investigationId;
    }

    public void setInvestigationId(int investigationId) {
        this.investigationId = investigationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
