package com.yusurg.demo.model;

public enum DifficultyLevel {
    EASY("Dafa"),
    MEDIUM("David"),
    HARD("Yusri");

    private final String patientName;

    DifficultyLevel(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientName() {
        return patientName;
    }
}

