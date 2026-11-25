# Tic Tac Toe Game with SQLite Database

A Java-based Tic Tac Toe game that stores game results and player statistics in an SQLite database.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- SQLite JDBC driver (sqlite-jdbc-3.49.1.0.jar)

## Project Structure

```
ProjectCIE3_2.0/
├── lib/
│   └── sqlite-jdbc-3.49.1.0.jar
├── src/
│   ├── database/
│   │   ├── DatabaseConfig.java
│   │   ├── DatabaseManager.java
│   │   └── GameDAO.java
│   └── TicTacToe.java
└── tic_tac_toe.db (created automatically)
```

## Setup

On Windows you can just double-click `run_game.bat`, which compiles and starts the game. To do it by hand:

1. The SQLite JDBC driver (`sqlite-jdbc-3.49.1.0.jar`) is already in `lib/`. If it is missing, download it from https://github.com/xerial/sqlite-jdbc/releases and put it there.

2. Compile the project:
```powershell
javac -cp "lib/*;src" src/database/*.java src/TicTacToe.java
```

3. Run the game:
```powershell
java -cp "src;lib/*" TicTacToe
```

## Features

- Two-player Tic Tac Toe game
- Player name input
- Game result tracking
- Leaderboard display
- SQLite database storage

## Database Schema

The game uses two tables:

1. `players` table:
   - id (INTEGER PRIMARY KEY)
   - name (TEXT UNIQUE)
   - wins (INTEGER)
   - losses (INTEGER)
   - draws (INTEGER)

2. `games` table:
   - id (INTEGER PRIMARY KEY)
   - player1_id (INTEGER)
   - player2_id (INTEGER)
   - winner_id (INTEGER)
   - game_date (TIMESTAMP)

## How to Play

1. Run the game
2. Enter player names when prompted
3. Take turns entering row and column numbers (0-2)
4. Game ends when someone wins or it's a draw
5. Leaderboard is displayed after each game

## Notes

- The database file `tic_tac_toe.db` is created automatically in the project directory
- Game history is limited to the last 10 games
- Leaderboard shows players ranked by points (3 points per win, 1 point per draw) 