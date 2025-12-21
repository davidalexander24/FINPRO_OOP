package com.yusurg.backend_yusurg.repository;

import com.yusurg.backend_yusurg.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT p FROM Player p ORDER BY p.totalExp DESC")
    List<Player> findTopPlayersByTotalScore();

    @Query("SELECT p FROM Player p ORDER BY p.surgeonLevel DESC, p.totalExp DESC")
    List<Player> findTopPlayersByLevel();
}
