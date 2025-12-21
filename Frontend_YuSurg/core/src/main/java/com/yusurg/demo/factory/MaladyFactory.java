package com.yusurg.demo.factory;

import com.yusurg.demo.model.DifficultyLevel;
import com.yusurg.demo.model.Malady;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MaladyFactory {
    private static final Random random = new Random();

    // Define illness pools per difficulty
    private static final String[] EASY_POOL = {
        "Broken Arm", "Broken Leg", "Bird Flu", "Turtle Flu",
        "Monkey Flu", "Nose Job", "Swallowed a World Lock", "Lung Tumor"
    };

    private static final String[] MEDIUM_POOL = {
        "Heart Attack", "Liver Infection", "Kidney Failure", "Appendicitis",
        "Torn Punching Muscle", "Gem Cuts", "Broken Heart", "Grumbleteeth"
    };

    private static final String[] HARD_POOL = {
        "Broken Everything (Run over by Truck)", "Serious Head Injury",
        "Serious Trauma (Punctured Lung)", "Brain Tumor"
    };

    public static List<Malady> createMaladies(DifficultyLevel difficulty) {
        List<Malady> maladies = new ArrayList<>();

        String selectedIllness;
        // ALL maladies are now hidden by default and require ultrasound to reveal
        boolean isHidden = true;

        switch (difficulty) {
            case EASY:
                selectedIllness = EASY_POOL[random.nextInt(EASY_POOL.length)];
                break;
            case MEDIUM:
                selectedIllness = MEDIUM_POOL[random.nextInt(MEDIUM_POOL.length)];
                break;
            case HARD:
                selectedIllness = HARD_POOL[random.nextInt(HARD_POOL.length)];
                break;
            default:
                selectedIllness = "Unknown Condition";
                break;
        }

        // Create the single malady with appropriate description
        // All maladies are hidden and require ultrasound to reveal
        maladies.add(new Malady(
            selectedIllness,
            getDescriptionForIllness(selectedIllness),
            isHidden
        ));

        return maladies;
    }

    private static String getDescriptionForIllness(String illness) {
        switch (illness) {
            case "Broken Arm":
            case "Broken Leg":
                return "Patient has a broken bone. Use Splint to fix.";
            case "Bird Flu":
            case "Turtle Flu":
            case "Monkey Flu":
                return "Patient has a fever. Use Lab Kit to diagnose, then Antibiotic to cure.";
            case "Nose Job":
                return "Patient wants cosmetic surgery. Anesthetize, make incision, then stitch it up.";
            case "Swallowed a World Lock":
                return "Foreign object in digestive system. Anesthetize, make incision, remove, stitch.";
            case "Lung Tumor":
                return "Growth in lung. Anesthetize, make incision, remove carefully, stitch.";

            // Medium
            case "Heart Attack":
                return "Patient's pulse is dropping fast. Use Transfusion to stabilize.";
            case "Liver Infection":
            case "Kidney Failure":
                return "Organ infection detected. Use Lab Kit, then Antibiotic. May need Transfusion.";
            case "Appendicitis":
                return "Patient needs surgery. Anesthetize, make incision, clean site, stitch up.";
            case "Torn Punching Muscle":
                return "Muscle tear. Anesthetize, make incision to repair, stitch carefully.";
            case "Gem Cuts":
                return "Multiple lacerations. Clean site with Antiseptic, then use Stitches.";
            case "Broken Heart":
                return "Emotional trauma manifesting physically. Use Ultrasound to detect, then Transfusion.";
            case "Grumbleteeth":
                return "Mysterious condition. Use Ultrasound to investigate, treat symptoms.";

            // Hard
            case "Broken Everything (Run over by Truck)":
                return "Massive trauma: multiple fractures, bleeding. Use Pins, Splint, Transfusion, Clamp.";
            case "Serious Head Injury":
                return "Severe cranial trauma. Anesthetize, incise carefully, stop bleeding, stitch.";
            case "Serious Trauma (Punctured Lung)":
                return "Critical chest injury. Anesthetize, incise, clamp bleeding, repair, stitch.";
            case "Brain Tumor":
                return "Hidden brain growth. Use Ultrasound to detect. Anesthetize, incise, remove carefully, stitch.";

            default:
                return "Unknown condition. Requires full diagnosis.";
        }
    }
}

