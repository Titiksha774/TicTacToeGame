package database;

public class DatabaseConfig {
    public static final String DB_URL = "jdbc:sqlite:tic_tac_toe.db";
    
   
    public static final String DB_USER = "";
    public static final String DB_PASSWORD = "";
   
    public static final String JDBC_DRIVER = "org.sqlite.JDBC";
    
    public static final int MAX_POOL_SIZE = 10;
    public static final int MIN_POOL_SIZE = 5;
    public static final int CONNECTION_TIMEOUT = 30000; // 30 seconds
} 