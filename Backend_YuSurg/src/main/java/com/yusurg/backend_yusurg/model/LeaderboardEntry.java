package com.yusurg.backend_yusurg.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "leaderboard")
public class LeaderboardEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false)
    private int score;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel difficulty;

    @Column(name = "achieved_at")
    private LocalDateTime achievedAt;

    @Column(name = "game_duration_seconds")
    private float gameDurationSeconds;

    public LeaderboardEntry() {
        this.achievedAt = LocalDateTime.now();
    }

    public LeaderboardEntry(Player player, int score, DifficultyLevel difficulty, float gameDurationSeconds) {
        this();
        this.player = player;
        this.score = score;
        this.difficulty = difficulty;
        this.gameDurationSeconds = gameDurationSeconds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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
