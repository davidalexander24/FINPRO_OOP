package com.yusurg.demo.tools;

import com.yusurg.demo.model.Patient;

import java.util.Random;

/**
 * Template Method Pattern
 * Purpose: Standardize the execution flow for all surgical tools
 * Flow: Check Condition -> RNG Skill Check -> Apply Effect -> Play Sound
 */
public abstract class SurgicalTool {
    protected static final Random random = new Random();
    protected static final float FAIL_CHANCE = 0.05f; // 5% fail rate

    protected String name;
    protected String successMessage;
    protected String failMessage;

    public SurgicalTool(String name) {
        this.name = name;
    }

    /**
     * Template Method - Defines the algorithm structure
     */
    public final ToolResult use(Patient patient) {
        // Step 0: Check Visibility Obstruction - Only Sponge can be used when obstructed
        if (patient.isVisibilityObstructed() && !canUseWhenObstructed()) {
            return new ToolResult(false, "You can't see what you are doing! Use the Sponge!", false);
        }

        // Step 0.5: Check Heart Stop State - Only Defibrillator can save when heart stopped
        // Other tools can still be used but will tick the death counter
        if (patient.isHeartStopped() && !canUseWhenHeartStopped()) {
            // Decrement death counter for using a non-Defibrillator tool while heart stopped
            boolean stillAlive = patient.decrementHeartStopCounter();
            if (!stillAlive) {
                return new ToolResult(false, "Patient died! Heart was stopped too long!", false);
            }
            // Continue with tool execution but warn player
        }

        // Step 1: Check if tool can be used
        if (!checkRequirements(patient)) {
            return new ToolResult(false, getRequirementFailMessage(), false);
        }

        // Step 2: RNG Skill Check (5% fail rate)
        boolean skillCheckPassed = random.nextFloat() >= FAIL_CHANCE;

        // Step 3: Apply Effect
        if (skillCheckPassed) {
            applySuccessEffect(patient);
            playSound(true);
            return new ToolResult(true, getSuccessMessage(), false);
        } else {
            applyFailEffect(patient);
            playSound(false);
            return new ToolResult(false, getFailMessage(), true);
        }
    }

    /**
     * Hook method - override to allow use when visibility is obstructed (e.g., Sponge)
     * @return true if this tool can be used when visibility is obstructed
     */
    protected boolean canUseWhenObstructed() {
        return false; // Default: cannot use when obstructed
    }

    /**
     * Hook method - override to allow use when heart is stopped (e.g., Defibrillator)
     * @return true if this tool can save the patient when heart is stopped
     */
    protected boolean canUseWhenHeartStopped() {
        return false; // Default: using this tool ticks death counter
    }

    // Abstract methods - must be implemented by concrete tools
    protected abstract boolean checkRequirements(Patient patient);
    protected abstract void applySuccessEffect(Patient patient);
    protected abstract void applyFailEffect(Patient patient);
    protected abstract String getRequirementFailMessage();
    protected abstract String getSuccessMessage();
    protected abstract String getFailMessage();

    // Hook method - can be overridden
    protected void playSound(boolean success) {
        // Sound implementation would go here
        // For now, just a placeholder
        System.out.println("[SOUND] " + name + " used " + (success ? "successfully" : "with failure"));
    }

    public String getName() {
        return name;
    }

    /**
     * Result class to return tool usage outcome
     */
    public static class ToolResult {
        public final boolean success;
        public final String message;
        public final boolean wasSkillFail; // true if failed due to RNG, false if requirement not met

        public ToolResult(boolean success, String message, boolean wasSkillFail) {
            this.success = success;
            this.message = message;
            this.wasSkillFail = wasSkillFail;
        }
    }
}

