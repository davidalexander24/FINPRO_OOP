package com.yusurg.backend_yusurg.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private Long playerId;
    private String username;
    private String email;
    private String message;

    public AuthResponse(String token, Long userId, Long playerId, String username, String email) {
        this.token = token;
        this.userId = userId;
        this.playerId = playerId;
        this.username = username;
        this.email = email;
        this.message = "Authentication successful";
    }
}
