package com.yusurg.demo.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.yusurg.demo.model.Malady;
import com.yusurg.demo.model.Patient;
import com.yusurg.demo.model.PatientObserver;
import com.yusurg.demo.model.DifficultyLevel;
import com.yusurg.demo.manager.GameManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponsiveSurgeryUI implements PatientObserver {
    private Skin skin;
    private Table rootTable;

    private static final Color PRIMARY_BG = new Color(0.1f, 0.1f, 0.2f, 0.85f);
    private static final Color ACCENT_COLOR = new Color(0.0f, 0.8f, 0.8f, 1f);
    private static final Color DANGER_COLOR = new Color(0.9f, 0.2f, 0.2f, 1f);
    private static final Color SUCCESS_COLOR = new Color(0.2f, 0.9f, 0.2f, 1f);
    private static final Color WARNING_COLOR = new Color(1.0f, 0.8f, 0.0f, 1f);
    private static final Color DIGITAL_GRAY = new Color(0.6f, 0.6f, 0.6f, 1f);

    private Map<String, Texture> toolTextures = new HashMap<>();

    private Label patientNameLabel;
    private Label difficultyLabel;
    private Label conditionLabel;
    private Label pulseValueLabel;
    private Label tempValueLabel;
    private Label statusLabel;
    private Label incisionsLabel;
    private Label brokenBonesLabel;
    private Label shatteredBonesLabel;

    private TextButton fixItButton;
    private TextButton completeSurgeryButton;
    private Table toolsContainer;

    private Label messageLabel;

    private Label visibilityObstructedLabel;
    private Image visibilityObstructedImage;
    private Texture visibilityObstructedTexture;

    private Label heartStopLabel;

    private FixItCallback fixItCallback;
    private CompleteSurgeryCallback completeSurgeryCallback;
    private ToolClickListener toolClickListener;
    private BackToMenuCallback backToMenuCallback;

    public ResponsiveSurgeryUI(Skin skin, Patient patient) {
        this.skin = skin;
        createUI();
        onVitalsChanged(patient);
    }

    public interface FixItCallback { void onFixIt(); }
    public interface CompleteSurgeryCallback { void onCompleteSurgery(); }
    public interface ToolClickListener { void onToolClicked(com.yusurg.demo.tools.SurgicalTool tool); }
    public interface BackToMenuCallback { void onBackToMenu(); }

    public void setFixItCallback(FixItCallback callback) { this.fixItCallback = callback; }
    public void setCompleteSurgeryCallback(CompleteSurgeryCallback callback) { this.completeSurgeryCallback = callback; }
    public void setToolClickListener(ToolClickListener listener) { this.toolClickListener = listener; }
    public void setBackToMenuCallback(BackToMenuCallback callback) { this.backToMenuCallback = callback; }

    private void createUI() {
        rootTable = new Table();
        rootTable.setFillParent(true);

        Table mainArea = new Table();

        Table leftSidebar = createLeftSidebar();
        mainArea.add(leftSidebar).width(260).fillY().top().left().pad(10);

        mainArea.add().expand().fill();

        rootTable.add(mainArea).grow().row();

        Table bottomToolbar = createBottomToolbar();
        rootTable.add(bottomToolbar).growX().height(140).bottom();

        heartStopLabel = new Label("HEART STOPPED! (Moves left: 2)", skin);
        heartStopLabel.setFontScale(1.5f);
        heartStopLabel.setColor(DANGER_COLOR);
        heartStopLabel.setAlignment(Align.center);
        heartStopLabel.setVisible(false);

        visibilityObstructedTexture = new Texture(Gdx.files.internal("VisibilityObstructed.png"));
        visibilityObstructedImage = new Image(visibilityObstructedTexture);
        visibilityObstructedImage.setFillParent(true);
        visibilityObstructedImage.setVisible(false);
        visibilityObstructedImage.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled);
    }

    private Table createLeftSidebar() {
        Table sidebar = new Table();
        sidebar.setBackground(createBackground(PRIMARY_BG));
        sidebar.pad(12);
        sidebar.top().left();

        Label headerLabel = new Label("PATIENT CHART", skin);
        headerLabel.setFontScale(1.2f);
        headerLabel.setColor(ACCENT_COLOR);
        sidebar.add(headerLabel).left().padBottom(15).row();

        patientNameLabel = new Label("Patient: ---", skin);
        sidebar.add(patientNameLabel).left().padBottom(3).row();

        difficultyLabel = new Label("Difficulty: Easy", skin);
        difficultyLabel.setColor(WARNING_COLOR);
        sidebar.add(difficultyLabel).left().padBottom(15).row();

        Label condHeader = new Label("CONDITION", skin);
        condHeader.setFontScale(0.8f);
        condHeader.setColor(DIGITAL_GRAY);
        sidebar.add(condHeader).left().row();

        conditionLabel = new Label("Loading...", skin);
        conditionLabel.setFontScale(1.2f);
        conditionLabel.setColor(WARNING_COLOR);
        conditionLabel.setWrap(true);
        sidebar.add(conditionLabel).left().width(230).padBottom(20).row();

        Label pulseHeader = new Label("HEART RATE", skin);
        pulseHeader.setFontScale(0.8f);
        pulseHeader.setColor(DIGITAL_GRAY);
        sidebar.add(pulseHeader).left().row();

        pulseValueLabel = new Label("100", skin);
        pulseValueLabel.setFontScale(2.2f);
        pulseValueLabel.setColor(SUCCESS_COLOR);
        sidebar.add(pulseValueLabel).left().padBottom(10).row();

        Label tempHeader = new Label("TEMP (°F)", skin);
        tempHeader.setFontScale(0.8f);
        tempHeader.setColor(DIGITAL_GRAY);
        sidebar.add(tempHeader).left().row();

        tempValueLabel = new Label("98.6", skin);
        tempValueLabel.setFontScale(2.2f);
        tempValueLabel.setColor(SUCCESS_COLOR);
        sidebar.add(tempValueLabel).left().padBottom(15).row();

        Table statusGrid = new Table();
        statusGrid.left();

        Label incLabel = new Label("Incisions: ", skin);
        incLabel.setColor(ACCENT_COLOR);
        incisionsLabel = new Label("0", skin);
        statusGrid.add(incLabel).left();
        statusGrid.add(incisionsLabel).left().row();

        Label brokenLabel = new Label("Broken Bones: ", skin);
        brokenLabel.setColor(ACCENT_COLOR);
        brokenBonesLabel = new Label("0", skin);
        statusGrid.add(brokenLabel).left();
        statusGrid.add(brokenBonesLabel).left().row();

        Label shatteredLabel = new Label("Shattered: ", skin);
        shatteredLabel.setColor(ACCENT_COLOR);
        shatteredBonesLabel = new Label("0", skin);
        statusGrid.add(shatteredLabel).left();
        statusGrid.add(shatteredBonesLabel).left().row();

        sidebar.add(statusGrid).left().padBottom(10).row();

        Label statusHeader = new Label("STATUS", skin);
        statusHeader.setFontScale(0.8f);
        statusHeader.setColor(DIGITAL_GRAY);
        sidebar.add(statusHeader).left().row();

        statusLabel = new Label("Awake", skin);
        statusLabel.setFontScale(1.3f);
        statusLabel.setColor(DANGER_COLOR);
        sidebar.add(statusLabel).left().row();

        visibilityObstructedLabel = new Label("YOU CAN'T SEE WHAT YOU ARE DOING!", skin);
        visibilityObstructedLabel.setFontScale(0.9f);
        visibilityObstructedLabel.setColor(DANGER_COLOR);
        visibilityObstructedLabel.setVisible(false);
        visibilityObstructedLabel.setWrap(true);
        sidebar.add(visibilityObstructedLabel).left().width(280).padTop(5).row();

        sidebar.add().expand().fill();

        return sidebar;
    }

    private Table createBottomToolbar() {
        Table toolbar = new Table();
        toolbar.setBackground(createBackground(new Color(0.08f, 0.08f, 0.15f, 0.95f)));
        toolbar.pad(5);

        messageLabel = new Label("Ready to begin surgery...", skin);
        messageLabel.setColor(ACCENT_COLOR);
        toolbar.add(messageLabel).colspan(2).center().padBottom(3).row();

        toolsContainer = new Table();
        toolsContainer.center();
        toolbar.add(toolsContainer).expandX().center().padRight(10);

        Table actionsColumn = new Table();
        actionsColumn.right();

        fixItButton = new TextButton("FIX IT!", skin);
        fixItButton.getLabel().setFontScale(1.2f);
        fixItButton.setColor(new Color(1f, 0.5f, 0.1f, 1f));
        fixItButton.setVisible(false);
        fixItButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (fixItCallback != null) fixItCallback.onFixIt();
            }
        });
        actionsColumn.add(fixItButton).width(130).height(40).padBottom(3).row();

        completeSurgeryButton = new TextButton("COMPLETE", skin);
        completeSurgeryButton.getLabel().setFontScale(1.0f);
        completeSurgeryButton.setColor(SUCCESS_COLOR);
        completeSurgeryButton.setVisible(false);
        completeSurgeryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (completeSurgeryCallback != null) completeSurgeryCallback.onCompleteSurgery();
            }
        });
        actionsColumn.add(completeSurgeryButton).width(130).height(40);

        toolbar.add(actionsColumn).right().padRight(10);

        return toolbar;
    }

    public void setTools(List<com.yusurg.demo.tools.SurgicalTool> tools) {
        toolsContainer.clear();
        toolsContainer.defaults().pad(3);

        for (com.yusurg.demo.tools.SurgicalTool tool : tools) {
            Table toolBtn = createToolButton(tool);
            toolsContainer.add(toolBtn).width(75).height(100);
        }

        TextButton backBtn = new TextButton("Menu", skin);
        backBtn.getLabel().setFontScale(0.8f);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (backToMenuCallback != null) backToMenuCallback.onBackToMenu();
            }
        });
        toolsContainer.add(backBtn).width(60).height(100);
    }

    private Table createToolButton(com.yusurg.demo.tools.SurgicalTool tool) {
        Table btn = new Table();

        String assetName = getToolAssetName(tool.getName());
        String texturePath = "tools_asset/" + assetName + ".png";
        Texture tex = loadTexture(texturePath);

        Image icon = new Image(tex);
        Label label = new Label(tool.getName(), skin);
        label.setFontScale(0.5f);
        label.setAlignment(Align.center);
        label.setColor(Color.WHITE);

        float iconSize = tool.getName().equalsIgnoreCase("ultrasound") ? 40 : 64;

        Table iconContainer = new Table();
        iconContainer.add(icon).size(iconSize).expand().center();

        btn.add(iconContainer).height(64).width(64).padTop(2).row();
        btn.add(label).padTop(2).width(70);

        btn.setTransform(true);
        btn.setOrigin(Align.center);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btn.addAction(Actions.sequence(
                    Actions.scaleTo(0.9f, 0.9f, 0.05f),
                    Actions.scaleTo(1f, 1f, 0.05f)
                ));
                if (toolClickListener != null) toolClickListener.onToolClicked(tool);
            }
        });

        return btn;
    }

    private Texture loadTexture(String path) {
        if (toolTextures.containsKey(path)) return toolTextures.get(path);

        Texture tex;
        try {
            tex = new Texture(Gdx.files.internal(path));
        } catch (Exception e) {
            Pixmap pm = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
            pm.setColor(Color.DARK_GRAY);
            pm.fill();
            pm.setColor(Color.WHITE);
            pm.drawRectangle(0, 0, 63, 63);
            tex = new Texture(pm);
            pm.dispose();
        }
        toolTextures.put(path, tex);
        return tex;
    }

    private String getToolAssetName(String toolName) {
        switch (toolName.toLowerCase()) {
            case "scalpel": return "scapel";
            case "anesthetic": return "anesthetics";
            case "antiseptic": return "antiseptics";
            case "antibiotic": return "antibiotics";
            case "lab kit": return "labkit";
            case "ultrasound": return "ultrasound";
            default: return toolName.toLowerCase().replace(" ", "_");
        }
    }

    private TextureRegionDrawable createBackground(Color color) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(color);
        pm.fill();
        Texture tex = new Texture(pm);
        pm.dispose();
        return new TextureRegionDrawable(tex);
    }

    public void updateVitals(Patient patient) {
        float pulse = patient.getPulse();
        if (patient.isHeartStopped()) {
            pulseValueLabel.setText("0");
            pulseValueLabel.setColor(DANGER_COLOR);
            heartStopLabel.setText("HEART STOPPED! (Moves left: " + patient.getMovesUntilDeath() + ")");
            heartStopLabel.setVisible(true);
            heartStopLabel.clearActions();
            heartStopLabel.addAction(Actions.forever(Actions.sequence(
                Actions.alpha(1f, 0.3f),
                Actions.alpha(0.3f, 0.3f)
            )));
        } else {
            pulseValueLabel.setText(String.format("%.0f", pulse));
            if (pulse >= 70) pulseValueLabel.setColor(SUCCESS_COLOR);
            else if (pulse >= 50) pulseValueLabel.setColor(WARNING_COLOR);
            else if (pulse >= 20) pulseValueLabel.setColor(new Color(1f, 0.5f, 0f, 1f));
            else pulseValueLabel.setColor(DANGER_COLOR);
            heartStopLabel.setVisible(false);
            heartStopLabel.clearActions();
        }

        if (patient.isVisibilityObstructed()) {
            visibilityObstructedLabel.setVisible(true);
            visibilityObstructedImage.setVisible(true);
        } else {
            visibilityObstructedLabel.setVisible(false);
            visibilityObstructedImage.setVisible(false);
        }

        float temp = patient.getTemperature();
        tempValueLabel.setText(String.format("%.1f", temp));
        if (temp < 100) tempValueLabel.setColor(SUCCESS_COLOR);
        else if (temp <= 102) tempValueLabel.setColor(WARNING_COLOR);
        else if (temp <= 104) tempValueLabel.setColor(new Color(1f, 0.5f, 0f, 1f));
        else tempValueLabel.setColor(DANGER_COLOR);

        incisionsLabel.setText(String.valueOf(patient.getIncisions()));
        boolean fixReady = patient.getActiveMalady() != null &&
            patient.getActiveMalady().requiresFixItButton() &&
            patient.getSurgicalDepth() >= patient.getActiveMalady().getRequiredIncisionDepth();
        if (fixReady) incisionsLabel.setColor(SUCCESS_COLOR);
        else if (patient.getIncisions() > 0) incisionsLabel.setColor(WARNING_COLOR);
        else incisionsLabel.setColor(Color.WHITE);

        String status = patient.getAwakenessStatus();
        statusLabel.setText(status);
        if (status.equals("Unconscious")) statusLabel.setColor(SUCCESS_COLOR);
        else if (status.equals("Coming To!")) statusLabel.setColor(WARNING_COLOR);
        else if (status.equals("Awake")) statusLabel.setColor(DANGER_COLOR);
        else if (status.contains("Scream")) statusLabel.setColor(DANGER_COLOR);
        else statusLabel.setColor(WARNING_COLOR);

        brokenBonesLabel.setText(String.valueOf(patient.getBrokenBoneCount()));
        brokenBonesLabel.setColor(patient.getBrokenBoneCount() > 0 ? new Color(1f, 0.5f, 0f, 1f) : Color.WHITE);

        shatteredBonesLabel.setText(String.valueOf(patient.getShatteredBoneCount()));
        shatteredBonesLabel.setColor(patient.getShatteredBoneCount() > 0 ? DANGER_COLOR : Color.WHITE);

        updateConditionDisplay();

        if (GameManager.getInstance().getCurrentDifficulty() != null) {
            difficultyLabel.setText("Difficulty: " + GameManager.getInstance().getCurrentDifficulty().name());

            if (GameManager.getInstance().getCurrentDifficulty() == DifficultyLevel.EASY) {
                patientNameLabel.setText("Patient");
            } else {
                patientNameLabel.setText("Patient: " + GameManager.getInstance().getCurrentDifficulty().getPatientName());
            }
        }

        fixItButton.setVisible(fixReady);
        completeSurgeryButton.setVisible(patient.isReadyToFinish());
    }

    private void updateConditionDisplay() {
        List<Malady> maladies = GameManager.getInstance().getCurrentMaladies();
        if (maladies == null || maladies.isEmpty()) {
            conditionLabel.setText("None");
            return;
        }

        StringBuilder sb = new StringBuilder();
        boolean hasVisible = false;
        for (Malady m : maladies) {
            if (m.isVisible()) {
                if (hasVisible) sb.append("\n");
                sb.append(m.getName());
                if (m.isCured()) sb.append(" ✓");
                hasVisible = true;
            }
        }
        conditionLabel.setText(hasVisible ? sb.toString() : "Use Ultrasound");
    }

    public void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setColor(isError ? DANGER_COLOR : ACCENT_COLOR);
    }

    @Override
    public void onVitalsChanged(Patient patient) {
        updateVitals(patient);
    }

    @Override
    public void onPatientDied() {
        showMessage("PATIENT DIED!", true);
    }

    public Stack getRootStack() {
        Stack stack = new Stack();
        stack.setFillParent(true);
        stack.add(rootTable);

        Table heartStopOverlay = new Table();
        heartStopOverlay.setFillParent(true);
        heartStopOverlay.top().padTop(50);
        heartStopOverlay.add(heartStopLabel).center();
        stack.add(heartStopOverlay);

        stack.add(visibilityObstructedImage);

        return stack;
    }

    public Table getRootTable() {
        return rootTable;
    }

    public void dispose() {
        for (Texture tex : toolTextures.values()) {
            tex.dispose();
        }
        toolTextures.clear();

        if (visibilityObstructedTexture != null) {
            visibilityObstructedTexture.dispose();
        }
    }
}

