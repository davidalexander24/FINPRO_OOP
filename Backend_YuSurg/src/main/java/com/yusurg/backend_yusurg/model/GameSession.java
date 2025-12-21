package com.yusurg.backend_yusurg.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_sessions")
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel difficulty;

    @Column(nullable = false)
    private int score = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameOutcome outcome = GameOutcome.IN_PROGRESS;

    @Column(name = "game_duration_seconds")
    private float gameDurationSeconds;

    @Column(name = "maladies_treated")
    private String maladiesTreated;

    @Column(name = "tools_used_count")
    private int toolsUsedCount = 0;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    public GameSession() {
        this.startedAt = LocalDateTime.now();
    }

    public GameSession(Player player, DifficultyLevel difficulty) {
        this();
        this.player = player;
        this.difficulty = difficulty;
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

    public void endGame(GameOutcome outcome, int score) {
        this.endedAt = LocalDateTime.now();
        this.outcome = outcome;
        this.score = score;
    }
}
