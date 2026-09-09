

public class Investigator {

    private int investigatorId;
    private String name;
    private String specialization;
    private String district;
    private boolean available;

    public Investigator() {
    }

    public Investigator(int investigatorId,
                        String name,
                        String specialization,
                        String district,
                        boolean available) {

        this.investigatorId = investigatorId;
        this.name = name;
        this.specialization = specialization;
        this.district = district;
        this.available = available;
    }

    public Investigator(String name,
                        String specialization,
                        String district,
                        boolean available) {

        this.name = name;
        this.specialization = specialization;
        this.district = district;
        this.available = available;
    }

    public int getInvestigatorId() {
        return investigatorId;
    }

    public void setInvestigatorId(int investigatorId) {
        this.investigatorId = investigatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
