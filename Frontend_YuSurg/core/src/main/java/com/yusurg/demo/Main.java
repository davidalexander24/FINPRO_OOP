package com.yusurg.demo;

import com.badlogic.gdx.Game;
import com.yusurg.demo.manager.GameManager;
import com.yusurg.demo.manager.TokenManager;
import com.yusurg.demo.model.PlayerData;
import com.yusurg.demo.view.AuthLoginScreen;
import com.yusurg.demo.view.MainMenuScreen;

public class Main extends Game {

    @Override
    public void create() {
        // Check if user is already logged in
        if (TokenManager.getInstance().isLoggedIn()) {
            // User has valid token, restore session
            String username = TokenManager.getInstance().getUsername();
            long userId = TokenManager.getInstance().getUserId();

            if (username != null && userId != -1) {
                // Create player data from stored info
                PlayerData player = new PlayerData();
                player.id = userId;
                player.username = username;
                player.surgeonLevel = 1; // Will be updated from backend
                player.totalExp = 0;
                player.totalGamesPlayed = 0;

                GameManager.getInstance().setCurrentPlayer(player);

                System.out.println("Auto-login successful for user: " + username);
                setScreen(new MainMenuScreen(this));
                return;
            }
        }

        // No valid token, show login screen
        setScreen(new AuthLoginScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
