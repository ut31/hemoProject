package com.example.hemoproject;


public class Hospital {
    private String name;
    private String managerName;
    private String email;

    public Hospital(String name, String managerName, String email) {
        this.name = name;
        this.managerName = managerName;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getManagerName() {
        return managerName;
    }

    public String getEmail() {
        return email;
    }
}
