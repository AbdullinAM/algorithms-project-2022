package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import static sample.CheckersApp.CELL_SIZE;

public class Cell extends Rectangle {

    private Checker checker;

    public boolean hasChecker() {
        return checker != null;
    }

    public Checker getChecker() {
        return checker;
    }

    public void setChecker(Checker checker) {
        this.checker = checker;
    }

    public Cell(boolean white, int x, int y) {
        setWidth(CELL_SIZE);
        setHeight(CELL_SIZE);
        relocate(x * CELL_SIZE, y * CELL_SIZE);
        setFill(white ? Color.WHITE : Color.GREY);
    }
}