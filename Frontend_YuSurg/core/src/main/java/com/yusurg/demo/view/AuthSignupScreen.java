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

public class AuthSignupScreen implements Screen {
    private Game game;
    private Stage stage;
    private Skin skin;

    private TextField usernameField;
    private TextField emailField;
    private TextField passwordField;
    private TextField confirmPasswordField;
    private Label messageLabel;
    private TextButton signupButton;
    private TextButton loginLinkButton;
    private boolean isLoading = false;

    public AuthSignupScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = SkinFactory.createDefaultSkin();

        // Create scrollable content for smaller screens
        Table contentTable = new Table();

        // Title
        Label titleLabel = new Label("YuSurg - Surgery Simulator", skin);
        titleLabel.setFontScale(2.2f);
        contentTable.add(titleLabel).padBottom(15).row();

        Label subtitleLabel = new Label("Begin Your Medical Journey!", skin);
        subtitleLabel.setFontScale(1.3f);
        contentTable.add(subtitleLabel).padBottom(30).row();

        // Signup form title
        Label signupTitle = new Label("CREATE ACCOUNT", skin);
        signupTitle.setFontScale(1.6f);
        signupTitle.setColor(SkinFactory.ACCENT_COLOR);
        contentTable.add(signupTitle).padBottom(25).row();

        // Username label and field
        Label usernameLabel = new Label("Username:", skin);
        usernameLabel.setFontScale(1.1f);
        contentTable.add(usernameLabel).left().padBottom(5).row();

        usernameField = new TextField("", skin);
        usernameField.setMessageText("Choose a username (3-50 characters)");
        contentTable.add(usernameField).width(400).height(50).padBottom(12).row();

        // Email label and field
        Label emailLabel = new Label("Email:", skin);
        emailLabel.setFontScale(1.1f);
        contentTable.add(emailLabel).left().padBottom(5).row();

        emailField = new TextField("", skin);
        emailField.setMessageText("Enter your email address");
        contentTable.add(emailField).width(400).height(50).padBottom(12).row();

        // Password label and field
        Label passwordLabel = new Label("Password:", skin);
        passwordLabel.setFontScale(1.1f);
        contentTable.add(passwordLabel).left().padBottom(5).row();

        passwordField = new TextField("", skin);
        passwordField.setMessageText("Create a password (min 6 characters)");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        contentTable.add(passwordField).width(400).height(50).padBottom(12).row();

        // Confirm Password label and field
        Label confirmPasswordLabel = new Label("Confirm Password:", skin);
        confirmPasswordLabel.setFontScale(1.1f);
        contentTable.add(confirmPasswordLabel).left().padBottom(5).row();

        confirmPasswordField = new TextField("", skin);
        confirmPasswordField.setMessageText("Confirm your password");
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');
        contentTable.add(confirmPasswordField).width(400).height(50).padBottom(20).row();

        // Signup button
        signupButton = new TextButton("SIGN UP", skin);
        signupButton.getLabel().setFontScale(1.3f);
        signupButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isLoading) {
                    attemptSignup();
                }
            }
        });
        contentTable.add(signupButton).width(400).height(60).padBottom(15).row();

        // Message label (for feedback/errors)
        messageLabel = new Label("", skin);
        messageLabel.setFontScale(1f);
        messageLabel.setWrap(true);
        contentTable.add(messageLabel).width(400).padBottom(15).row();

        // Login link
        loginLinkButton = new TextButton("Already have an account? Login", skin);
        loginLinkButton.getLabel().setFontScale(1f);
        loginLinkButton.getLabel().setColor(SkinFactory.ACCENT_COLOR);
        loginLinkButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isLoading) {
                    game.setScreen(new AuthLoginScreen(game));
                }
            }
        });
        contentTable.add(loginLinkButton).padTop(5).row();

        // Add content to scrollable pane
        ScrollPane scrollPane = new ScrollPane(contentTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        // Main table
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.add(scrollPane).expand().fill().pad(20);
        stage.addActor(mainTable);
    }

    private void attemptSignup() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate input
        if (username.isEmpty()) {
            showError("Please enter a username");
            return;
        }

        if (username.length() < 3) {
            showError("Username must be at least 3 characters");
            return;
        }

        if (username.length() > 50) {
            showError("Username must be less than 50 characters");
            return;
        }

        if (email.isEmpty()) {
            showError("Please enter your email");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Please enter a valid email address");
            return;
        }

        if (password.isEmpty()) {
            showError("Please enter a password");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        // Disable UI during signup
        setLoading(true);
        showMessage("Creating account...");

        // Signup with backend in background thread
        new Thread(() -> {
            try {
                AuthResponse authResponse = AuthService.getInstance().sendSignupRequest(username, email, password);

                // Fetch full player data from backend
                PlayerData player;
                try {
                    player = com.yusurg.demo.api.BackendService.getInstance().getPlayerData(authResponse.userId);
                    // Preserve the username the user typed
                    player.username = username;
                    System.out.println("[SIGNUP] Fetched player data - XP: " + player.totalExp + ", Level: " + player.surgeonLevel);
                } catch (Exception e) {
                    System.err.println("[SIGNUP] Failed to fetch player data, using defaults: " + e.getMessage());
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
                    GameManager.getInstance().setCurrentPlayer(finalPlayer);
                    showSuccess("Account created! Welcome, Dr. " + username + "!");

                    // Proceed to main menu after short delay
                    new Thread(() -> {
                        try {
                            Thread.sleep(1500);
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
                    System.err.println("Signup error: " + e.getMessage());
                });
            }
        }).start();
    }

    private boolean isValidEmail(String email) {
        // Simple email validation
        return email.contains("@") && email.contains(".") && email.indexOf("@") < email.lastIndexOf(".");
    }

    private void setLoading(boolean loading) {
        isLoading = loading;
        signupButton.setDisabled(loading);
        loginLinkButton.setDisabled(loading);
        usernameField.setDisabled(loading);
        emailField.setDisabled(loading);
        passwordField.setDisabled(loading);
        confirmPasswordField.setDisabled(loading);
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

