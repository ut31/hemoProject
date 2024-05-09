package com.example.hemoproject;

public class Donor {
    private String name;
    private String email;
    private String government;
    private String bloodType;
    private String phone; // Add phone number field

    public Donor(String name, String email, String government, String bloodType, String phone) {
        this.name = name;
        this.email = email;
        this.government = government;
        this.bloodType = bloodType;
        this.phone = phone; // Initialize phone number
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGovernment() {
        return government;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getPhone() {
        return phone;
    }
}
