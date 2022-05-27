package sample;

import javafx.util.Pair;

public class Computer {

    public static final int KING_SORE = 5;
    public static final int DEPTH = 4;


    int[][] state;

    static int side;

    int rivalInQuestion;
    Pair<Integer, Integer> fighter = null;

    public Computer() {
        state = new int[][]{{0, 1, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {-1, 0, -1, 0, -1, 0, -1, 0},
                {0, -1, 0, -1, 0, -1, 0, -1},
                {-1, 0, -1, 0, -1, 0, -1, 0}};
        side = rivalInQuestion = -1;
    }

    public Computer(int[][] m, int s, Pair<Integer,Integer> p) {
        side = s;
        rivalInQuestion = s;
        state = m;
        if (p != null && fightIsPossibleForPosition(p.getValue(), p.getKey())){
            fighter = p;
        }

    }

    public Computer(Computer prev, int y, int x, int newY, int newX, boolean wasFight) {
        int dirX = newX - x;
        int delta = Math.abs(dirX);
        dirX /= delta;
        int dirY = (newY - y) / delta;
        state = new int[8][8];
        for (int i = 0; i < 8; i++)
            System.arraycopy(prev.state[i], 0, state[i], 0, 8);
        state[newY][newX] = state[y][x];
        if (prev.rivalInQuestion == 1 && newY == 0 || prev.rivalInQuestion == -1 && newY == 7)
            state[newY][newX] *= KING_SORE;
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

    public boolean isFriend(int i) {
        return rivalInQuestion * i > 0;
    }

    public boolean isEnemy(int i) {
        return rivalInQuestion * i < 0;
    }

    public boolean isComputerSide(int i){
        return side*i>0;
    }

    public static boolean outOfLimits(int x, int y) {
        return x > 7 || x < 0 || y > 7 || y < 0;
    }

    public int cost(){
        int cost = 0;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                if (isComputerSide(state[i][j]))
                    cost+=Math.abs(state[i][j]);
                else cost-=Math.abs(state[i][j]);
            }
        return cost;
    }

    public int minOrMax(int a, int b) {
        if(rivalInQuestion==side) return Math.max(a, b);
        else return Math.min(a, b);
    }

    public int[] minimaxStart(int depth) {
        int r;
        if (rivalInQuestion==side) r = 0;
        else r = 40;
        int  y0, x0, newX, newY;
        y0 = x0 = newX = newY =-2;
        if (fighter != null) {
            int y = fighter.getKey();
            int x =fighter.getValue();
            int checker = state[y][x];
            if(rivalInQuestion == side)
                if (Math.abs(checker) == 1) {//возможно стоит вынести в функцию
                    for (int i = -1; i < 2; i += 2)
                        for (int j = -1; j < 2; j += 2) {
                            if (!outOfLimits(x + 2 * i, y + 2 * j) && isEnemy(state[y + j][x + i]) && state[y + 2 * j][x + 2 * i] == 0) {
                                Computer newState = new Computer(this, y, x, y + 2 * j, x + 2 * i, true);
                                int k = newState.minimax(depth - 1);
                                if(k >= r){
                                    r = k;
                                    y0 = y;
                                    x0 = x;
                                    newX = x + 2 * i;
                                    newY = y + 2 * j;
                                }
                            }
                        }
                } else {
                    for (int i = -1; i < 2; i += 2)
                        for (int j = -1; j < 2; j += 2)
                            for (int n = 1; !outOfLimits(x + n * i, y + n * j); n++) {
                                if (isFriend(state[y + j * n][x + i * n])) break;
                                if (state[y + j * n][x + i * n] == 0) {
                                    Computer newState = new Computer(this, y, x, y + n * j, x + n * i, true);
                                    int k = newState.minimax(depth - 1);
                                    if(k >= r){
                                        r = k;
                                        y0 = y;
                                        x0 = x;
                                        newX = x + n * i;
                                        newY = y + n * j;
                                    }
                                } else {
                                    do {
                                        n++;
                                    } while (!outOfLimits(y + j * n, x + i * n) && isEnemy(state[y + j * n][x + i * n]));
                                    if (outOfLimits(y + j * n, x + i * n)) {
                                        break;
                                    } else {
                                        Computer newState = new Computer(this, y, x, y + n * j, x + n * i, true);
                                        int k = newState.minimax(depth - 1);
                                        if(k >= r){
                                            r = k;
                                            y0 = y;
                                            x0 = x;
                                            newX = x + n * i;
                                            newY = y + n * j;
                                        }
                                    }

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
                            Computer newState = new Computer(this, y, x, y + checker, x + checker, false);
                            int k = newState.minimax(depth - 1);
                            if(k >= r){
                                r = k;
                                y0 = y;
                                x0 = x;
                                newX = x + checker;
                                newY = y + checker;
                            }
                        }
                        if (!outOfLimits(y + checker, x - checker) && state[y + checker][x - checker] == 0) {
                            Computer newState = new Computer(this, y, x, y + checker, x - checker, false);
                            int k = newState.minimax(depth - 1);
                            if(k >= r){
                                r = k;
                                y0 = y;
                                x0 = x;
                                newX = x - checker;
                                newY = y + checker;
                            }
                        }
                    } else {
                        for (int i = -1; i < 2; i += 2)
                            for (int j = -1; j < 2; j += 2) {
                                for (int n = 1; !outOfLimits(x + n * i, y + n * j); n++) {
                                    if (state[y][x] != 0) break;
                                    Computer newState = new Computer(this, y, x, y + n * j, x + n * i, false);
                                    int k = newState.minimax(depth - 1);
                                    if(k >= r){
                                        r = k;
                                        y0 = y;
                                        x0 = x;
                                        newX = x + n * i;
                                        newY = y + n * j;
                                    }
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
                                if (!outOfLimits(x + 2 * i, y + 2 * j) && isEnemy(state[y + j][x + i]) && state[y + 2*j][x + 2*i] == 0) {
                                    Computer newState = new Computer(this, y, x, y + 2 * j, x + 2 * i, true);
                                    int k = newState.minimax(depth - 1);
                                    if(k >= r){
                                        r = k;
                                        y0 = y;
                                        x0 = x;
                                        newX = x + 2 * i;
                                        newY = y + 2 * j;
                                    }
                                }
                            }
                    } else {
                        for (int i = -1; i < 2; i += 2)
                            for (int j = -1; j < 2; j += 2)
                                for (int n = 1; !outOfLimits(x + n * i, y + n * j); n++) {
                                    if (isFriend(state[y + j * n][x + i * n])) break;
                                    if (state[y + j * n][x + i * n] == 0) {
                                        Computer newState = new Computer(this, y, x, y + n * j, x + n * i, true);
                                        int k = newState.minimax(depth - 1);
                                        if(k >= r){
                                            r = k;
                                            y0 = y;
                                            x0 = x;
                                            newX = x + n * i;
                                            newY = y + n * j;
                                        }
                                    } else {
                                        do {
                                            n++;
                                        } while (!outOfLimits(y + j * n, x + i * n) && isEnemy(state[y + j * n][x + i * n]));
                                        if (outOfLimits(y + j * n, x + i * n)) break;
                                        else {
                                            Computer newState = new Computer(this, y, x, y + n * j, x + n * i, true);
                                            int k = newState.minimax(depth - 1);
                                            if (k >= r) {
                                                r = k;
                                                y0 = y;
                                                x0 = x;
                                                newX = x + n * i;
                                                newY = y + n * j;
                                            }
                                        }

                                    }
                                }
                    }
                }

        }

        return new int[]{x0,y0,newX,newY};
    }

    public int minimax(int depth) {
        int r;
        if (rivalInQuestion==side) r = 0;
        else r = 40;
        if (depth == 0) {
            return cost();
        }
        if (fighter != null) {
            int y = fighter.getKey();
            int x =fighter.getValue();
            int checker = state[y][x];
            if(rivalInQuestion == side)
            if (Math.abs(checker) == 1) {//возможно стоит вынести в функцию
                for (int i = -1; i < 2; i += 2)
                    for (int j = -1; j < 2; j += 2) {
                        if (!outOfLimits(x + 2 * i, y + 2 * j) && isEnemy(state[y + j][x + i]) && state[y + 2 * j][x + 2 * i] == 0) {
                            Computer newState = new Computer(this, y, x, y + 2 * j, x + 2 * i, true);
                            r = minOrMax(newState.minimax(depth - 1), r);
                        }
                    }
            } else {
                for (int i = -1; i < 2; i += 2)
                    for (int j = -1; j < 2; j += 2)
                        for (int n = 1; !outOfLimits(x + n * i, y + n * j); n++) {
                            if (isFriend(state[y + j * n][x + i * n])) break;
                            if (state[y + j * n][x + i * n] == 0) {
                                Computer newState = new Computer(this, y, x, y + n * j, x + n * i, true);
                                r = minOrMax(newState.minimax(depth - 1), r);
                            } else {
                                do {
                                    n++;
                                } while (!outOfLimits(y + j * n, x + i * n) && isEnemy(state[y + j * n][x + i * n]));
                                if (outOfLimits(y + j * n, x + i * n)) break;
                                else {
                                    Computer newState = new Computer(this, y, x, y + n * j, x + n * i, true);
                                    r = minOrMax(newState.minimax(depth - 1), r);
                                }

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
                            Computer newState = new Computer(this, y, x, y + checker, x + checker, false);
                            r = minOrMax(newState.minimax(depth - 1), r);
                        }
                        if (!outOfLimits(y + checker, x - checker) && state[y + checker][x - checker] == 0) {
                            Computer newState = new Computer(this, y, x, y + checker, x - checker, false);
                            r = minOrMax(newState.minimax(depth - 1), r);
                        }
                    } else {
                        for (int i = -1; i < 2; i += 2)
                            for (int j = -1; j < 2; j += 2) {
                                for (int n = 1; !outOfLimits(x + n * i, y + n * j); n++) {
                                    if (state[y][x] != 0) break;
                                    Computer newState = new Computer(this, y, x, y + n * j, x + n * i, false);
                                    r = minOrMax(newState.minimax(depth - 1), r);
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
                                if (!outOfLimits(x + 2 * i, y + 2 * j) && isEnemy(state[y + j][x + i]) && state[y + 2 * j][x + 2 * i] == 0) {
                                    Computer newState = new Computer(this, y, x, y + 2 * j, x + 2 * i, true);
                                    r = minOrMax(newState.minimax(depth - 1), r);
                                }
                            }
                    } else {
                        for (int i = -1; i < 2; i += 2)
                            for (int j = -1; j < 2; j += 2)
                                for (int n = 1; !outOfLimits(x + n * i, y + n * j); n++) {
                                    if (isFriend(state[y + j * n][x + i * n])) break;
                                    if (state[y + j * n][x + i * n] == 0) {
                                        Computer newState = new Computer(this, y, x, y + n * j, x + n * i, true);
                                        r = minOrMax(newState.minimax(depth - 1), r);
                                    } else {
                                        do {
                                            n++;
                                        } while (!outOfLimits(y + j * n, x + i * n) && isEnemy(state[y + j * n][x + i * n]));
                                        if (outOfLimits(y + j * n, x + i * n)) break;
                                        else{
                                            Computer newState = new Computer(this, y, x, y + n * j, x + n * i, true);
                                            r = minOrMax(newState.minimax(depth - 1), r);
                                        }

                                    }
                                }
                    }
                }
        }
        return r;
    }

    public boolean fightIsPossibleForPosition(int x, int y) {
        boolean r = false;
        if (Math.abs(state[y][x]) == 1) {
            for (int i = -1; i < 2; i += 2)
                for (int j = -1; j < 2; j += 2) {
                    if (!outOfLimits(x + 2 * i, y + 2 * j))
                        r = r || isEnemy(state[y + j][x + i]) && state[y + 2 * j][x + 2 * i] == 0;
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
                if (!isFriend(state[y][x])) continue;
                if (fightIsPossibleForPosition(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }
}
