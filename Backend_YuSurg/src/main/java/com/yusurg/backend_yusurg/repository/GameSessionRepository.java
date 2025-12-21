package com.yusurg.backend_yusurg.repository;

import com.yusurg.backend_yusurg.model.DifficultyLevel;
import com.yusurg.backend_yusurg.model.GameSession;
import com.yusurg.backend_yusurg.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {

    List<GameSession> findByPlayerOrderByStartedAtDesc(Player player);

    List<GameSession> findByPlayerIdOrderByStartedAtDesc(Long playerId);

    List<GameSession> findByDifficulty(DifficultyLevel difficulty);

    @Query("SELECT gs FROM GameSession gs WHERE gs.player.id = :playerId ORDER BY gs.score DESC")
    List<GameSession> findTopScoresByPlayer(@Param("playerId") Long playerId);

    @Query("SELECT gs FROM GameSession gs ORDER BY gs.score DESC")
    List<GameSession> findTopScores();

    @Query("SELECT gs FROM GameSession gs WHERE gs.difficulty = :difficulty ORDER BY gs.score DESC")
    List<GameSession> findTopScoresByDifficulty(@Param("difficulty") DifficultyLevel difficulty);

    List<GameSession> findByPlayerId(Long playerId);

    @Query("SELECT COUNT(gs) FROM GameSession gs WHERE gs.player.id = :playerId")
    long countByPlayerId(@Param("playerId") Long playerId);
}
