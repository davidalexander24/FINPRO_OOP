package com.yusurg.demo.tools;

import com.yusurg.demo.model.Patient;

/**
 * Antiseptic Tool
 * Requirement: Site Unsanitary
 * Success: Site -> Sanitary
 * Fail: Spilled on shoes (no effect)
 */
public class Antiseptic extends SurgicalTool {

    public Antiseptic() {
        super("Antiseptic");
    }

    @Override
    protected boolean checkRequirements(Patient patient) {
        return !patient.isSiteSanitary();
    }

    @Override
    protected void applySuccessEffect(Patient patient) {
        patient.setSiteSanitary(true);
    }

    @Override
    protected void applyFailEffect(Patient patient) {
        // No effect
    }

    @Override
    protected String getRequirementFailMessage() {
        return "Site is already sanitary!";
    }

    @Override
    protected String getSuccessMessage() {
        return "Site cleaned and sanitized.";
    }

    @Override
    protected String getFailMessage() {
        return "Spilled on your shoes. Smells nice though!";
    }
}

