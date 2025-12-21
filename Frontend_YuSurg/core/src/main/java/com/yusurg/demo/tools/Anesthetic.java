package com.yusurg.demo.tools;

import com.yusurg.demo.model.Patient;

/**
 * Anesthetic Tool
 * Requirement: Patient Awake
 * Success: Status -> Unconscious
 * Fail: Blur Screen (you inhaled it)
 */
public class Anesthetic extends SurgicalTool {

    public Anesthetic() {
        super("Anesthetic");
    }

    @Override
    protected boolean checkRequirements(Patient patient) {
        return true; // Can always use anesthetic to re-anesthetize
    }

    @Override
    protected void applySuccessEffect(Patient patient) {
        patient.setAwake(false);
        // Note: resolveTurn() already resets actionsSinceAnesthesia counter
    }

    @Override
    protected void applyFailEffect(Patient patient) {
        patient.setAwake(false); // Still works on patient
        // Blur effect would be handled by UI
    }

    @Override
    protected String getRequirementFailMessage() {
        return ""; // Never fails requirements
    }

    @Override
    protected String getSuccessMessage() {
        return "Patient is now unconscious.";
    }

    @Override
    protected String getFailMessage() {
        return "You inhaled it yourself! Vision blurred!";
    }
}

