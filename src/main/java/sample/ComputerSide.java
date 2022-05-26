package sample;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ComputerSide {
    int[][] state;

    static int side;

    int rivalInQuestion = -1;
    //Pair<Integer, Integer> fightingChecker = null;

    public boolean isFriend(int i){
        return rivalInQuestion *i>0;
    }

    public boolean isEnemy(int i){
        return rivalInQuestion *i<0;
    }

    //List<Pair<Integer, Integer>> checkersThatCanBeat;

    public ComputerSide() {
        state = new int[][]{{0, 1, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {-1, 0, -1, 0, -1, 0, -1, 0},
                {0, -1, 0, -1, 0, -1, 0, -1},
                {-1, 0, -1, 0, -1, 0, -1, 0}};
        //checkersThatCanBeat = this.abilityToBeat();
    }

    public ComputerSide(int[][] m, int side) {
        rivalInQuestion = side;
        state = new int[8][8];
        for (int i = 0; i < 8; i++)
            System.arraycopy(m[i], 0, state[i], 0, 8);
        //checkersThatCanBeat = this.abilityToBeat();
    }

/*
    public ComputerSide(int[][] prevM, int y, int x, int newX, int newY, List<Pair<Integer, Integer>> kill, int side) {
        rivalInQuestion = side;
        state = new int[8][8];
        for (int i = 0; i < 8; i++)
            System.arraycopy(prevM[i], 0, state[i], 0, 8);
        state[newY][newX] = state[y][x];
        state[y][x] = 0;
        for (Pair<Integer, Integer> i : kill) {
            state[i.getKey()][i.getValue()] = 0;
        }
        //checkersThatCanBeat = this.abilityToBeat();

    }
    public ComputerSide(int[][] prevM, int y, int x, int newX, int newY, List<Pair<Integer, Integer>> kill, int side, boolean fightContinue) {
        fightingChecker = new Pair<>(newY,newX);
        rivalInQuestion = side;
        state = new int[8][8];
        for (int i = 0; i < 8; i++)
            System.arraycopy(prevM[i], 0, state[i], 0, 8);
        state[newY][newX] = state[y][x];
        state[y][x] = 0;
        for (Pair<Integer, Integer> i : kill) {
            state[i.getKey()][i.getValue()] = 0;
        }
        //checkersThatCanBeat = this.abilityToBeat();

    }
*/

/*
    public List<Pair<Integer, Integer>> abilityToBeat() {
        List<Pair<Integer, Integer>> l = new ArrayList<>();
        for (int y = 0; y < 8; y++)
            for (int x = 0; x < 8; x++) {
                if (!isFriend(state[y][x])) continue;

                if (Math.abs(state[y][x]) > 1) {
                    for (int k = -1; !outOfLimits(y + k, x + k); k--) {
                        int s = state[y + k][x + k];
                        if (s == 0) continue;
                        if (isFriend(s)) break;
                        while (!outOfLimits(y + k - 1, x + k - 1)) {
                            if (state[y + k - 1][x + k - 1] == 0) {
                                l.add(new Pair<>(y, x));
                                break;
                            }
                            k--;
                        }
                        break;
                    }
                    for (int k = -1; !outOfLimits(y - k, x + k); k--) {
                        int s = state[y - k][x + k];
                        if (s == 0) continue;
                        if (isFriend(s)) break;
                        while (!outOfLimits(y - k + 1, x + k - 1)) {
                            if (state[y - k + 1][x + k - 1] == 0) {
                                l.add(new Pair<>(y, x));
                                break;
                            }
                            k--;
                        }
                        break;
                    }
                    for (int k = -1; !outOfLimits(y + k, x - k); k--) {
                        int s = state[y + k][x - k];
                        if (s == 0) continue;
                        if (isFriend(s)) break;
                        while (!outOfLimits(y + k - 1, x - k + 1)) {
                            if (state[y + k - 1][x - k + 1] == 0) {
                                l.add(new Pair<>(y, x));
                                break;
                            }
                            k--;
                        }
                        break;
                    }
                    for (int k = -1; !outOfLimits(y - k, x - k); k--) {
                        int s = state[y - k][x - k];
                        if (s == 0) continue;
                        if (isFriend(s)) break;
                        while (!outOfLimits(y - k + 1, x - k + 1)) {
                            if (state[y - k + 1][x - k + 1] == 0) {
                                l.add(new Pair<>(y, x));
                                break;
                            }
                            k--;
                        }
                        break;
                    }
                } else if (Math.abs(state[y][x]) == 1) {
                    if (!outOfLimits(y - 2 * rivalInQuestion, x - 2 * rivalInQuestion) && this.state[y - rivalInQuestion][x - rivalInQuestion] == rivalInQuestion * (-1) && this.state[y - 2 * rivalInQuestion][x - 2 * rivalInQuestion] == 0 ||
                            !outOfLimits(y - 2 * rivalInQuestion, x + 2 * rivalInQuestion) && this.state[y - rivalInQuestion][x + rivalInQuestion] == rivalInQuestion * (-1) && this.state[y - 2 * rivalInQuestion][x + 2 * rivalInQuestion] == 0 ||
                            !outOfLimits(y + 2 * rivalInQuestion, x + 2 * rivalInQuestion) && this.state[y + rivalInQuestion][x + rivalInQuestion] == rivalInQuestion * (-1) && this.state[y + 2 * rivalInQuestion][x + 2 * rivalInQuestion] == 0 ||
                            !outOfLimits(y + 2 * rivalInQuestion, x - 2 * rivalInQuestion) && this.state[y + rivalInQuestion][x - rivalInQuestion] == rivalInQuestion * (-1) && this.state[y + 2 * rivalInQuestion][x - 2 * rivalInQuestion] == 0) {
                        l.add(new Pair<>(y, x));
                    }
                }
            }
        return l;
    }
*/


    public boolean outOfLimits(int x, int y) {
        return x > 7 || x < 0 || y > 7 || y < 0;
    }

    public ComputerSide(int[][] prevM, int y, int x, int newY, int newX, int newRivalInQuestion){
        rivalInQuestion = side;
        state = new int[8][8];
        for (int i = 0; i < 8; i++)
            System.arraycopy(prevM[i], 0, state[i], 0, 8);
        int dirX = newX-x;
        int delta = Math.abs(dirX);
        dirX /= delta;
        int dirY = (newY-y)/delta;
        state[newY][newX] = state[y][x];
        for (;delta>0;delta--){
            state[newY-delta*dirY][newX-delta*dirX] = 0;
        }
    }

    public List<Pair<Integer, Integer>> minimax(int depth/*, boolean isKing, ComputerSide state*/) {
        List<Pair<Integer, Integer>> l = new ArrayList<>();
        if (depth == 0) {
        }
        if (!fightIsPossibleForColor()) {
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    int checker = state[y][x];
                    if (checker == 0 || !isFriend(checker)) continue;
                    if (Math.abs(checker) == 1) {
                        if (!outOfLimits(y + checker, x + checker) && state[y + checker][x + checker] == 0) {
                            ComputerSide newState = new ComputerSide(state, y, x, y+checker, x+checker, rivalInQuestion*(-1));
                            newState.minimax(depth-1);
                        }
                        if (!outOfLimits(y + checker, x - checker) && state[y + checker][x - checker] == 0) {
                            ComputerSide newState = new ComputerSide(state, y, x, y+checker, x-checker, rivalInQuestion*(-1));
                            newState.minimax(depth-1);
                        }
                    } else {
                        for (int i = -1; i < 2; i += 2)
                            for (int j = -1; j < 2; j += 2) {
                                //if (killOrSignX * (-1) == i && killOrSignY * (-1) == j) continue;
                                for (int n = 1; !outOfLimits(x + n * i, y + n * j) ; n++) {
                                    if (state[y][x] != 0) break;
                                    ComputerSide newState = new ComputerSide(state, y, x, y + n * j, x + n * i, rivalInQuestion*(-1));
                                    newState.minimax(depth-1);
                                }

                            }
                        for (int k = -1; !outOfLimits(y + k, x + k); k--) {
                            if (state[y][x] != 0) break;
                            minimax(depth-1);
                        }
                        for (int k = -1; !outOfLimits(y - k, x + k); k--) {
                            if (state[y][x] != 0) break;
                            minimax(depth-1);
                        }
                        for (int k = -1; !outOfLimits(y + k, x - k); k--) {
                            if (state[y][x] != 0) break;
                            minimax(depth-1);
                        }
                        for (int k = -1; !outOfLimits(y - k, x - k); k--) {
                            if (state[y][x] != 0) break;
                            minimax(depth-1);
                        }
                    }
                }
            }
        } else {
            for (Pair<Integer, Integer> p : checkersThatCanBeat) {
                int y = p.getKey();
                int x = p.getValue();
                int checker = state[y][x];
                List<Pair<Integer, Integer>> deadList = new ArrayList<>();
                if (Math.abs(checker) == 1) {
                    if (!outOfLimits(y - 2 * checker, x - 2 * checker) && state[y - checker][x - checker] == checker * (-1) && state[y - 2 * checker][x - 2 * checker] == 0) {
                        int[][] m = new int[8][8];
                        for (int i = 0; i < 8; i++)
                            System.arraycopy(state[i], 0, m[i], 0, 8);
                        m[y][x] = 0;
                        m[y - checker][x - checker] = 0;
                        m[y - 2 * checker][x - 2 * checker] = checker;
                        deadList.add(new Pair<>(y - checker, x - checker));
                        while ()
                        new ComputerSide(state,y,x,y - 2 * checker, x - 2 * checker,deadList, rivalInQuestion, true);
                        minimax(depth-1);
                    }
                    if (!outOfLimits(y + 2 * checker, x - 2 * checker) && state[y + checker][x - checker] == checker * (-1) && state[y + 2 * checker][x - 2 * checker] == 0) {
                        minimax(depth-1);
                    }
                    if (!outOfLimits(y - 2 * checker, x + 2 * checker) && state[y - checker][x + checker] == checker * (-1) && state[y - 2 * checker][x + 2 * checker] == 0) {
                        minimax(depth-1);
                    }
                    if (!outOfLimits(y + 2 * checker, x + 2 * checker) && state[y + checker][x + checker] == checker * (-1) && state[y + 2 * checker][x + 2 * checker] == 0) {
                        minimax(depth-1);
                    }
                } else {
                    for (int k = -1;!outOfLimits(y + k, x + k); k--){
                        if (state[y+k][x+k] == 0){
                            minimax(depth-1);
                        } else if (isFriend(state[y+k][x+k])){
                            break;
                        }else {
                            while (!outOfLimits(y + k -1, x + k -1)&& isEnemy(state[y + k -1][x + k -1])){
                                k--;
                            }
                            if (state[y + k -1][x + k -1] == 0){
                                k--;
                                minimax(depth-1);
                            } else break;
                        }
                    }
                    for (int k = -1;!outOfLimits(y - k, x + k); k--){
                        if (state[y-k][x+k] == 0){
                            minimax(depth-1);
                        } else if (isFriend(state[y-k][x+k])){
                            break;
                        }else {
                            while (!outOfLimits(y - k +1, x + k -1)&& isEnemy(state[y - k +1][x + k -1])){
                                k--;
                            }
                            if (state[y - k +1][x + k -1] == 0){
                                k--;
                                minimax(depth-1);
                            } else break;
                        }
                    }
                    for (int k = -1;!outOfLimits(y + k, x - k); k--){
                        if (state[y+k][x-k] == 0){
                            minimax(depth-1);
                        } else if (isFriend(state[y+k][x-k])){
                            break;
                        }else {
                            while (!outOfLimits(y + k -1, x - k +1)&& isEnemy(state[y + k -1][x - k +1])){
                                k--;
                            }
                            if (state[y + k -1][x - k +1] == 0){
                                k--;
                                minimax(depth-1);
                            } else break;
                        }
                    }
                    for (int k = -1;!outOfLimits(y - k, x - k); k--){
                        if (state[y-k][x-k] == 0){
                            minimax(depth-1);
                        } else if (isFriend(state[y-k][x-k])){
                            break;
                        }else {
                            while (!outOfLimits(y - k +1, x - k +1)&& isEnemy(state[y - k +1][x - k +1])){
                                k--;
                            }
                            if (state[y - k +1][x - k +1] == 0){
                                k--;
                                minimax(depth-1);
                            } else break;
                        }
                    }
                }
            }
        }

        return l;
    }

    public static final int UNREAL_DIR_OR_INDEX = -2;

    public boolean fightIsPossibleForPosition(int x, int y, int killOrSignX, int killOrSignY) {
        boolean r = false;
        if (Math.abs(rivalInQuestion)==1) {
            for (int i = -1; i < 2; i += 2)
                for (int j = -1; j < 2; j += 2) {
                    if (!outOfLimits(x+2*i,y+2*i))
                        r = r || (x + i != killOrSignX || y + j != killOrSignY) && isFriend(state[y + j][x + i])
                            && state[y + j][x + i] == 0;
                }
            return r;
                    /*x < 6 && y < 6 && (x + 1 != killOrSignX || y + 1 != killOrSignY) && isFriend(state[y + 1][x + 1])
                    && state[y + 2][x + 2] == 0||//направление +1 +1*/
        } else {
            for (int i = -1; i < 2; i += 2)
                for (int j = -1; j < 2; j += 2) {
                    if (killOrSignX * (-1) == i && killOrSignY * (-1) == j) continue;
                    for (int n = 1; !outOfLimits(x + n * i, y + n * j); n++) {
                        if (state[y + n * j][x + n * i] != 0) {
                            if (isFriend(state[y + j *n][x + i *n])) break;
                            r = true;
                        } else if (r) return true;
                    }
                    r = false;
                }
        }
        return false;
    }

    public boolean fightIsPossibleForColor(/*CheckerType type*/) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (!isEnemy(state[y][x])) continue;
                if (fightIsPossibleForPosition(/*board[x][y].getChecker().getType(),*/ x, y, UNREAL_DIR_OR_INDEX, UNREAL_DIR_OR_INDEX)) {
                    return true;
                }
            }
        }
        return false;
    }
}
