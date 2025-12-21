package com.yusurg.backend_yusurg.service;

import com.yusurg.backend_yusurg.dto.PlayerDTO;
import com.yusurg.backend_yusurg.dto.PlayerResponseDTO;
import com.yusurg.backend_yusurg.exception.PlayerNotFoundException;
import com.yusurg.backend_yusurg.exception.UsernameAlreadyExistsException;
import com.yusurg.backend_yusurg.model.Player;
import com.yusurg.backend_yusurg.repository.PlayerRepository;
import com.yusurg.backend_yusurg.repository.GameSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final GameSessionRepository gameSessionRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, GameSessionRepository gameSessionRepository) {
        this.playerRepository = playerRepository;
        this.gameSessionRepository = gameSessionRepository;
    }

    public PlayerResponseDTO createPlayer(PlayerDTO playerDTO) {
        if (playerRepository.existsByUsername(playerDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists: " + playerDTO.getUsername());
        }

        Player player = new Player(playerDTO.getUsername());
        Player savedPlayer = playerRepository.save(player);
        return new PlayerResponseDTO(savedPlayer);
    }

    public PlayerResponseDTO getPlayerById(Long id) {
        Player player = playerRepository.findById(id)
            .orElseThrow(() -> new PlayerNotFoundException("Player not found with ID: " + id));
        return new PlayerResponseDTO(player);
    }

    public PlayerResponseDTO getPlayerByUsername(String username) {
        Player player = playerRepository.findByUsername(username)
            .orElseThrow(() -> new PlayerNotFoundException("Player not found with username: " + username));
        return new PlayerResponseDTO(player);
    }

    public PlayerResponseDTO getOrCreatePlayer(String username) {
        return playerRepository.findByUsername(username)
            .map(player -> {
                player.setLastLogin(LocalDateTime.now());
                return new PlayerResponseDTO(playerRepository.save(player));
            })
            .orElseGet(() -> createPlayer(new PlayerDTO(username)));
    }

    public List<PlayerResponseDTO> getAllPlayers() {
        return playerRepository.findAll().stream()
            .map(PlayerResponseDTO::new)
            .collect(Collectors.toList());
    }

    public List<PlayerResponseDTO> getTopPlayersByScore(int limit) {
        return playerRepository.findTopPlayersByTotalScore().stream()
            .limit(limit)
            .map(PlayerResponseDTO::new)
            .collect(Collectors.toList());
    }

    public List<PlayerResponseDTO> getTopPlayersByLevel(int limit) {
        return playerRepository.findTopPlayersByLevel().stream()
            .limit(limit)
            .map(PlayerResponseDTO::new)
            .collect(Collectors.toList());
    }

    public void updateLastLogin(Long playerId) {
        Player player = playerRepository.findById(playerId)
            .orElseThrow(() -> new PlayerNotFoundException("Player not found with ID: " + playerId));
        player.setLastLogin(LocalDateTime.now());
        playerRepository.save(player);
    }

    public void deletePlayer(Long id) {
        if (!playerRepository.existsById(id)) {
            throw new PlayerNotFoundException("Player not found with ID: " + id);
        }
        playerRepository.deleteById(id);
    }

    public Player getPlayerEntityById(Long id) {
        return playerRepository.findById(id)
            .orElseThrow(() -> new PlayerNotFoundException("Player not found with ID: " + id));
    }

    public PlayerResponseDTO updatePlayerStats(Long playerId, int expGained) {
        Player player = getPlayerEntityById(playerId);

        int newTotalExp = player.getTotalExp() + expGained;
        player.setTotalExp(newTotalExp);

        int newLevel = calculateLevel(newTotalExp);
        player.setSurgeonLevel(Math.min(newLevel, 10));

        Player savedPlayer = playerRepository.save(player);
        return new PlayerResponseDTO(savedPlayer);
    }

    public PlayerResponseDTO updatePlayerStats(Player player, int expGained) {
        int newTotalExp = player.getTotalExp() + expGained;
        player.setTotalExp(newTotalExp);

        int newLevel = calculateLevel(newTotalExp);
        player.setSurgeonLevel(Math.min(newLevel, 10));

        Player savedPlayer = playerRepository.save(player);
        return new PlayerResponseDTO(savedPlayer);
    }

    private int calculateLevel(int totalExp) {
        return Math.min((totalExp / 500) + 1, 10);
    }
}
