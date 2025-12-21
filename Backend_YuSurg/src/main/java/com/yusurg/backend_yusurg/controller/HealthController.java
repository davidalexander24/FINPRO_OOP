package com.yusurg.backend_yusurg.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "YuSurg Backend API");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to YuSurg Backend API");
        response.put("documentation", "/api/docs");
        response.put("endpoints", new String[]{
            "GET /api/health - Health check",
            "POST /api/players - Create player",
            "POST /api/players/login - Login/register player",
            "GET /api/players - Get all players",
            "GET /api/players/{id} - Get player by ID",
            "POST /api/games/start - Start a game session",
            "POST /api/games/end - End a game session",
            "GET /api/games/player/{playerId} - Get player's game history",
            "GET /api/leaderboard - Get top scores",
            "GET /api/leaderboard/difficulty/{difficulty} - Get top scores by difficulty"
        });
        return ResponseEntity.ok(response);
    }
}
