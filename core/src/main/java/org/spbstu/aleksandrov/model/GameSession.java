package org.spbstu.aleksandrov.model;

import java.util.Random;

import static org.spbstu.aleksandrov.model.Tetromino.Type.*;

public class GameSession {

    private final Tetromino.Type[] bucket = {L, J, S, Z, T, I, O};
    public static final int[] FRAMES_PER_STEP = {
            48, 43, 38, 33, 28, 23, 18, 13, 8, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1
    };
    private final int[] POINTS = {40, 100, 300, 1200};
    private final Random random = new Random();
    private Tetromino fallingTetromino;
    private Tetromino fallingProjection;
    private Tetromino nextTetromino;

    private int counter = 0;
    private int score = 0;
    private int highScore;
    private int level = 1;
    private int tCounter = 0;
    private int linesCleared;
    private final GameField gameField;
    private boolean gameOver = false;

    public GameSession(int highScore) {
        this.highScore = highScore;
        shuffleBucket();
        nextTetromino = new Tetromino(bucket[counter]);
        counter++;
        gameField = new GameField();
        generateNewTetromino();
        updateFallingProjection();
    }

    public void updateFallingProjection() {
        fallingProjection = fallingTetromino.clone();
        while (gameField.areCellsEmpty(fallingProjection.getCoordinates()))
            fallingProjection.move(0, -1);
        fallingProjection.move(0, 1);
    }

    public void step() {

        fallingTetromino.move(0, -1);

        if (!gameField.areCellsEmpty(fallingTetromino.getCoordinates())) {
            fallingTetromino.move(0, 1);
            updateFallingProjection();
            hardDrop();
        }
    }

    public boolean hardDrop() {
        int blocks = fallingTetromino.getRotationPoint().getY() -
                fallingProjection.getRotationPoint().getY();
        score += blocks;
        gameField.stackTetromino(fallingProjection);
        int lines = gameField.checkLinesToClear(fallingProjection.getCoordinates());
        gameField.cleanLines();
        linesCleared += lines;
        level = linesCleared / 10 + 1;
        if (lines != 0) score += (level + 1) * POINTS[lines - 1];
        if (score > highScore) highScore = score;
        generateNewTetromino();
        updateFallingProjection();
        return true;
    }

    private boolean ready = true;

    public boolean isReady() {
        return ready;
    }

    public void generateNewTetromino() {
        ready = false;
        tCounter++;
        if (counter == 7) {
            shuffleBucket();
            counter = 0;
        }

        if (!gameField.areCellsEmpty(nextTetromino.getCoordinates())) {
            gameOver = true;
            return;
        }

        fallingTetromino = nextTetromino;
        nextTetromino = new Tetromino(bucket[counter]);
        counter++;

        fallingTetromino.move(0, -2);
        while (!gameField.areCellsEmpty(fallingTetromino.getCoordinates()))
            fallingTetromino.move(0, 1);
        ready = true;
    }

    public void shuffleBucket() {
        int n = 7;
        for (int i = n - 1; i > 1; i--) {
            int j = random.nextInt(i);
            Tetromino.Type t = bucket[j];
            bucket[j] = bucket[i];
            bucket[i] = t;
        }
    }

    public void addPoints(int delta) {
        score += delta;
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    public GameField getGameField() {
        return gameField;
    }

    public Tetromino getFallingTetromino() {
        return fallingTetromino;
    }

    public Tetromino getFallingProjection() {
        return fallingProjection;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Tetromino getNextTetromino() {
        return nextTetromino;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        linesCleared = (level - 1) * 10;
        this.level = level;
    }

    public int getLinesCleared() {
        return linesCleared;
    }

    public Tetromino.Type[] getBucket() {
        return bucket;
    }

    public int getCounter() {
        return counter;
    }

    public int getTCounter() {
        return tCounter;
    }
}
