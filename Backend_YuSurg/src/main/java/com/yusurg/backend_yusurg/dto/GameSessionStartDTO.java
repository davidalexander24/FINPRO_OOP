package com.yusurg.backend_yusurg.dto;

import com.yusurg.backend_yusurg.model.DifficultyLevel;
import jakarta.validation.constraints.NotNull;

public class GameSessionStartDTO {

    private Long playerId;

    @NotNull(message = "Difficulty level is required")
    private DifficultyLevel difficulty;

    public GameSessionStartDTO() {}

    public GameSessionStartDTO(Long playerId, DifficultyLevel difficulty) {
        this.playerId = playerId;
        this.difficulty = difficulty;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }
}
