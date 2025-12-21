package com.yusurg.demo.tools;

import com.yusurg.demo.model.Patient;

/**
 * Antibiotic Tool
 * Requirement: Unlocked by LabKit
 * Success: Lower Temp (Min 98.6)
 * Fail: Temp Rises
 */
public class Antibiotic extends SurgicalTool {

    public Antibiotic() {
        super("Antibiotic");
    }

    @Override
    protected boolean checkRequirements(Patient patient) {
        return patient.isAntibioticUnlocked();
    }

    @Override
    protected void applySuccessEffect(Patient patient) {
        float newTemp = Math.max(98.6f, patient.getTemperature() - 2.0f);
        patient.setTemperature(newTemp);

        // Auto-cure Flu illnesses when temperature reaches <= 98.6F (STRICT)
        if (patient.getActiveMalady() != null &&
            patient.getActiveMalady().getName().contains("Flu") &&
            newTemp <= 98.6f) {
            patient.getActiveMalady().cure();
        }
    }

    @Override
    protected void applyFailEffect(Patient patient) {
        patient.setTemperature(patient.getTemperature() + 1.5f);
    }

    @Override
    protected String getRequirementFailMessage() {
        return "Must use Lab Kit first to unlock antibiotics!";
    }

    @Override
    protected String getSuccessMessage() {
        return "Antibiotics administered. Temperature decreasing.";
    }

    @Override
    protected String getFailMessage() {
        return "The bacteria like it! Temperature rising!";
    }
}

