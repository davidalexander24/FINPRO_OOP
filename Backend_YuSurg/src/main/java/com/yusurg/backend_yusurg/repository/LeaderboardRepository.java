package com.yusurg.backend_yusurg.repository;

import com.yusurg.backend_yusurg.model.DifficultyLevel;
import com.yusurg.backend_yusurg.model.LeaderboardEntry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Long> {

    @Query("SELECT le FROM LeaderboardEntry le ORDER BY le.score DESC")
    List<LeaderboardEntry> findTopScores(Pageable pageable);

    @Query("SELECT le FROM LeaderboardEntry le WHERE le.difficulty = :difficulty ORDER BY le.score DESC")
    List<LeaderboardEntry> findTopScoresByDifficulty(@Param("difficulty") DifficultyLevel difficulty, Pageable pageable);

    @Query("SELECT le FROM LeaderboardEntry le WHERE le.player.id = :playerId ORDER BY le.score DESC")
    List<LeaderboardEntry> findByPlayerId(@Param("playerId") Long playerId);

    @Query("SELECT le FROM LeaderboardEntry le WHERE le.player.username = :username ORDER BY le.score DESC")
    List<LeaderboardEntry> findByPlayerUsername(@Param("username") String username);

    @Query("SELECT COUNT(le) FROM LeaderboardEntry le WHERE le.score > :score")
    long countEntriesWithHigherScore(@Param("score") int score);
}
