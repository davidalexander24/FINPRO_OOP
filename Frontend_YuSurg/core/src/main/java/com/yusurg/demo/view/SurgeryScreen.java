package com.yusurg.demo.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.yusurg.demo.controller.ToolCommand;
import com.yusurg.demo.controller.UseTool;
import com.yusurg.demo.manager.GameManager;
import com.yusurg.demo.model.Patient;
import com.yusurg.demo.tools.*;

import java.util.ArrayList;
import java.util.List;

public class SurgeryScreen implements Screen {
    private Game game;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Texture patientTexture;
    private Texture backgroundTexture;

    // Avatar textures based on patient state
    private Texture avatarAwake;
    private Texture avatarNotAwake;
    private Texture avatarDead;
    private Texture avatarSurgerySuccess;

    private Patient patient;
    private ResponsiveSurgeryUI surgeryUI;

    private List<SurgicalTool> tools;

    public SurgeryScreen(Game game) {
        this.game = game;
        this.patient = new Patient();
        initializeTools();
    }

    private void initializeTools() {
        tools = new ArrayList<>();
        tools.add(new Scalpel());
        tools.add(new LabKit());
        tools.add(new Anesthetic());
        tools.add(new Antiseptic());
        tools.add(new Antibiotic());
        tools.add(new Stitches());
        tools.add(new Sponge());
        tools.add(new Ultrasound());
        tools.add(new Defibrillator());
        tools.add(new Transfusion());
        tools.add(new Clamp());
        tools.add(new Pins());
        tools.add(new Splint());
    }

    @Override
    public void show() {
        // Use FitViewport for responsive layout that maintains aspect ratio
        stage = new Stage(new FitViewport(1280, 720));
        Gdx.input.setInputProcessor(stage);

        skin = SkinFactory.createDefaultSkin();

        // Initialize SpriteBatch for drawing patient texture
        batch = new SpriteBatch();

        // Load background texture
        backgroundTexture = new Texture(Gdx.files.internal("Background.png"));

        // Load all avatar textures
        avatarAwake = new Texture(Gdx.files.internal("Avatars/Avatar_Awake.png"));
        avatarNotAwake = new Texture(Gdx.files.internal("Avatars/Avatar_NotAwake.png"));
        avatarDead = new Texture(Gdx.files.internal("Avatars/Avatar_Dead.png"));
        avatarSurgerySuccess = new Texture(Gdx.files.internal("Avatars/Avatar_SurgerySuccess.png"));

        // Set initial patient texture
        patientTexture = avatarAwake;

        // CRITICAL: Initialize patient BEFORE creating UI
        initializePatient();

        // Create Responsive Surgery UI (passes patient to constructor for immediate initialization)
        surgeryUI = new ResponsiveSurgeryUI(skin, patient);
        patient.addObserver(surgeryUI);

        // Set up callbacks
        surgeryUI.setCompleteSurgeryCallback(() -> handleSurgeryComplete());
        surgeryUI.setFixItCallback(() -> handleFixIt());
        surgeryUI.setToolClickListener(tool -> useTool(tool));
        surgeryUI.setBackToMenuCallback(() -> game.setScreen(new MainMenuScreen(game)));

        // Populate tools in the UI
        surgeryUI.setTools(tools);

        // Add UI to stage (use getRootStack for overlay support)
        stage.addActor(surgeryUI.getRootStack());
    }

    private void initializePatient() {
        patient.reset();

        // Get the active malady
        List<com.yusurg.demo.model.Malady> maladies = GameManager.getInstance().getCurrentMaladies();
        if (maladies != null && !maladies.isEmpty()) {
            com.yusurg.demo.model.Malady malady = maladies.get(0);
            patient.setActiveMalady(malady);

            String name = malady.getName();

            // Apply specific initial conditions based on illness type
            // Bone fractures
            if (name.contains("Broken Arm") || name.contains("Broken Leg")) {
                patient.addBrokenBone();
            }
            else if (name.contains("Broken Everything") || name.contains("Run over by Truck")) {
                // Massive trauma
                patient.setPulse(40);
                patient.addShatteredBone();
                patient.addShatteredBone();
                patient.addBrokenBone();
            }
            else if (name.contains("Shattered")) {
                patient.addShatteredBone();
                patient.addShatteredBone();
            }
            // Flu types - temperature set automatically by setActiveMalady()
            // Heart/Organ conditions
            else if (name.contains("Heart Attack") || name.contains("Broken Heart")) {
                patient.setPulse(50);
            }
            else if (name.contains("Liver") || name.contains("Kidney")) {
                patient.setPulse(65);
                patient.setTemperature(101.5f);
            }
            // Head injuries
            else if (name.contains("Head Injury")) {
                patient.setPulse(45);
            }
            // Punctured lung / serious trauma
            else if (name.contains("Punctured Lung") || name.contains("Serious Trauma")) {
                patient.setPulse(35);
                patient.addIncision(); // Already has injury
            }
        }
    }



    private void useTool(SurgicalTool tool) {
        if (patient.isDead() && !(tool instanceof Defibrillator)) {
            surgeryUI.showMessage("Patient is dead! Use Defibrillator!", true);
            return;
        }

        // Command Pattern - Encapsulate the action
        ToolCommand command = new UseTool(tool, patient);
        SurgicalTool.ToolResult result = command.execute();

        // Display result
        surgeryUI.showMessage(result.message, !result.success);

        // Update UI
        surgeryUI.updateVitals(patient);

        // Only check for game lost condition (death)
        if (GameManager.getInstance().isGameLost()) {
            showDefeatDialog();
        }
    }

    private void handleFixIt() {
        // Check if at correct surgical depth
        if (patient.getActiveMalady() != null &&
            patient.getSurgicalDepth() >= patient.getActiveMalady().getRequiredIncisionDepth()) {
            // Mark the internal trauma as fixed
            patient.setMaladyFixed(true);
            patient.markInternalTraumaFixed();
            surgeryUI.showMessage("Internal trauma repaired!", false);
        } else {
            surgeryUI.showMessage("Need to make deeper incisions first!", true);
        }
    }

    private void handleSurgeryComplete() {
        // Verify surgery is actually complete
        if (patient.isSurgeryComplete() && patient.getActiveMalady() != null) {
            // Mark malady as officially cured
            patient.getActiveMalady().cure();
            GameManager.getInstance().checkWinCondition();
            showSuccessDialog();
        } else {
            // Build detailed error message showing what's missing
            StringBuilder missing = new StringBuilder("Missing: ");
            boolean hasIssue = false;

            if (patient.getTemperature() > 98.6f) {
                missing.append("Temp > 98.6°F, ");
                hasIssue = true;
            }
            if (patient.getIncisions() > 0) {
                missing.append("Close incisions, ");
                hasIssue = true;
            }
            if (patient.getBrokenBoneCount() > 0) {
                missing.append("Fix broken bones, ");
                hasIssue = true;
            }
            if (patient.getShatteredBoneCount() > 0) {
                missing.append("Fix shattered bones, ");
                hasIssue = true;
            }
            if (patient.getActiveMalady() != null &&
                patient.getActiveMalady().requiresFixItButton() &&
                !patient.isMaladyFixed()) {
                missing.append("Press FIX IT button, ");
                hasIssue = true;
            }

            if (hasIssue) {
                // Remove trailing comma and space
                String message = missing.toString();
                if (message.endsWith(", ")) {
                    message = message.substring(0, message.length() - 2);
                }
                surgeryUI.showMessage(message, true);
            } else {
                surgeryUI.showMessage("Surgery not complete! Check all conditions.", true);
            }
        }
    }

    private void showSuccessDialog() {
        Dialog dialog = new Dialog("SURGERY SUCCESSFUL!", skin);

        // Set custom background with lower opacity for success dialog
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(new com.badlogic.gdx.graphics.Color(0.1f, 0.1f, 0.2f, 0.5f)); // 50% opacity instead of 85%
        pixmap.fill();
        com.badlogic.gdx.graphics.Texture bgTexture = new com.badlogic.gdx.graphics.Texture(pixmap);
        pixmap.dispose();
        dialog.getBackground().setMinWidth(0);
        dialog.getBackground().setMinHeight(0);
        dialog.setBackground(new com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable(bgTexture));

        dialog.getContentTable().pad(20);

        int xpGained = GameManager.getInstance().getScore();
        String message = "Patient Saved!\n\n";
        message += "Illness Cured: " + (patient.getActiveMalady() != null ?
                   patient.getActiveMalady().getName() : "N/A") + "\n";
        message += "Final Pulse: " + String.format("%.0f", patient.getPulse()) + "\n";
        message += "Final Temperature: " + String.format("%.1f F", patient.getTemperature()) + "\n";
        message += "Surgery Time: " + String.format("%.1f seconds", GameManager.getInstance().getGameTime()) + "\n";
        message += "\nXP Gained: " + xpGained;

        dialog.text(message);

        // Create status label to show saving progress and updated XP
        Label statusLabel = new Label("Saving progress...", skin);
        dialog.getContentTable().row();
        dialog.getContentTable().add(statusLabel).padTop(10);

        TextButton menuButton = new TextButton("Back to Main Menu", skin);
        menuButton.getLabel().setFontScale(1.2f);
        menuButton.setDisabled(true); // Disable until save completes
        dialog.button(menuButton, true);

        dialog.show(stage);
        dialog.setModal(true);

        // Send game result to backend
        if (GameManager.getInstance().getCurrentPlayer() != null) {
            new Thread(() -> {
                try {
                    long playerId = GameManager.getInstance().getCurrentPlayer().id;
                    float duration = GameManager.getInstance().getGameTime();
                    int previousLevel = GameManager.getInstance().getCurrentPlayer().surgeonLevel;
                    int previousTotalXp = GameManager.getInstance().getCurrentPlayer().totalExp;
                    String originalUsername = GameManager.getInstance().getCurrentPlayer().username;

                    System.out.println("[SURGERY] Starting backend update...");
                    System.out.println("[SURGERY] Player ID: " + playerId);
                    System.out.println("[SURGERY] Session ID: " + GameManager.getInstance().getCurrentSessionId());

                    com.yusurg.demo.model.PlayerData updatedPlayer;

                    // Check if we have a valid session ID
                    if (GameManager.getInstance().getCurrentSessionId() != null) {
                        // Use the normal game session end flow
                        long sessionId = GameManager.getInstance().getCurrentSessionId();
                        System.out.println("[SURGERY] Calling endGameSession with sessionId: " + sessionId);
                        updatedPlayer = com.yusurg.demo.api.BackendService.getInstance()
                            .endGameSession(sessionId, "WIN", xpGained, duration, playerId);
                        System.out.println("[SURGERY] endGameSession returned player: " + updatedPlayer);
                    } else {
                        // Fallback: Directly add XP to player when session start failed
                        System.out.println("[SURGERY] No game session found, using direct XP update...");
                        updatedPlayer = com.yusurg.demo.api.BackendService.getInstance()
                            .addPlayerXP(playerId, xpGained);
                        System.out.println("[SURGERY] addPlayerXP returned player: " + updatedPlayer);
                    }

                    System.out.println("[SURGERY] Updated player totalExp from response: " + updatedPlayer.totalExp);

                    // Always preserve original username from login
                    updatedPlayer.username = originalUsername;

                    GameManager.getInstance().setCurrentPlayer(updatedPlayer);

                    // Enhanced terminal output for XP and level update
                    System.out.println("\n========== SURGERY COMPLETE ==========");
                    System.out.println("Patient: " + (patient.getActiveMalady() != null ? patient.getActiveMalady().getName() : "N/A"));
                    System.out.println("Surgery Duration: " + String.format("%.1f seconds", duration));
                    System.out.println("---------------------------------------");
                    System.out.println("XP Earned: +" + xpGained);
                    System.out.println("Previous Total XP: " + previousTotalXp);
                    System.out.println("New Total XP: " + updatedPlayer.totalExp);
                    System.out.println("---------------------------------------");
                    if (updatedPlayer.surgeonLevel > previousLevel) {
                        System.out.println("*** LEVEL UP! ***");
                        System.out.println("Previous Level: " + previousLevel);
                    }
                    System.out.println("Surgeon Level: " + updatedPlayer.surgeonLevel);
                    System.out.println("=======================================\n");

                    // Update UI on main thread
                    Gdx.app.postRunnable(() -> {
                        statusLabel.setText("Total XP: " + updatedPlayer.totalExp + " | Level: " + updatedPlayer.surgeonLevel);
                        menuButton.setDisabled(false);
                    });
                } catch (Exception e) {
                    System.err.println("Failed to end game session: " + e.getMessage());
                    e.printStackTrace();
                    // Enable button even on error so user can continue
                    Gdx.app.postRunnable(() -> {
                        statusLabel.setText("Save failed - XP may not be updated");
                        menuButton.setDisabled(false);
                    });
                }
            }).start();
        } else {
            // No player logged in, enable button immediately
            statusLabel.setText("Not logged in - XP not saved");
            menuButton.setDisabled(false);
        }

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!menuButton.isDisabled()) {
                    game.setScreen(new MainMenuScreen(game));
                }
            }
        });
    }


    private void showDefeatDialog() {
        Dialog dialog = new Dialog("Game Over", skin);
        dialog.text("Patient Died!\nBetter luck next time!");
        dialog.button("Main Menu", true);
        dialog.show(stage);

        dialog.setModal(true);
        dialog.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        // Surgical theme background: Dark teal/green (#206060 = 0.125, 0.376, 0.376)
        Gdx.gl.glClearColor(0.125f, 0.376f, 0.376f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update patient vitals (turn-based mode - no time decay)
        if (!GameManager.getInstance().isGameWon() && !GameManager.getInstance().isGameLost()) {
            patient.update(delta);
            GameManager.getInstance().updateGameTime(delta);
        }

        // Update avatar based on patient state
        updateAvatarTexture();

        // Draw background and patient sprite in exact center using viewport coordinates
        batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
        batch.begin();

        float worldWidth = stage.getViewport().getWorldWidth();
        float worldHeight = stage.getViewport().getWorldHeight();

        // Draw background to fill the entire viewport
        batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);

        // Draw patient avatar in center
        float patientX = (worldWidth - patientTexture.getWidth()) / 2;
        float patientY = (worldHeight - patientTexture.getHeight()) / 2;
        batch.draw(patientTexture, patientX, patientY);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    private void updateAvatarTexture() {
        // Priority: Surgery Success > Dead > Not Awake > Awake
        if (GameManager.getInstance().isGameWon()) {
            patientTexture = avatarSurgerySuccess;
        } else if (patient.isDead()) {
            patientTexture = avatarDead;
        } else if (!patient.isAwake()) {
            patientTexture = avatarNotAwake;
        } else {
            patientTexture = avatarAwake;
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        if (surgeryUI != null) {
            surgeryUI.dispose();
        }
        stage.dispose();
        skin.dispose();
        if (batch != null) {
            batch.dispose();
        }
        // Dispose background texture
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        // Dispose all avatar textures
        if (avatarAwake != null) {
            avatarAwake.dispose();
        }
        if (avatarNotAwake != null) {
            avatarNotAwake.dispose();
        }
        if (avatarDead != null) {
            avatarDead.dispose();
        }
        if (avatarSurgerySuccess != null) {
            avatarSurgerySuccess.dispose();
        }
    }
}

