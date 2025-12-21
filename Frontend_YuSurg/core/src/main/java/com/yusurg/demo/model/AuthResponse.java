package com.yusurg.demo.model;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    public String token;

    @SerializedName(value = "userId", alternate = {"user_id", "id", "playerId", "player_id"})
    public long userId;

    public String username;

    public AuthResponse() {
    }

    public AuthResponse(String token, long userId, String username) {
        this.token = token;
        this.userId = userId;
        this.username = username;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "token='" + (token != null ? token.substring(0, Math.min(20, token.length())) + "..." : "null") + '\'' +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}

