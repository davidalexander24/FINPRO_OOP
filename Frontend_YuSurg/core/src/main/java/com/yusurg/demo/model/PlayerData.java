package com.yusurg.demo.model;

import com.google.gson.annotations.SerializedName;

public class PlayerData {
    public long id;

    public String username;

    @SerializedName(value = "surgeonLevel", alternate = {"surgeon_level", "level"})
    public int surgeonLevel;

    @SerializedName(value = "totalExp", alternate = {"totalXp", "total_exp", "total_xp", "xp", "experience"})
    public int totalExp;

    @SerializedName(value = "totalGamesPlayed", alternate = {"total_games_played", "gamesPlayed", "games_played"})
    public int totalGamesPlayed;

    @SerializedName(value = "createdAt", alternate = {"created_at"})
    public String createdAt;

    @SerializedName(value = "lastLogin", alternate = {"last_login"})
    public String lastLogin;

    public PlayerData() {
    }

    public PlayerData(long id, String username, int surgeonLevel, int totalExp, int totalGamesPlayed) {
        this.id = id;
        this.username = username;
        this.surgeonLevel = surgeonLevel;
        this.totalExp = totalExp;
        this.totalGamesPlayed = totalGamesPlayed;
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", surgeonLevel=" + surgeonLevel +
                ", totalExp=" + totalExp +
                ", totalGamesPlayed=" + totalGamesPlayed +
                '}';
    }
}

