package com.yusurg.backend_yusurg.service;

import com.yusurg.backend_yusurg.dto.LeaderboardEntryDTO;
import com.yusurg.backend_yusurg.model.DifficultyLevel;
import com.yusurg.backend_yusurg.model.LeaderboardEntry;
import com.yusurg.backend_yusurg.model.Player;
import com.yusurg.backend_yusurg.repository.LeaderboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;

    @Autowired
    public LeaderboardService(LeaderboardRepository leaderboardRepository) {
        this.leaderboardRepository = leaderboardRepository;
    }

    public LeaderboardEntryDTO addEntry(Player player, int score, DifficultyLevel difficulty, float gameDurationSeconds) {
        LeaderboardEntry entry = new LeaderboardEntry(player, score, difficulty, gameDurationSeconds);
        LeaderboardEntry savedEntry = leaderboardRepository.save(entry);

        int rank = (int) leaderboardRepository.countEntriesWithHigherScore(score) + 1;
        return new LeaderboardEntryDTO(savedEntry, rank);
    }

    public List<LeaderboardEntryDTO> getTopScores(int limit) {
        List<LeaderboardEntry> entries = leaderboardRepository.findTopScores(PageRequest.of(0, limit));
        return convertToDTO(entries);
    }

    public List<LeaderboardEntryDTO> getTopScoresByDifficulty(DifficultyLevel difficulty, int limit) {
        List<LeaderboardEntry> entries = leaderboardRepository.findTopScoresByDifficulty(
            difficulty, PageRequest.of(0, limit));
        return convertToDTO(entries);
    }

    public List<LeaderboardEntryDTO> getPlayerEntries(Long playerId) {
        List<LeaderboardEntry> entries = leaderboardRepository.findByPlayerId(playerId);
        return convertToDTOWithRanks(entries);
    }

    public int getPlayerRank(int score) {
        return (int) leaderboardRepository.countEntriesWithHigherScore(score) + 1;
    }

    private List<LeaderboardEntryDTO> convertToDTO(List<LeaderboardEntry> entries) {
        List<LeaderboardEntryDTO> dtos = new ArrayList<>();
        int rank = 1;
        for (LeaderboardEntry entry : entries) {
            dtos.add(new LeaderboardEntryDTO(entry, rank++));
        }
        return dtos;
    }

    private List<LeaderboardEntryDTO> convertToDTOWithRanks(List<LeaderboardEntry> entries) {
        List<LeaderboardEntryDTO> dtos = new ArrayList<>();
        for (LeaderboardEntry entry : entries) {
            int rank = (int) leaderboardRepository.countEntriesWithHigherScore(entry.getScore()) + 1;
            dtos.add(new LeaderboardEntryDTO(entry, rank));
        }
        return dtos;
    }
}
