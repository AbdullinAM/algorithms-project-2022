package sample;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ComputerSide {
    int[][] state;

    static int side;

    int rivalInQuestion = -1;

    public boolean isFriend(int i) {
        return rivalInQuestion * i > 0;
    }

    public boolean isEnemy(int i) {
        return rivalInQuestion * i < 0;
    }

    public ComputerSide() {
        state = new int[][]{{0, 1, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {-1, 0, -1, 0, -1, 0, -1, 0},
                {0, -1, 0, -1, 0, -1, 0, -1},
                {-1, 0, -1, 0, -1, 0, -1, 0}};
    }

    public ComputerSide(int[][] m, int side) {
        rivalInQuestion = side;
        state = new int[8][8];
        for (int i = 0; i < 8; i++)
            System.arraycopy(m[i], 0, state[i], 0, 8);
    }

    public boolean outOfLimits(int x, int y) {
        return x > 7 || x < 0 || y > 7 || y < 0;
    }

    public ComputerSide(ComputerSide prev, int y, int x, int newY, int newX, boolean wasFight) {
        int dirX = newX - x;
        int delta = Math.abs(dirX);
        dirX /= delta;
        int dirY = (newY - y) / delta;
        state = new int[8][8];
        for (int i = 0; i < 8; i++)
            System.arraycopy(prev.state[i], 0, state[i], 0, 8);
        state[newY][newX] = state[y][x];
        if (prev.rivalInQuestion == 1 && newY == 0 || prev.rivalInQuestion == -1 && newY == 7)
            state[newY][newX] *= 5;
        for (; delta > 0; delta--) {
            state[newY - delta * dirY][newX - delta * dirX] = 0;
        }

        if (wasFight && fightIsPossibleForPosition(newX, newY)) {
            fighter = new Pair<>(newY, newX);
            rivalInQuestion = prev.rivalInQuestion;
        } else {
            fighter = null;
            rivalInQuestion = (-1) * prev.rivalInQuestion;
        }
    }

    Pair<Integer, Integer> fighter = null;


    public List<Pair<Integer, Integer>> minimax(int depth) {
        List<Pair<Integer, Integer>> l = new ArrayList<>();//TODO заменить
        if (depth == 0) {
        }
        if (fighter != null) {
            int y = fighter.getKey();
            int x =fighter.getValue();
            int checker = state[y][x];
            if (Math.abs(checker) == 1) {//возможно стоит вынести в функцию
                for (int i = -1; i < 2; i += 2)
                    for (int j = -1; j < 2; j += 2) {
                        if (!outOfLimits(x + 2 * i, y + 2 * j) && isEnemy(state[y + j][x + i]) && state[y - 2 * checker][x - 2 * checker] == 0) {
                            ComputerSide newState = new ComputerSide(this, y, x, y + 2 * j, x + 2 * i, true);
                            newState.minimax(depth - 1);
                        }
                    }
            } else {
                for (int i = -1; i < 2; i += 2)
                    for (int j = -1; j < 2; j += 2)
                        for (int n = 1; !outOfLimits(x + n * i, y + n * j); n++) {
                            if (isFriend(state[y + j * n][x + i * n])) break;
                            if (state[y + j * n][x + i * n] == 0) {
                                ComputerSide newState = new ComputerSide(this, y, x, y + n * j, x + n * i, true);
                                newState.minimax(depth - 1);
                            } else {
                                do {
                                    n++;
                                } while (!outOfLimits(y + j * n, x + i * n) && isEnemy(state[y + j * n][x + i * n]));
                                if (state[y + j * n][x + i * n] == 0) {
                                    ComputerSide newState = new ComputerSide(this, y, x, y + n * j, x + n * i, true);
                                    newState.minimax(depth - 1);
                                } else break;

                            }
                        }
            }
        }
        else if (!fightIsPossibleForColor()) {
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    int checker = state[y][x];
                    if (checker == 0 || !isFriend(checker)) continue;
                    if (Math.abs(checker) == 1) {
                        if (!outOfLimits(y + checker, x + checker) && state[y + checker][x + checker] == 0) {
                            ComputerSide newState = new ComputerSide(this, y, x, y + checker, x + checker, false);
                            newState.minimax(depth - 1);
                        }
                        if (!outOfLimits(y + checker, x - checker) && state[y + checker][x - checker] == 0) {
                            ComputerSide newState = new ComputerSide(this, y, x, y + checker, x - checker, false);
                            newState.minimax(depth - 1);
                        }
                    } else {
                        for (int i = -1; i < 2; i += 2)
                            for (int j = -1; j < 2; j += 2) {
                                for (int n = 1; !outOfLimits(x + n * i, y + n * j); n++) {
                                    if (state[y][x] != 0) break;
                                    ComputerSide newState = new ComputerSide(this, y, x, y + n * j, x + n * i, false);
                                    newState.minimax(depth - 1);
                                }

                            }
                    }
                }
            }
        }
        else {
            for (int y = 0; y < 8; y++)
                for (int x = 0; x < 8; x++) {
                    int checker = state[y][x];
                    if (!isFriend(checker) || !fightIsPossibleForPosition(x, y)) continue;
                    if (Math.abs(checker) == 1) {
                        for (int i = -1; i < 2; i += 2)
                            for (int j = -1; j < 2; j += 2) {
                                if (!outOfLimits(x + 2 * i, y + 2 * j) && isEnemy(state[y + j][x + i]) && state[y - 2 * checker][x - 2 * checker] == 0) {
                                    ComputerSide newState = new ComputerSide(this, y, x, y + 2 * j, x + 2 * i, true);
                                    newState.minimax(depth - 1);
                                }
                            }
                    } else {
                        for (int i = -1; i < 2; i += 2)
                            for (int j = -1; j < 2; j += 2)
                                for (int n = 1; !outOfLimits(x + n * i, y + n * j); n++) {
                                    if (isFriend(state[y + j * n][x + i * n])) break;
                                    if (state[y + j * n][x + i * n] == 0) {
                                        ComputerSide newState = new ComputerSide(this, y, x, y + n * j, x + n * i, true);
                                        newState.minimax(depth - 1);
                                    } else {
                                        do {
                                            n++;
                                        } while (!outOfLimits(y + j * n, x + i * n) && isEnemy(state[y + j * n][x + i * n]));
                                        if (state[y + j * n][x + i * n] == 0) {
                                            ComputerSide newState = new ComputerSide(this, y, x, y + n * j, x + n * i, true);
                                            newState.minimax(depth - 1);
                                        } else break;

                                    }
                                }
                    }
                }




        }

        return l;
    }


    public boolean fightIsPossibleForPosition(int x, int y) {
        boolean r = false;
        if (Math.abs(rivalInQuestion) == 1) {
            for (int i = -1; i < 2; i += 2)
                for (int j = -1; j < 2; j += 2) {
                    if (!outOfLimits(x + 2 * i, y + 2 * j))
                        r = r || isFriend(state[y + j][x + i]) && state[y + 2 * j][x + 2 * i] == 0;
                }
            return r;
        } else {
            for (int i = -1; i < 2; i += 2)
                for (int j = -1; j < 2; j += 2) {
                    for (int n = 1; !outOfLimits(x + n * i, y + n * j); n++) {
                        if (state[y + n * j][x + n * i] != 0) {
                            if (isFriend(state[y + j * n][x + i * n])) break;
                            r = true;
                        } else if (r) return true;
                    }
                    r = false;
                }
        }
        return false;
    }

    public boolean fightIsPossibleForColor() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (!isEnemy(state[y][x])) continue;
                if (fightIsPossibleForPosition(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }
}
