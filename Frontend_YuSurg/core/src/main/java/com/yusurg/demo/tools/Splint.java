package com.yusurg.demo.tools;

import com.yusurg.demo.model.Patient;

/**
 * Splint Tool
 * Requirement: Broken Bone
 * Success: Fix Broken Bone
 * Fail: Cut the patient (Bleeding)
 */
public class Splint extends SurgicalTool {

    public Splint() {
        super("Splint");
    }

    @Override
    protected boolean checkRequirements(Patient patient) {
        return patient.getBrokenBoneCount() > 0;
    }

    @Override
    protected void applySuccessEffect(Patient patient) {
        patient.removeBrokenBone();

        // Auto-cure bone-fracture illnesses when all bones are fixed
        if (patient.getActiveMalady() != null &&
            patient.getBrokenBoneCount() == 0 &&
            patient.getShatteredBoneCount() == 0) {
            String name = patient.getActiveMalady().getName();
            if (name.contains("Broken") || name.contains("Bone")) {
                patient.getActiveMalady().cure();
            }
        }
    }

    @Override
    protected void applyFailEffect(Patient patient) {
        patient.addIncision(); // Cut from mishandling splint
        patient.setPulse(patient.getPulse() - 5);
    }

    @Override
    protected String getRequirementFailMessage() {
        return "No broken bones to splint!";
    }

    @Override
    protected String getSuccessMessage() {
        return "Broken bone set with splint.";
    }

    @Override
    protected String getFailMessage() {
        return "You cut the patient with the splint! Bleeding!";
    }
}

