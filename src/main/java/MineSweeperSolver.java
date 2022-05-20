public class MineSweeperSolver {
    private Cell[][] visibleBoard;
    private int bombsAmount;

    private MineSweeperSolver(Cell[][] visibleBoard, int bombsAmount) {
        this.visibleBoard = visibleBoard;
        this.bombsAmount = bombsAmount;
    }

}
