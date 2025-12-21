package com.yusurg.demo.model;

public class Malady {
    private String name;
    private String description;
    private boolean cured;
    private boolean hidden; // Requires ultrasound to reveal
    private boolean requiresFeverManagement; // Does this illness involve fever?
    private int requiredIncisionDepth; // How many scalpel cuts needed before Fix It appears
    private boolean requiresFixItButton; // Does this illness need the Fix It button?

    public Malady(String name, String description, boolean hidden) {
        this.name = name;
        this.description = description;
        this.cured = false;
        this.hidden = hidden;
        this.requiresFeverManagement = false;
        this.requiredIncisionDepth = 0;
        this.requiresFixItButton = false;

        // Configure based on illness type
        configureMaladyProperties();
    }

    private void configureMaladyProperties() {
        // Flu types - require fever management
        if (name.contains("Flu")) {
            requiresFeverManagement = true;
            requiredIncisionDepth = 0;
            requiresFixItButton = false;
        }
        // Bone fractures - no fever, no surgery
        else if (name.equals("Broken Arm")) {
            requiresFeverManagement = false;
            requiredIncisionDepth = 0;
            requiresFixItButton = false;
        }
        else if (name.equals("Broken Leg")) {
            requiresFeverManagement = false;
            requiredIncisionDepth = 1;
            requiresFixItButton = true;
        }
        else if (name.contains("Broken Everything") || name.contains("Run over by")) {
            requiresFeverManagement = true;
            requiredIncisionDepth = 1; // Variable per GDD
            requiresFixItButton = true;
        }
        // Surgery-type illnesses
        else if (name.equals("Nose Job") || name.equals("Lung Tumor")) {
            requiresFeverManagement = false;
            requiredIncisionDepth = 1;
            requiresFixItButton = true;
        }
        else if (name.equals("Heart Attack")) {
            requiresFeverManagement = true;
            requiredIncisionDepth = 2;
            requiresFixItButton = true;
        }
        else if (name.equals("Brain Tumor")) {
            requiresFeverManagement = false;
            requiredIncisionDepth = 5;
            requiresFixItButton = true;
        }
        else if (name.equals("Liver Infection") || name.equals("Kidney Failure") ||
                 name.equals("Swallowed a World Lock") || name.contains("Herniated Disc") ||
                 name.contains("Damaged spine")) {
            requiresFeverManagement = true;
            requiredIncisionDepth = 2;
            requiresFixItButton = true;
        }
        else if (name.equals("Appendicitis")) {
            requiresFeverManagement = true;
            requiredIncisionDepth = 3;
            requiresFixItButton = true;
        }
        else if (name.equals("Serious Head Injury") || name.equals("Torn Punching Muscle")) {
            requiresFeverManagement = false;
            requiredIncisionDepth = 3;
            requiresFixItButton = true;
        }
        else if (name.contains("Serious Trauma") && name.contains("Punctured")) {
            requiresFeverManagement = false;
            requiredIncisionDepth = 3;
            requiresFixItButton = true;
        }
        else if (name.contains("Massive Trauma")) {
            requiresFeverManagement = false;
            requiredIncisionDepth = 3;
            requiresFixItButton = true;
        }
        else if (name.equals("Gem Cuts")) {
            requiresFeverManagement = false;
            requiredIncisionDepth = 2; // "examine the wounds"
            requiresFixItButton = true;
        }
        else if (name.equals("Broken Heart")) {
            requiresFeverManagement = true;
            requiredIncisionDepth = 1;
            requiresFixItButton = true;
        }
        else if (name.equals("Grumbleteeth")) {
            requiresFeverManagement = true;
            requiredIncisionDepth = 1;
            requiresFixItButton = true;
        }
    }

    public void cure() {
        this.cured = true;
    }

    public void reveal() {
        this.hidden = false;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isCured() { return cured; }
    public boolean isHidden() { return hidden; }
    public boolean isVisible() { return !hidden; }
    public boolean requiresFeverManagement() { return requiresFeverManagement; }
    public int getRequiredIncisionDepth() { return requiredIncisionDepth; }
    public boolean requiresFixItButton() { return requiresFixItButton; }
}

