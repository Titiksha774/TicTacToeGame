package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        try {
            Class.forName(DatabaseConfig.JDBC_DRIVER);
            connection = DriverManager.getConnection(DatabaseConfig.DB_URL);
            createTables();
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Create players table
            stmt.execute("CREATE TABLE IF NOT EXISTS players (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT UNIQUE NOT NULL, " +
                    "wins INTEGER DEFAULT 0, " +
                    "losses INTEGER DEFAULT 0, " +
                    "draws INTEGER DEFAULT 0)");

            // Create games table
            stmt.execute("CREATE TABLE IF NOT EXISTS games (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "player1_id INTEGER, " +
                    "player2_id INTEGER, " +
                    "winner_id INTEGER, " +
                    "game_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (player1_id) REFERENCES players(id), " +
                    "FOREIGN KEY (player2_id) REFERENCES players(id), " +
                    "FOREIGN KEY (winner_id) REFERENCES players(id))");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void saveGameResult(String player1Name, String player2Name, String winnerName) {
        try {
            // Insert or update players
            int player1Id = insertOrUpdatePlayer(player1Name);
            int player2Id = insertOrUpdatePlayer(player2Name);
            int winnerId = winnerName.equals("Draw") ? -1 : insertOrUpdatePlayer(winnerName);

            // Insert game result
            String sql = "INSERT INTO games (player1_id, player2_id, winner_id) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, player1Id);
                pstmt.setInt(2, player2Id);
                pstmt.setInt(3, winnerId);
                pstmt.executeUpdate();
            }

            // Update player statistics
            updatePlayerStats(player1Name, winnerName.equals(player1Name), winnerName.equals("Draw"));
            updatePlayerStats(player2Name, winnerName.equals(player2Name), winnerName.equals("Draw"));

        } catch (SQLException e) {
            System.err.println("Error saving game result: " + e.getMessage());
        }
    }

    private int insertOrUpdatePlayer(String playerName) throws SQLException {
        String sql = "INSERT OR IGNORE INTO players (name) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            pstmt.executeUpdate();
        }

        sql = "SELECT id FROM players WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }

    private void updatePlayerStats(String playerName, boolean isWinner, boolean isDraw) throws SQLException {
        String sql = "UPDATE players SET " +
                (isDraw ? "draws = draws + 1" :
                 isWinner ? "wins = wins + 1" : "losses = losses + 1") +
                " WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            pstmt.executeUpdate();
        }
    }

    public List<String> getLeaderboard() {
        List<String> leaderboard = new ArrayList<>();
        String sql = "SELECT name, wins, losses, draws, " +
                "(wins * 3 + draws) as points " +
                "FROM players " +
                "ORDER BY points DESC, wins DESC, draws DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String entry = String.format("%s - Wins: %d, Losses: %d, Draws: %d, Points: %d",
                        rs.getString("name"),
                        rs.getInt("wins"),
                        rs.getInt("losses"),
                        rs.getInt("draws"),
                        rs.getInt("points"));
                leaderboard.add(entry);
            }
        } catch (SQLException e) {
            System.err.println("Error getting leaderboard: " + e.getMessage());
        }
        return leaderboard;
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
} 