package com.yusurg.demo.tools;

import com.yusurg.demo.model.Patient;

/**
 * Sponge Tool
 * Requirement: Always usable
 * Success: Clean Site / Fix Visibility / Clear Obstruction
 * Fail: None (humorous message)
 * Special: Can be used even when visibility is obstructed
 */
public class Sponge extends SurgicalTool {

    public Sponge() {
        super("Sponge");
    }

    @Override
    protected boolean checkRequirements(Patient patient) {
        return true; // Always usable
    }

    @Override
    protected boolean canUseWhenObstructed() {
        return true; // Sponge can ALWAYS be used, even when obstructed
    }

    @Override
    protected void applySuccessEffect(Patient patient) {
        patient.setSiteSanitary(true);
        patient.setVisibilityLow(false);
        patient.setVisibilityObstructed(false); // Clear obstruction
    }

    @Override
    protected void applyFailEffect(Patient patient) {
        // No negative effect, just funny message
    }

    @Override
    protected String getRequirementFailMessage() {
        return ""; // Never fails requirements
    }

    @Override
    protected String getSuccessMessage() {
        return "Site cleaned successfully. Visibility restored.";
    }

    @Override
    protected String getFailMessage() {
        return "You ate the sponge. That's... weird.";
    }
}

