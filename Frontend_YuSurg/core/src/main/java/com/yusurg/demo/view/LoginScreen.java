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
import com.yusurg.demo.api.BackendService;
import com.yusurg.demo.manager.GameManager;
import com.yusurg.demo.model.PlayerData;

public class LoginScreen implements Screen {
    private Game game;
    private Stage stage;
    private Skin skin;
    private TextField usernameField;
    private Label messageLabel;
    private TextButton loginButton;

    public LoginScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = SkinFactory.createDefaultSkin();

        // Create UI
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Title
        Label titleLabel = new Label("YuSurg - Surgery Simulator", skin);
        titleLabel.setFontScale(2.5f);
        table.add(titleLabel).padBottom(30).row();

        Label subtitleLabel = new Label("Welcome, Doctor!", skin);
        subtitleLabel.setFontScale(1.5f);
        table.add(subtitleLabel).padBottom(50).row();

        // Username label
        Label usernameLabel = new Label("Enter Your Username:", skin);
        usernameLabel.setFontScale(1.2f);
        table.add(usernameLabel).padBottom(10).row();

        // Username text field
        usernameField = new TextField("", skin);
        usernameField.setMessageText("Username (3-50 characters)");
        table.add(usernameField).width(400).height(60).padBottom(20).row();

        // Login button
        loginButton = new TextButton("LOGIN / REGISTER", skin);
        loginButton.getLabel().setFontScale(1.3f);
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                attemptLogin();
            }
        });
        table.add(loginButton).width(400).height(70).padBottom(20).row();

        // Message label (for feedback)
        messageLabel = new Label("", skin);
        messageLabel.setFontScale(1f);
        table.add(messageLabel).padTop(20).row();

        // Instructions
        Label instructionsLabel = new Label("Your progress will be saved automatically", skin);
        instructionsLabel.setFontScale(0.9f);
        table.add(instructionsLabel).padTop(30).row();
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();

        // Validate username
        if (username.isEmpty()) {
            messageLabel.setText("Please enter a username");
            messageLabel.setColor(1, 0.3f, 0.3f, 1);
            return;
        }

        if (username.length() < 3) {
            messageLabel.setText("Username must be at least 3 characters");
            messageLabel.setColor(1, 0.3f, 0.3f, 1);
            return;
        }

        if (username.length() > 50) {
            messageLabel.setText("Username must be less than 50 characters");
            messageLabel.setColor(1, 0.3f, 0.3f, 1);
            return;
        }

        // Disable button during login
        loginButton.setDisabled(true);
        messageLabel.setText("Connecting to server...");
        messageLabel.setColor(1, 1, 1, 1);

        // Login/Register with backend
        new Thread(() -> {
            try {
                PlayerData player = BackendService.getInstance().loginOrRegister(username);

                // Success - update UI on main thread
                Gdx.app.postRunnable(() -> {
                    GameManager.getInstance().setCurrentPlayer(player);
                    messageLabel.setText("Welcome, " + player.username + "!");
                    messageLabel.setColor(0.3f, 1, 0.3f, 1);

                    // Proceed to main menu after short delay
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                            Gdx.app.postRunnable(() -> {
                                game.setScreen(new MainMenuScreen(game));
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                });

            } catch (Exception e) {
                // Error - update UI on main thread
                Gdx.app.postRunnable(() -> {
                    loginButton.setDisabled(false);
                    messageLabel.setText("Connection failed: " + e.getMessage());
                    messageLabel.setColor(1, 0.3f, 0.3f, 1);
                    System.err.println("Login error: " + e.getMessage());
                    e.printStackTrace();
                });
            }
        }).start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
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
        if (stage != null) {
            stage.dispose();
        }
        if (skin != null) {
            skin.dispose();
        }
    }
}

