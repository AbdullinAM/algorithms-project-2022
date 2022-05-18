package org.spbstu.aleksandrov.model;

import org.spbstu.aleksandrov.model.Tetromino.Coordinate;

import java.util.ArrayList;
import java.util.Collections;

import static org.spbstu.aleksandrov.model.Tetromino.Movement.*;

public class GameField {

    private final CellType[][] gameField = new CellType[22][10];

    private static final Coordinate[/*from what state rotation is*/][/*number of attempt*/] wallKickData = {
            { new Coordinate(-1, 0), new Coordinate(-1, 1),
                    new Coordinate(0, -2), new Coordinate(-1, -2) },
            { new Coordinate(1, 0), new Coordinate(1, -1),
                    new Coordinate(0, 2), new Coordinate(1, 2) },
            { new Coordinate(1, 0), new Coordinate(1, 1),
                    new Coordinate(0, -2), new Coordinate(1, -2) },
            { new Coordinate(-1, 0), new Coordinate(-1, -1),
                    new Coordinate(0, 2), new Coordinate(-1, 2) }
    };

    private static final Coordinate[/**/][/*number of attempt*/] wallKickDataI = {
            { new Coordinate(-2, 0), new Coordinate(1, 0),
                    new Coordinate(-2, -1), new Coordinate(1, 2) },
            { new Coordinate(-1, 0), new Coordinate(2, 0),
                    new Coordinate(-1, 2), new Coordinate(2, -1) },
    };

    // TODO merge with Tetromino.Type
    @SuppressWarnings("unused")
    public enum CellType {
        ORANGE, BLUE, GREEN, RED, PURPLE, CYAN, YELLOW, SPACE
    }

    public GameField() {
        for (int i = 0; i < 22; i++) {
            for (int j = 0; j < 10; j++) {
                gameField[i][j] = CellType.SPACE;
            }
        }
    }

    public boolean areCellsEmpty(ArrayList<Tetromino.Coordinate> coordinates) {
        for (Tetromino.Coordinate coordinate : coordinates) {
            if (coordinate.getX() > 9 || coordinate.getX() < 0 || coordinate.getY() > 21 || coordinate.getY() < 0)
                return false;
            if (gameField[coordinate.getY()][coordinate.getX()] != CellType.SPACE) return false;
        }
        return true;
    }

    ArrayList<Integer> linesToClear = new ArrayList<>(4);

    // Pass to this method coordinates recently stacked tetromino
    public int checkLinesToClear(ArrayList<Coordinate> coordinates) {

        int result = 0;

        for (Coordinate coordinate : coordinates) {

            int i = coordinate.getY();
            if (linesToClear.contains(i)) continue;
            boolean fullLine = true;

            for (int j = 0; j < 10; j++) {
                if (gameField[i][j] == CellType.SPACE) {
                    fullLine = false;
                    break;
                }
            }
            if (fullLine) {
                result++;
                linesToClear.add(i);
            }
        }
        return result;
    }

    public void cleanLines() {
        Collections.sort(linesToClear, Collections.<Integer>reverseOrder());
        // move down whole field above cleaned line
        for (int n : linesToClear) {
            for (int j = n + 1; j < 22; j++) {
                System.arraycopy(gameField[j], 0, gameField[j - 1], 0, 10);
                if (j == 21) for (int m = 0; m < 10; m++) gameField[j][m] = CellType.SPACE;
            }
        }
        linesToClear.clear();
    }

    public void rotateOnField(Tetromino.Movement direction, Tetromino tetromino) {
        // direction = -1 is clockwise

        int state = tetromino.getState();
        tetromino.rotate(direction);

        boolean stable = areCellsEmpty(tetromino.getCoordinates());

        int attempt;
        Coordinate[][] source;

        if (tetromino.getType() == Tetromino.Type.I) source = wallKickDataI;
        else source = wallKickData;

        Coordinate offset = new Coordinate(0, 0);

        if (!stable) {
            // wall kicks

            attempt = 0;

            do {
                if (attempt != 0) {
                    offset.negation();
                    tetromino.move(offset);
                }
                if (tetromino.getType() != Tetromino.Type.I) {
                    offset = source[state][attempt].deepClone();
                    if (direction == ROT_L && state % 2 == 0) offset.setX(-offset.getX());
                }
                else {
                    int index;
                    if (direction == ROT_L) index = (state + 1) % 2; else index = state % 2;
                    offset = source[index][attempt].deepClone();
                    if ((state > 1 && direction == ROT_R) ||
                            (state > 0 && state < 3 && direction == ROT_L))
                        offset.negation();
                }
                tetromino.move(offset);
                attempt++;
                stable = areCellsEmpty(tetromino.getCoordinates());
            } while (!stable && attempt < 4);
        }
        if (!stable) {
            offset.negation();
            tetromino.move(offset);
            tetromino.rotate(direction.negation());
            return;
        }

        // if state changed then rotation was done, Listener is notified
        if (state != tetromino.getState()) tetromino.notifyListener(direction);
    }

    public void stackTetromino(Tetromino tetromino) {
        for (Tetromino.Coordinate coordinate : tetromino.getCoordinates()) {
            gameField[coordinate.getY()][coordinate.getX()] = CellType.valueOf(String.valueOf(tetromino.getColor()));
        }
    }

    public CellType[][] getGameField() {
        return gameField;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public GameField clone() {
        GameField newGameField = new GameField();
        for (int j = 0; j < 10; j++)
            for (int i = 0; i < 22; i++)
                newGameField.gameField[i][j] = this.gameField[i][j];
        return newGameField;
    }
}
