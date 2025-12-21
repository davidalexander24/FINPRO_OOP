package com.yusurg.demo.tools;

import com.yusurg.demo.model.Patient;

/**
 * Clamp Tool
 * Requirement: Bleeding
 * Success: Slows Bleeding
 * Fail: Dropped it (no effect)
 */
public class Clamp extends SurgicalTool {

    public Clamp() {
        super("Clamp");
    }

    @Override
    protected boolean checkRequirements(Patient patient) {
        return patient.isBleeding();
    }

    @Override
    protected void applySuccessEffect(Patient patient) {
        // Reduce pulse loss by temporarily boosting pulse
        patient.setPulse(patient.getPulse() + 5);
    }

    @Override
    protected void applyFailEffect(Patient patient) {
        // No effect
    }

    @Override
    protected String getRequirementFailMessage() {
        return "Patient is not bleeding!";
    }

    @Override
    protected String getSuccessMessage() {
        return "Clamp applied. Bleeding slowed.";
    }

    @Override
    protected String getFailMessage() {
        return "You dropped the clamp! Butterfingers!";
    }
}

