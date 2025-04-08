import database.GameDAO;
import java.util.Scanner;

public class TicTacToe {
    private char[][] board;
    private char currentPlayer;
    private String player1;
    private String player2;
    private final GameDAO gameDAO;

    public TicTacToe() {
        board = new char[3][3];
        currentPlayer = 'X';
        gameDAO = new GameDAO();
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    private void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println();
            System.out.println("-------------");
        }
    }

    private boolean makeMove(int row, int col) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3 || board[row][col] != ' ') {
            return false;
        }
        board[row][col] = currentPlayer;
        return true;
    }

    private boolean checkWin() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != ' ' && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return true;
            }
        }

        // Check diagonals
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return true;
        }
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return true;
        }

        return false;
    }

    private boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Welcome to Tic Tac Toe!");
        System.out.print("Enter Player 1 name: ");
        player1 = scanner.nextLine();
        System.out.print("Enter Player 2 name: ");
        player2 = scanner.nextLine();

        while (true) {
            printBoard();
            System.out.println("Current player: " + (currentPlayer == 'X' ? player1 : player2));
            System.out.print("Enter row (0-2) and column (0-2) separated by space: ");
            
            try {
                int row = scanner.nextInt();
                int col = scanner.nextInt();
                
                if (makeMove(row, col)) {
                    if (checkWin()) {
                        printBoard();
                        String winner = currentPlayer == 'X' ? player1 : player2;
                        System.out.println(winner + " wins!");
                        gameDAO.saveGameResult(player1, player2, winner);
                        break;
                    } else if (checkDraw()) {
                        printBoard();
                        System.out.println("It's a draw!");
                        gameDAO.saveGameResult(player1, player2, "Draw");
                        break;
                    }
                    currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                } else {
                    System.out.println("Invalid move! Try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter numbers between 0 and 2.");
                scanner.nextLine(); // Clear the invalid input
            }
        }

        showLeaderboard();
        scanner.close();
        gameDAO.close();
    }

    private void showLeaderboard() {
        System.out.println("\nLeaderboard:");
        System.out.println("------------");
        gameDAO.getLeaderboard().forEach(System.out::println);
    }

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        game.startGame();
    }
} 