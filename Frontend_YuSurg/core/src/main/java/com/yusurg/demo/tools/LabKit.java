package com.yusurg.demo.tools;

import com.yusurg.demo.model.Patient;

/**
 * Lab Kit Tool
 * Requirement: Always usable
 * Success: Unlock Antibiotics / Diagnose
 * Fail: Contaminated sample (no effect)
 */
public class LabKit extends SurgicalTool {

    public LabKit() {
        super("Lab Kit");
    }

    @Override
    protected boolean checkRequirements(Patient patient) {
        return true; // Always usable
    }

    @Override
    protected void applySuccessEffect(Patient patient) {
        patient.unlockAntibiotics();
    }

    @Override
    protected void applyFailEffect(Patient patient) {
        // No effect
    }

    @Override
    protected String getRequirementFailMessage() {
        return ""; // Never fails requirements
    }

    @Override
    protected String getSuccessMessage() {
        return "Sample analyzed. Antibiotics unlocked!";
    }

    @Override
    protected String getFailMessage() {
        return "Contaminated sample! Try again.";
    }
}

