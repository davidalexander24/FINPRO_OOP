package com.yusurg.backend_yusurg.controller;

import com.yusurg.backend_yusurg.dto.PlayerDTO;
import com.yusurg.backend_yusurg.dto.PlayerResponseDTO;
import com.yusurg.backend_yusurg.security.SecurityUtils;
import com.yusurg.backend_yusurg.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/me")
    public ResponseEntity<PlayerResponseDTO> getCurrentPlayer() {
        Long playerId = SecurityUtils.getCurrentPlayerId();
        PlayerResponseDTO player = playerService.getPlayerById(playerId);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(player);
    }

    @PostMapping("/me/addxp")
    public ResponseEntity<PlayerResponseDTO> addMyXP(@RequestParam int xp) {
        Long playerId = SecurityUtils.getCurrentPlayerId();
        PlayerResponseDTO player = playerService.updatePlayerStats(playerId, xp);
        return ResponseEntity.ok(player);
    }

    @PostMapping
    public ResponseEntity<PlayerResponseDTO> createPlayer(@Valid @RequestBody PlayerDTO playerDTO) {
        PlayerResponseDTO createdPlayer = playerService.createPlayer(playerDTO);
        return new ResponseEntity<>(createdPlayer, HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<PlayerResponseDTO> registerPlayer(@Valid @RequestBody PlayerDTO playerDTO) {
        PlayerResponseDTO player = playerService.getOrCreatePlayer(playerDTO.getUsername());
        return ResponseEntity.ok(player);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponseDTO> getPlayerById(@PathVariable Long id) {
        PlayerResponseDTO player = playerService.getPlayerById(id);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(player);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<PlayerResponseDTO> getPlayerByUsername(@PathVariable String username) {
        PlayerResponseDTO player = playerService.getPlayerByUsername(username);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(player);
    }

    @GetMapping
    public ResponseEntity<List<PlayerResponseDTO>> getAllPlayers() {
        List<PlayerResponseDTO> players = playerService.getAllPlayers();
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(players);
    }

    @GetMapping("/top/score")
    public ResponseEntity<List<PlayerResponseDTO>> getTopPlayersByScore(
            @RequestParam(defaultValue = "10") int limit) {
        List<PlayerResponseDTO> players = playerService.getTopPlayersByScore(limit);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(players);
    }

    @GetMapping("/top/level")
    public ResponseEntity<List<PlayerResponseDTO>> getTopPlayersByLevel(
            @RequestParam(defaultValue = "10") int limit) {
        List<PlayerResponseDTO> players = playerService.getTopPlayersByLevel(limit);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(players);
    }

    @PostMapping("/{id}/addxp")
    public ResponseEntity<PlayerResponseDTO> addPlayerXP(
            @PathVariable Long id,
            @RequestParam int xp) {
        PlayerResponseDTO player = playerService.updatePlayerStats(id, xp);
        return ResponseEntity.ok(player);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }
}
