package app.game15.logic.fifteen;
import java.util.ArrayList;

public class State {

    public int[][] blocks;
    private int zeroRow;
    private int zeroColumn;
    private int h;

    public State getParent() {
        return parent;
    }

    private final State parent;

    public int[][] getBlocks(){
        return blocks;
    }



    public State(int[][] blocks, State parent) {
        this.parent = parent;
        this.blocks = blocks;
        h = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                if (blocks[i][j] != (i*dimension() + j + 1) && blocks[i][j] != 0) {
                    h += 1;
                }
                if (blocks[i][j] == 0) {
                    zeroRow = i;
                    zeroColumn = j;
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

    public ArrayList<State> getChildren() {
        return children;
    }

    private ArrayList<State> children;

    public void setChildren() {
        ArrayList<State> boardList = new ArrayList<>();
        boardList.add(changes(getInitialBlock(), zeroRow, zeroColumn, zeroRow, zeroColumn + 1));
        boardList.add(changes(getInitialBlock(), zeroRow, zeroColumn, zeroRow, zeroColumn - 1));
        boardList.add(changes(getInitialBlock(), zeroRow, zeroColumn, zeroRow - 1, zeroColumn));
        boardList.add(changes(getInitialBlock(), zeroRow, zeroColumn, zeroRow + 1, zeroColumn));
        children = boardList;
    }

    private int[][] getInitialBlock() {
        return saveOriginal(blocks);
    }

    private State changes(int[][] blocks, int zeroX, int zeroY, int neighborX, int neighborY) {

        if (neighborX > -1 && neighborX < dimension() && neighborY > -1 && neighborY < dimension()) {
            int t = blocks[neighborX][neighborY];
            blocks[neighborX][neighborY] = blocks[zeroX][zeroY];
            blocks[zeroX][zeroY] = t;
            State s = new State(blocks, this);
            int[][] ts = s.blocks;
            return s;
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
            System.arraycopy(original[i], 0, result[i], 0, original[i].length);
        }
        return result;
    }
}
