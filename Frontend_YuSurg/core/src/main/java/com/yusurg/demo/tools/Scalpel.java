package com.yusurg.demo.tools;

import com.yusurg.demo.model.Patient;

/**
 * Scalpel Tool
 * Requirement: Patient Unconscious
 * Success: +1 Incision, Slight Bleeding
 * Fail: Heavy Bleeding
 */
public class Scalpel extends SurgicalTool {

    public Scalpel() {
        super("Scalpel");
    }

    @Override
    protected boolean checkRequirements(Patient patient) {
        return !patient.isAwake();
    }

    @Override
    protected void applySuccessEffect(Patient patient) {
        patient.addIncision();
        patient.incrementSurgicalDepth(); // Track depth for Fix It button
    }

    @Override
    protected void applyFailEffect(Patient patient) {
        patient.addIncision();
        patient.incrementSurgicalDepth(); // Track depth for Fix It button
        patient.setPulse(patient.getPulse() - 10); // Heavy bleeding effect
    }

    @Override
    protected String getRequirementFailMessage() {
        return "Patient must be unconscious before using scalpel!";
    }

    @Override
    protected String getSuccessMessage() {
        return "Clean incision made.";
    }

    @Override
    protected String getFailMessage() {
        return "This will leave a nasty scar! Heavy bleeding!";
    }
}

