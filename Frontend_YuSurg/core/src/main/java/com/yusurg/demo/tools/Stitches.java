package com.yusurg.demo.tools;

import com.yusurg.demo.model.Patient;

/**
 * Stitches Tool
 * Requirement: Incision > 0
 * Success: -1 Incision, Stop Bleeding
 * Fail: Bleeding continues
 */
public class Stitches extends SurgicalTool {

    public Stitches() {
        super("Stitches");
    }

    @Override
    protected boolean checkRequirements(Patient patient) {
        return patient.getIncisions() > 0;
    }

    @Override
    protected void applySuccessEffect(Patient patient) {
        patient.removeIncision();

        // Mark internal trauma as fixed when closing incisions (surgery completed)
        if (patient.getIncisions() == 0) {
            patient.markInternalTraumaFixed();
        }
    }

    @Override
    protected void applyFailEffect(Patient patient) {
        // Incision stays, bleeding continues
    }

    @Override
    protected String getRequirementFailMessage() {
        return "No incisions to stitch!";
    }

    @Override
    protected String getSuccessMessage() {
        return "Incision stitched. Bleeding stopped.";
    }

    @Override
    protected String getFailMessage() {
        return "You tied yourself up! Incision still open!";
    }
}

