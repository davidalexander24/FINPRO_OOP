package com.yusurg.demo.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yusurg.demo.api.AuthService;
import com.yusurg.demo.factory.MaladyFactory;
import com.yusurg.demo.manager.GameManager;
import com.yusurg.demo.model.DifficultyLevel;
import com.yusurg.demo.model.Malady;

import java.util.List;

public class MainMenuScreen implements Screen {
    private Game game;
    private Stage stage;
    private Skin skin;

    public MainMenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Create default skin
        skin = SkinFactory.createDefaultSkin();

        // Create UI
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Title
        Label titleLabel = new Label("YuSurg - Surgery Simulator", skin);
        titleLabel.setFontScale(2f);
        table.add(titleLabel).padBottom(30).row();

        // Player info
        if (GameManager.getInstance().getCurrentPlayer() != null) {
            com.yusurg.demo.model.PlayerData player = GameManager.getInstance().getCurrentPlayer();
            Label playerLabel = new Label("Dr. " + player.username, skin);
            playerLabel.setFontScale(1.3f);
            playerLabel.setColor(0.3f, 1f, 0.3f, 1f);
            table.add(playerLabel).padBottom(10).row();

            Label levelLabel = new Label("Surgeon Level: " + player.surgeonLevel + " | Total XP: " + player.totalExp, skin);
            levelLabel.setFontScale(1f);
            table.add(levelLabel).padBottom(30).row();
        }

        Label subtitleLabel = new Label("Select Your Patient", skin);
        subtitleLabel.setFontScale(1.5f);
        table.add(subtitleLabel).padBottom(30).row();

        // Easy Button - Dafa
        TextButton easyButton = new TextButton("Easy", skin);
        easyButton.getLabel().setFontScale(1.5f);
        easyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame(DifficultyLevel.EASY);
            }
        });
        table.add(easyButton).width(300).height(80).padBottom(20).row();

        // Medium Button - David
        TextButton mediumButton = new TextButton("Medium", skin);
        mediumButton.getLabel().setFontScale(1.5f);
        mediumButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame(DifficultyLevel.MEDIUM);
            }
        });
        table.add(mediumButton).width(300).height(80).padBottom(20).row();

        // Hard Button - Yusri
        TextButton hardButton = new TextButton("Hard", skin);
        hardButton.getLabel().setFontScale(1.5f);
        hardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame(DifficultyLevel.HARD);
            }
        });
        table.add(hardButton).width(300).height(80).padBottom(20).row();

        // Instructions
        Label instructionsLabel = new Label("Keep your patient alive and cure all conditions!", skin);
        table.add(instructionsLabel).padTop(30).row();

        // Logout button
        TextButton logoutButton = new TextButton("Logout", skin);
        logoutButton.getLabel().setFontScale(1f);
        logoutButton.getLabel().setColor(SkinFactory.DANGER_COLOR);
        logoutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logout();
            }
        });
        table.add(logoutButton).width(200).height(50).padTop(40).row();
    }

    private void logout() {
        // Clear token and player data
        AuthService.getInstance().logout();
        GameManager.getInstance().setCurrentPlayer(null);

        // Go back to login screen
        game.setScreen(new AuthLoginScreen(game));
    }

    private void startGame(DifficultyLevel difficulty) {
        List<Malady> maladies = MaladyFactory.createMaladies(difficulty);
        GameManager.getInstance().startGame(difficulty, maladies);

        // Start backend game session in background
        if (GameManager.getInstance().getCurrentPlayer() != null) {
            new Thread(() -> {
                try {
                    long playerId = GameManager.getInstance().getCurrentPlayer().id;
                    Long sessionId = com.yusurg.demo.api.BackendService.getInstance()
                        .startGameSession(playerId, difficulty);
                    GameManager.getInstance().setCurrentSessionId(sessionId);
                    System.out.println("Game session started: " + sessionId);
                } catch (Exception e) {
                    System.err.println("Failed to start game session: " + e.getMessage());
                }
            }).start();
        }

        game.setScreen(new SurgeryScreen(game));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
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
        stage.dispose();
        skin.dispose();
    }
}

