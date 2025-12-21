package com.yusurg.demo.tools;

import com.yusurg.demo.model.Patient;

/**
 * Defibrillator Tool
 * Requirement: Heart must be stopped (isHeartStopped == true)
 * Success: Revive Patient (Pulse 30), reset heart stop state and death timer
 * Fail: Electrocuted yourself (no effect on patient)
 * Special: Can ONLY be used when heart is stopped - the only tool that saves from heart stop
 */
public class Defibrillator extends SurgicalTool {

    public Defibrillator() {
        super("Defibrillator");
    }

    @Override
    protected boolean checkRequirements(Patient patient) {
        // Can only be used when heart is stopped - this is the primary condition
        return patient.isHeartStopped();
    }

    @Override
    protected boolean canUseWhenHeartStopped() {
        return true; // Defibrillator is the ONLY tool that can save when heart is stopped
    }

    @Override
    protected boolean canUseWhenObstructed() {
        return true; // Defibrillator can be used even when visibility is obstructed - it's an emergency!
    }

    @Override
    protected void applySuccessEffect(Patient patient) {
        patient.setPulse(80); // Revive with pulse of 30
        patient.setHeartStopped(false); // Clear heart stop state
        patient.setMovesUntilDeath(2); // Reset death timer
    }

    @Override
    protected void applyFailEffect(Patient patient) {
        // No effect on patient - you just shock yourself
    }

    @Override
    protected String getRequirementFailMessage() {
        return "Patient has a pulse! Don't shock them!";
    }

    @Override
    protected String getSuccessMessage() {
        return "Clear! Patient revived! Heart is beating again!";
    }

    @Override
    protected String getFailMessage() {
        return "You electrocuted yourself! BZZZZT! Patient still needs help!";
    }
}

