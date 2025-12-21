package com.yusurg.demo.tools;

import com.yusurg.demo.model.Patient;

/**
 * Pins Tool
 * Requirement: Shattered Bone
 * Success: Shattered -> Broken
 * Fail: Jabbed artery! Heavy Bleeding
 */
public class Pins extends SurgicalTool {

    public Pins() {
        super("Pins");
    }

    @Override
    protected boolean checkRequirements(Patient patient) {
        return patient.getShatteredBoneCount() > 0;
    }

    @Override
    protected void applySuccessEffect(Patient patient) {
        patient.removeShatteredBone();
        patient.addBrokenBone();
    }

    @Override
    protected void applyFailEffect(Patient patient) {
        patient.setPulse(patient.getPulse() - 15); // Heavy bleeding
        patient.addIncision(); // Represents the damage
    }

    @Override
    protected String getRequirementFailMessage() {
        return "No shattered bones to pin!";
    }

    @Override
    protected String getSuccessMessage() {
        return "Shattered bone stabilized with pins.";
    }

    @Override
    protected String getFailMessage() {
        return "You jabbed an artery! Heavy bleeding!";
    }
}

