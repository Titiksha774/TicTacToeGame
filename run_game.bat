@echo off
echo Compiling Tic Tac Toe game...
javac -cp "lib/*;src" src/database/*.java src/TicTacToe.java
if errorlevel 1 (
    echo Compilation failed!
    pause
    exit /b 1
)
echo Compilation successful!
echo Starting the game...
java -cp "src;lib/*" TicTacToe
pause 