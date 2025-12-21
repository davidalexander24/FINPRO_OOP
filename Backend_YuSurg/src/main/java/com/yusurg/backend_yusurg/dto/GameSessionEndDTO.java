package com.yusurg.backend_yusurg.dto;

import com.yusurg.backend_yusurg.model.GameOutcome;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class GameSessionEndDTO {

    @NotNull(message = "Game session ID is required")
    private Long sessionId;

    @NotNull(message = "Game outcome is required")
    private GameOutcome outcome;

    @Min(value = 0, message = "Score cannot be negative")
    private int score;

    private float gameDurationSeconds;

    private String maladiesTreated;

    private int toolsUsedCount;

    public GameSessionEndDTO() {}

    public GameSessionEndDTO(Long sessionId, GameOutcome outcome, int score, float gameDurationSeconds) {
        this.sessionId = sessionId;
        this.outcome = outcome;
        this.score = score;
        this.gameDurationSeconds = gameDurationSeconds;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public GameOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(GameOutcome outcome) {
        this.outcome = outcome;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getGameDurationSeconds() {
        return gameDurationSeconds;
    }

    public void setGameDurationSeconds(float gameDurationSeconds) {
        this.gameDurationSeconds = gameDurationSeconds;
    }

    public String getMaladiesTreated() {
        return maladiesTreated;
    }

    public void setMaladiesTreated(String maladiesTreated) {
        this.maladiesTreated = maladiesTreated;
    }

    public int getToolsUsedCount() {
        return toolsUsedCount;
    }

    public void setToolsUsedCount(int toolsUsedCount) {
        this.toolsUsedCount = toolsUsedCount;
    }
}
