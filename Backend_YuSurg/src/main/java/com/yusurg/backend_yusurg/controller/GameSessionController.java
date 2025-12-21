package com.yusurg.backend_yusurg.controller;

import com.yusurg.backend_yusurg.dto.GameSessionEndDTO;
import com.yusurg.backend_yusurg.dto.GameSessionResponseDTO;
import com.yusurg.backend_yusurg.dto.GameSessionStartDTO;
import com.yusurg.backend_yusurg.security.SecurityUtils;
import com.yusurg.backend_yusurg.service.GameSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GameSessionController {

    private final GameSessionService gameSessionService;

    @PostMapping("/start")
    public ResponseEntity<GameSessionResponseDTO> startGame(@Valid @RequestBody GameSessionStartDTO startDTO) {
        Long authenticatedPlayerId = SecurityUtils.getCurrentPlayerId();
        startDTO.setPlayerId(authenticatedPlayerId);

        GameSessionResponseDTO session = gameSessionService.startGame(startDTO);
        return new ResponseEntity<>(session, HttpStatus.CREATED);
    }

    @PostMapping("/end")
    public ResponseEntity<GameSessionResponseDTO> endGame(@Valid @RequestBody GameSessionEndDTO endDTO) {
        GameSessionResponseDTO session = gameSessionService.endGame(endDTO);
        return ResponseEntity.ok(session);
    }

    @GetMapping("/my-sessions")
    public ResponseEntity<List<GameSessionResponseDTO>> getMyGameSessions() {
        Long playerId = SecurityUtils.getCurrentPlayerId();
        List<GameSessionResponseDTO> sessions = gameSessionService.getPlayerSessions(playerId);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(sessions);
    }

    @GetMapping("/my-count")
    public ResponseEntity<Long> getMyGameCount() {
        Long playerId = SecurityUtils.getCurrentPlayerId();
        long count = gameSessionService.getPlayerGameCount(playerId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/my-top")
    public ResponseEntity<List<GameSessionResponseDTO>> getMyTopScores(
            @RequestParam(defaultValue = "10") int limit) {
        Long playerId = SecurityUtils.getCurrentPlayerId();
        List<GameSessionResponseDTO> sessions = gameSessionService.getPlayerTopScores(playerId, limit);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameSessionResponseDTO> getSessionById(@PathVariable Long id) {
        GameSessionResponseDTO session = gameSessionService.getSessionById(id);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(session);
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<GameSessionResponseDTO>> getPlayerSessions(@PathVariable Long playerId) {
        List<GameSessionResponseDTO> sessions = gameSessionService.getPlayerSessions(playerId);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(sessions);
    }

    @GetMapping("/player/{playerId}/top")
    public ResponseEntity<List<GameSessionResponseDTO>> getPlayerTopScores(
            @PathVariable Long playerId,
            @RequestParam(defaultValue = "10") int limit) {
        List<GameSessionResponseDTO> sessions = gameSessionService.getPlayerTopScores(playerId, limit);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(sessions);
    }

    @GetMapping("/top")
    public ResponseEntity<List<GameSessionResponseDTO>> getTopScores(
            @RequestParam(defaultValue = "10") int limit) {
        List<GameSessionResponseDTO> sessions = gameSessionService.getTopScores(limit);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(sessions);
    }

    @GetMapping("/player/{playerId}/count")
    public ResponseEntity<Long> getPlayerGameCount(@PathVariable Long playerId) {
        long count = gameSessionService.getPlayerGameCount(playerId);
        return ResponseEntity.ok(count);
    }
}
