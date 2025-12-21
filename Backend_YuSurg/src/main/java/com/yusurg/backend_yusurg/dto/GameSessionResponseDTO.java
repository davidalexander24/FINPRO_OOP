package com.yusurg.backend_yusurg.dto;

import com.yusurg.backend_yusurg.model.DifficultyLevel;
import com.yusurg.backend_yusurg.model.GameOutcome;
import com.yusurg.backend_yusurg.model.GameSession;

import java.time.LocalDateTime;

public class GameSessionResponseDTO {

    private Long id;
    private Long playerId;
    private String playerUsername;
    private DifficultyLevel difficulty;
    private int score;
    private GameOutcome outcome;
    private float gameDurationSeconds;
    private String maladiesTreated;
    private int toolsUsedCount;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    public GameSessionResponseDTO() {}

    public GameSessionResponseDTO(GameSession session) {
        this.id = session.getId();
        this.playerId = session.getPlayer().getId();
        this.playerUsername = session.getPlayer().getUsername();
        this.difficulty = session.getDifficulty();
        this.score = session.getScore();
        this.outcome = session.getOutcome();
        this.gameDurationSeconds = session.getGameDurationSeconds();
        this.maladiesTreated = session.getMaladiesTreated();
        this.toolsUsedCount = session.getToolsUsedCount();
        this.startedAt = session.getStartedAt();
        this.endedAt = session.getEndedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }

    public void setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public GameOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(GameOutcome outcome) {
        this.outcome = outcome;
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

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }
}
