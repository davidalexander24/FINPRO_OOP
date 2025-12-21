package com.yusurg.backend_yusurg.service;

import com.yusurg.backend_yusurg.dto.GameSessionEndDTO;
import com.yusurg.backend_yusurg.dto.GameSessionResponseDTO;
import com.yusurg.backend_yusurg.dto.GameSessionStartDTO;
import com.yusurg.backend_yusurg.exception.GameSessionNotFoundException;
import com.yusurg.backend_yusurg.model.GameOutcome;
import com.yusurg.backend_yusurg.model.GameSession;
import com.yusurg.backend_yusurg.model.Player;
import com.yusurg.backend_yusurg.repository.GameSessionRepository;
import com.yusurg.backend_yusurg.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GameSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final PlayerService playerService;
    private final LeaderboardService leaderboardService;
    private final PlayerRepository playerRepository;

    public GameSessionService(GameSessionRepository gameSessionRepository,
                              PlayerService playerService,
                              LeaderboardService leaderboardService,
                              PlayerRepository playerRepository) {
        this.gameSessionRepository = gameSessionRepository;
        this.playerService = playerService;
        this.leaderboardService = leaderboardService;
        this.playerRepository = playerRepository;
    }

    public GameSessionResponseDTO startGame(GameSessionStartDTO startDTO) {
        Player player = playerService.getPlayerEntityById(startDTO.getPlayerId());

        GameSession session = new GameSession(player, startDTO.getDifficulty());
        GameSession savedSession = gameSessionRepository.save(session);

        return new GameSessionResponseDTO(savedSession);
    }

    public GameSessionResponseDTO endGame(GameSessionEndDTO endDTO) {
        GameSession session = gameSessionRepository.findById(endDTO.getSessionId())
            .orElseThrow(() -> new GameSessionNotFoundException("Game session not found with ID: " + endDTO.getSessionId()));

        session.endGame(endDTO.getOutcome(), endDTO.getScore());
        session.setGameDurationSeconds(endDTO.getGameDurationSeconds());
        session.setMaladiesTreated(endDTO.getMaladiesTreated());
        session.setToolsUsedCount(endDTO.getToolsUsedCount());

        Player player = session.getPlayer();

        if (endDTO.getOutcome() == GameOutcome.WIN) {
            int xpGained = calculateXPForDifficulty(session.getDifficulty());

            playerService.updatePlayerStats(player, xpGained);

            player = playerService.getPlayerEntityById(player.getId());

            leaderboardService.addEntry(player, endDTO.getScore(),
                    session.getDifficulty(), endDTO.getGameDurationSeconds());
        }

        GameSession savedSession = gameSessionRepository.save(session);
        return new GameSessionResponseDTO(savedSession);
    }

    public GameSessionResponseDTO getSessionById(Long id) {
        GameSession session = gameSessionRepository.findById(id)
            .orElseThrow(() -> new GameSessionNotFoundException("Game session not found with ID: " + id));
        return new GameSessionResponseDTO(session);
    }

    public List<GameSessionResponseDTO> getAllSessions() {
        return gameSessionRepository.findAll().stream()
            .map(GameSessionResponseDTO::new)
            .collect(Collectors.toList());
    }

    public List<GameSessionResponseDTO> getPlayerSessions(Long playerId) {
        return gameSessionRepository.findByPlayerId(playerId).stream()
            .map(GameSessionResponseDTO::new)
            .collect(Collectors.toList());
    }

    public List<GameSessionResponseDTO> getTopScoresByDifficulty(
            com.yusurg.backend_yusurg.model.DifficultyLevel difficulty, int limit) {
        return gameSessionRepository.findTopScoresByDifficulty(difficulty).stream()
            .limit(limit)
            .map(GameSessionResponseDTO::new)
            .collect(Collectors.toList());
    }

    public List<GameSessionResponseDTO> getTopScores(int limit) {
        return gameSessionRepository.findTopScores().stream()
            .limit(limit)
            .map(GameSessionResponseDTO::new)
            .collect(Collectors.toList());
    }

    public List<GameSessionResponseDTO> getPlayerTopScores(Long playerId, int limit) {
        return gameSessionRepository.findTopScoresByPlayer(playerId).stream()
            .limit(limit)
            .map(GameSessionResponseDTO::new)
            .collect(Collectors.toList());
    }

    public long getPlayerGameCount(Long playerId) {
        return gameSessionRepository.countByPlayerId(playerId);
    }

    private int calculateXPForDifficulty(com.yusurg.backend_yusurg.model.DifficultyLevel difficulty) {
        switch (difficulty) {
            case EASY:
                return 100;
            case MEDIUM:
                return 200;
            case HARD:
                return 300;
            default:
                return 100;
        }
    }
}
