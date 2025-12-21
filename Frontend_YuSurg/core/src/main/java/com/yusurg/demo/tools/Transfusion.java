package com.yusurg.demo.tools;

import com.yusurg.demo.model.Patient;

/**
 * Transfusion Tool
 * Requirement: Always usable
 * Success: +Pulse
 * Fail: Spilled blood everywhere, Site Unsanitary
 */
public class Transfusion extends SurgicalTool {

    public Transfusion() {
        super("Transfusion");
    }

    @Override
    protected boolean checkRequirements(Patient patient) {
        return true; // Always usable
    }

    @Override
    protected void applySuccessEffect(Patient patient) {
        patient.setPulse(patient.getPulse() + 20);
    }

    @Override
    protected void applyFailEffect(Patient patient) {
        patient.setSiteSanitary(false);
    }

    @Override
    protected String getRequirementFailMessage() {
        return ""; // Never fails requirements
    }

    @Override
    protected String getSuccessMessage() {
        return "Blood transfusion successful. Pulse increased.";
    }

    @Override
    protected String getFailMessage() {
        return "Spilled blood everywhere! Site contaminated!";
    }
}

