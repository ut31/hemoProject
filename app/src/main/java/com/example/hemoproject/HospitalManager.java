package com.example.hemoproject;


import java.util.ArrayList;
import java.util.List;

public class HospitalManager {
    private String managerName;
    private List<Hospital> hospitals;

    public HospitalManager(String managerName) {
        this.managerName = managerName;
        this.hospitals = new ArrayList<>();
    }

    public void addHospital(Hospital hospital) {
        hospitals.add(hospital);
    }

    public List<Hospital> getHospitals() {
        return hospitals;
    }
}
