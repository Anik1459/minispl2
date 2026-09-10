package com.example.oopproject.model;

public class Investigator {
    private int id;
    private String name;
    private String specialization;
    private String division;
    private boolean available;

    public Investigator() {}

    public Investigator(int id, String name, String specialization,
                        String division, boolean available) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.division = division;
        this.available = available;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return name + " (" + specialization + ")";
    }
}
