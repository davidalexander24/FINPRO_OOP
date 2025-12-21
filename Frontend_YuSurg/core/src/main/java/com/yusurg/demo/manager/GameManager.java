package com.yusurg.demo.manager;

import com.yusurg.demo.model.DifficultyLevel;
import com.yusurg.demo.model.Malady;
import com.yusurg.demo.model.PlayerData;

import java.util.List;

public class GameManager {
    private static GameManager instance;

    private PlayerData currentPlayer;
    private DifficultyLevel currentDifficulty;
    private int score;
    private boolean gameWon;
    private boolean gameLost;
    private List<Malady> currentMaladies;
    private float gameTime;
    private Long currentSessionId;

    private GameManager() {
        reset();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void reset() {
        score = 0;
        gameWon = false;
        gameLost = false;
        gameTime = 0;
        currentMaladies = null;
    }

    public void startGame(DifficultyLevel difficulty, List<Malady> maladies) {
        reset();
        this.currentDifficulty = difficulty;
        this.currentMaladies = maladies;
    }

    public void updateGameTime(float delta) {
        if (!gameWon && !gameLost) {
            gameTime += delta;
        }
    }

    public void checkWinCondition() {
        if (currentMaladies == null || currentMaladies.isEmpty()) {
            return;
        }

        boolean allCured = true;
        for (Malady malady : currentMaladies) {
            if (!malady.isCured()) {
                allCured = false;
                break;
            }
        }

        if (allCured) {
            gameWon = true;
            calculateScore();
        }
    }

    public void setGameLost() {
        gameLost = true;
    }

    private void calculateScore() {
        // XP based on difficulty only
        switch (currentDifficulty) {
            case EASY:
                score = 100;
                break;
            case MEDIUM:
                score = 200;
                break;
            case HARD:
                score = 300;
                break;
            default:
                score = 100;
        }
    }

    // Getters and Setters
    public DifficultyLevel getCurrentDifficulty() {
        return currentDifficulty;
    }

    public int getScore() {
        return score;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public boolean isGameLost() {
        return gameLost;
    }

    public List<Malady> getCurrentMaladies() {
        return currentMaladies;
    }

    public float getGameTime() {
        return gameTime;
    }

    public PlayerData getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerData player) {
        this.currentPlayer = player;
    }

    public Long getCurrentSessionId() {
        return currentSessionId;
    }

    public void setCurrentSessionId(Long sessionId) {
        this.currentSessionId = sessionId;
    }
}

