package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {
    private final DatabaseManager dbManager;

    public GameDAO() {
        dbManager = DatabaseManager.getInstance();
    }

    public void saveGameResult(String player1, String player2, String winner) {
        dbManager.saveGameResult(player1, player2, winner);
    }

    public List<String> getGameHistory() {
        List<String> history = new ArrayList<>();
        String sql = "SELECT p1.name as player1, p2.name as player2, " +
                "CASE WHEN g.winner_id = -1 THEN 'Draw' ELSE pw.name END as winner, " +
                "g.game_date " +
                "FROM games g " +
                "JOIN players p1 ON g.player1_id = p1.id " +
                "JOIN players p2 ON g.player2_id = p2.id " +
                "LEFT JOIN players pw ON g.winner_id = pw.id " +
                "ORDER BY g.game_date DESC LIMIT 10";

        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String entry = String.format("%s vs %s - Winner: %s (%s)",
                        rs.getString("player1"),
                        rs.getString("player2"),
                        rs.getString("winner"),
                        rs.getTimestamp("game_date"));
                history.add(entry);
            }
        } catch (SQLException e) {
            System.err.println("Error getting game history: " + e.getMessage());
        }
        return history;
    }

    public List<String> getLeaderboard() {
        return dbManager.getLeaderboard();
    }

    public void close() {
        dbManager.close();
    }
} 