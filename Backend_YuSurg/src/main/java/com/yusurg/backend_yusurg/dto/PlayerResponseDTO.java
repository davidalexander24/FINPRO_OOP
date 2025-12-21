package com.yusurg.backend_yusurg.dto;

import com.yusurg.backend_yusurg.model.Player;

import java.time.LocalDateTime;

public class PlayerResponseDTO {
    private Long id;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private int surgeonLevel;
    private int totalExp;

    public PlayerResponseDTO() {}

    public PlayerResponseDTO(Player player) {
        this.id = player.getId();
        this.username = player.getUsername();
        this.createdAt = player.getCreatedAt();
        this.lastLogin = player.getLastLogin();
        this.surgeonLevel = player.getSurgeonLevel();
        this.totalExp = player.getTotalExp();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getSurgeonLevel() {
        return surgeonLevel;
    }

    public void setSurgeonLevel(int surgeonLevel) {
        this.surgeonLevel = surgeonLevel;
    }

    public int getTotalExp() {
        return totalExp;
    }

    public void setTotalExp(int totalExp) {
        this.totalExp = totalExp;
    }
}
