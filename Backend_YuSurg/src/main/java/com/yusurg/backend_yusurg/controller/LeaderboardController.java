package com.yusurg.backend_yusurg.controller;

import com.yusurg.backend_yusurg.dto.LeaderboardEntryDTO;
import com.yusurg.backend_yusurg.model.DifficultyLevel;
import com.yusurg.backend_yusurg.security.SecurityUtils;
import com.yusurg.backend_yusurg.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping("/me")
    public ResponseEntity<List<LeaderboardEntryDTO>> getMyLeaderboardEntries() {
        Long playerId = SecurityUtils.getCurrentPlayerId();
        List<LeaderboardEntryDTO> entries = leaderboardService.getPlayerEntries(playerId);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(entries);
    }

    @GetMapping
    public ResponseEntity<List<LeaderboardEntryDTO>> getTopScores(
            @RequestParam(defaultValue = "10") int limit) {
        List<LeaderboardEntryDTO> entries = leaderboardService.getTopScores(limit);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<LeaderboardEntryDTO>> getTopScoresByDifficulty(
            @PathVariable DifficultyLevel difficulty,
            @RequestParam(defaultValue = "10") int limit) {
        List<LeaderboardEntryDTO> entries = leaderboardService.getTopScoresByDifficulty(difficulty, limit);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(entries);
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<LeaderboardEntryDTO>> getPlayerEntries(@PathVariable Long playerId) {
        List<LeaderboardEntryDTO> entries = leaderboardService.getPlayerEntries(playerId);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(entries);
    }

    @GetMapping("/rank")
    public ResponseEntity<Integer> getRankForScore(@RequestParam int score) {
        int rank = leaderboardService.getPlayerRank(score);
        return ResponseEntity.ok(rank);
    }
}
