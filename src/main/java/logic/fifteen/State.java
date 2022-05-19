package logic.fifteen;
import java.util.HashSet;
import java.util.Set;

public class State {

    public int[][] blocks;
    private int zeroX;
    private int zeroY;
    private int h;

    public State(int[][] blocks) {

        this.blocks = saveOriginal(blocks);
        h = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                if (blocks[i][j] != (i*dimension() + j + 1) && blocks[i][j] != 0) {
                    h += 1;
                }
                if (blocks[i][j] == 0) {
                    zeroX = (int) i;
                    zeroY = (int) j;
                }
            }
        }
    }


    public int dimension() {
        return blocks.length;
    }

    public int h() {
        return h;
    }

    public boolean isGoal() {  //   если все на своем месте, значит это искомая позиция
        return h == 0;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State State = (State) o;

        if (State.dimension() != dimension()) return false;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                if (blocks[i][j] != State.blocks[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public Iterable<State> neighbors() {
        Set<State> boardList = new HashSet<State>();
        boardList.add(changes(getNewBlock(), zeroX, zeroY, zeroX, zeroY + 1));
        boardList.add(changes(getNewBlock(), zeroX, zeroY, zeroX, zeroY - 1));
        boardList.add(changes(getNewBlock(), zeroX, zeroY, zeroX - 1, zeroY));
        boardList.add(changes(getNewBlock(), zeroX, zeroY, zeroX + 1, zeroY));

        return boardList;
    }

    private int[][] getNewBlock() { //  опять же, для неизменяемости
        return saveOriginal(blocks);
    }

    private State changes(int[][] blocks2, int x1, int y1, int x2, int y2) {  //  в этом методе меняем два соседних поля

        if (x2 > -1 && x2 < dimension() && y2 > -1 && y2 < dimension()) {
            int t = blocks2[x2][y2];
            blocks2[x2][y2] = blocks2[x1][y1];
            blocks2[x1][y1] = t;
            return new State(blocks2);
        } else
            return null;

    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int[] block : blocks) {
            for (int j = 0; j < blocks.length; j++) {
                s.append(String.format("%2d ", block[j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private static int[][] saveOriginal(int[][] original) {
        if (original == null) {
            return null;
        }

        final int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = new int[original[i].length];
            for (int j = 0; j < original[i].length; j++) {
                result[i][j] = original[i][j];
            }
        }
        return result;
    }
}
