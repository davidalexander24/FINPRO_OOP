package com.yusurg.backend_yusurg.dto;

import com.yusurg.backend_yusurg.model.DifficultyLevel;
import com.yusurg.backend_yusurg.model.LeaderboardEntry;

import java.time.LocalDateTime;

public class LeaderboardEntryDTO {

    private Long id;
    private int rank;
    private String playerUsername;
    private int score;
    private DifficultyLevel difficulty;
    private LocalDateTime achievedAt;
    private float gameDurationSeconds;

    public LeaderboardEntryDTO() {}

    public LeaderboardEntryDTO(LeaderboardEntry entry, int rank) {
        this.id = entry.getId();
        this.rank = rank;
        this.playerUsername = entry.getPlayer().getUsername();
        this.score = entry.getScore();
        this.difficulty = entry.getDifficulty();
        this.achievedAt = entry.getAchievedAt();
        this.gameDurationSeconds = entry.getGameDurationSeconds();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }

    public void setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }

    public LocalDateTime getAchievedAt() {
        return achievedAt;
    }

    public void setAchievedAt(LocalDateTime achievedAt) {
        this.achievedAt = achievedAt;
    }

    public float getGameDurationSeconds() {
        return gameDurationSeconds;
    }

    public void setGameDurationSeconds(float gameDurationSeconds) {
        this.gameDurationSeconds = gameDurationSeconds;
    }
}
