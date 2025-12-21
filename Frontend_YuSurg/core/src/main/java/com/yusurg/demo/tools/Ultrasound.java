package com.yusurg.demo.tools;

import com.yusurg.demo.model.Patient;
import com.yusurg.demo.manager.GameManager;
import com.yusurg.demo.model.Malady;

import java.util.List;

/**
 * Ultrasound Tool
 * Requirement: Always usable
 * Success: Reveal Hidden Maladies
 * Fail: Scanned the nurse (no effect)
 */
public class Ultrasound extends SurgicalTool {

    public Ultrasound() {
        super("Ultrasound");
    }

    @Override
    protected boolean checkRequirements(Patient patient) {
        return true; // Always usable
    }

    @Override
    protected void applySuccessEffect(Patient patient) {
        // Reveal all hidden maladies
        List<Malady> maladies = GameManager.getInstance().getCurrentMaladies();
        if (maladies != null) {
            for (Malady malady : maladies) {
                if (malady.isHidden()) {
                    malady.reveal();
                }
            }
        }
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
        return "Ultrasound complete. Hidden conditions revealed!";
    }

    @Override
    protected String getFailMessage() {
        return "You scanned the nurse instead. She's healthy!";
    }
}

