package com.yusurg.backend_yusurg.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseMigration implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            jdbcTemplate.execute("ALTER TABLE game_sessions DROP CONSTRAINT IF EXISTS game_sessions_outcome_check");
            System.out.println("✅ Dropped old game_sessions_outcome_check constraint");

            jdbcTemplate.execute("ALTER TABLE game_sessions ADD CONSTRAINT game_sessions_outcome_check CHECK (outcome IN ('IN_PROGRESS', 'WIN', 'LOSS', 'ABANDONED'))");
            System.out.println("✅ Added new game_sessions_outcome_check constraint with IN_PROGRESS");
        } catch (Exception e) {
            System.out.println("⚠️ Migration note: " + e.getMessage());
        }
    }
}

