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
import com.yusurg.demo.manager.GameManager;
import com.yusurg.demo.model.AuthResponse;
import com.yusurg.demo.model.PlayerData;

public class AuthLoginScreen implements Screen {
    private Game game;
    private Stage stage;
    private Skin skin;

    private TextField usernameField;
    private TextField passwordField;
    private Label messageLabel;
    private TextButton loginButton;
    private TextButton signupLinkButton;
    private boolean isLoading = false;

    public AuthLoginScreen(Game game) {
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
        table.add(titleLabel).padBottom(20).row();

        Label subtitleLabel = new Label("Welcome Back, Doctor!", skin);
        subtitleLabel.setFontScale(1.5f);
        table.add(subtitleLabel).padBottom(40).row();

        // Login form title
        Label loginTitle = new Label("LOGIN", skin);
        loginTitle.setFontScale(1.8f);
        loginTitle.setColor(SkinFactory.ACCENT_COLOR);
        table.add(loginTitle).padBottom(30).row();

        // Username label and field
        Label usernameLabel = new Label("Username:", skin);
        usernameLabel.setFontScale(1.1f);
        table.add(usernameLabel).left().padBottom(5).row();

        usernameField = new TextField("", skin);
        usernameField.setMessageText("Enter your username");
        table.add(usernameField).width(400).height(50).padBottom(15).row();

        // Password label and field
        Label passwordLabel = new Label("Password:", skin);
        passwordLabel.setFontScale(1.1f);
        table.add(passwordLabel).left().padBottom(5).row();

        passwordField = new TextField("", skin);
        passwordField.setMessageText("Enter your password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        table.add(passwordField).width(400).height(50).padBottom(25).row();

        // Login button
        loginButton = new TextButton("LOGIN", skin);
        loginButton.getLabel().setFontScale(1.3f);
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isLoading) {
                    attemptLogin();
                }
            }
        });
        table.add(loginButton).width(400).height(60).padBottom(20).row();

        // Message label (for feedback/errors)
        messageLabel = new Label("", skin);
        messageLabel.setFontScale(1f);
        messageLabel.setWrap(true);
        table.add(messageLabel).width(400).padBottom(20).row();

        // Signup link
        signupLinkButton = new TextButton("Don't have an account? Sign Up", skin);
        signupLinkButton.getLabel().setFontScale(1f);
        signupLinkButton.getLabel().setColor(SkinFactory.ACCENT_COLOR);
        signupLinkButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isLoading) {
                    game.setScreen(new AuthSignupScreen(game));
                }
            }
        });
        table.add(signupLinkButton).padTop(10).row();
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Validate input
        if (username.isEmpty()) {
            showError("Please enter your username");
            return;
        }

        if (password.isEmpty()) {
            showError("Please enter your password");
            return;
        }

        if (username.length() < 3) {
            showError("Username must be at least 3 characters");
            return;
        }

        // Disable UI during login
        setLoading(true);
        showMessage("Logging in...");

        // Login with backend in background thread
        new Thread(() -> {
            try {
                AuthResponse authResponse = AuthService.getInstance().sendLoginRequest(username, password);

                System.out.println("[LOGIN] Auth response - userId: " + authResponse.userId + ", username: " + authResponse.username);

                // Fetch full player data from backend
                PlayerData player;
                try {
                    player = com.yusurg.demo.api.BackendService.getInstance().getPlayerData(authResponse.userId);
                    // Preserve the username the user typed
                    player.username = username;
                    System.out.println("[LOGIN] Fetched player data - XP: " + player.totalExp + ", Level: " + player.surgeonLevel);
                } catch (Exception e) {
                    System.err.println("[LOGIN] Failed to fetch player data, using defaults: " + e.getMessage());
                    // Fallback to default values if fetch fails
                    player = new PlayerData();
                    player.id = authResponse.userId;
                    player.username = username;
                    player.surgeonLevel = 1;
                    player.totalExp = 0;
                    player.totalGamesPlayed = 0;
                }

                final PlayerData finalPlayer = player;

                // Success - update UI on main thread
                Gdx.app.postRunnable(() -> {
                    System.out.println("[LOGIN] Setting current player with id: " + finalPlayer.id + ", XP: " + finalPlayer.totalExp);

                    GameManager.getInstance().setCurrentPlayer(finalPlayer);
                    showSuccess("Welcome back, Dr. " + username + "!");

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

            } catch (AuthService.AuthException e) {
                // Error - update UI on main thread
                Gdx.app.postRunnable(() -> {
                    setLoading(false);
                    showError(e.getMessage());
                    System.err.println("Login error: " + e.getMessage());
                });
            }
        }).start();
    }

    private void setLoading(boolean loading) {
        isLoading = loading;
        loginButton.setDisabled(loading);
        signupLinkButton.setDisabled(loading);
        usernameField.setDisabled(loading);
        passwordField.setDisabled(loading);
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setColor(1, 1, 1, 1);
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setColor(SkinFactory.DANGER_COLOR);
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setColor(SkinFactory.SUCCESS_COLOR);
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

