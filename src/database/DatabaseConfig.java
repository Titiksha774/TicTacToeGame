package database;

public class DatabaseConfig {
    // JDBC URL for SQLite database
    public static final String DB_URL = "jdbc:sqlite:tic_tac_toe.db";
    
    // Database credentials (not needed for SQLite)
    public static final String DB_USER = "";
    public static final String DB_PASSWORD = "";
    
    // JDBC Driver class
    public static final String JDBC_DRIVER = "org.sqlite.JDBC";
    
    // Connection pool settings
    public static final int MAX_POOL_SIZE = 10;
    public static final int MIN_POOL_SIZE = 5;
    public static final int CONNECTION_TIMEOUT = 30000; // 30 seconds
} 