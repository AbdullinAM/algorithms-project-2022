import java.util.List;
import java.util.Objects;

public class CellGroup {
    private final List<Coord> cellList;
    private int numOfMines;

    public CellGroup(List<Coord> cells, int numOfMines) {
        this.cellList = cells;
        this.numOfMines = numOfMines;
    }

    public int size() {
        return cellList.size();
    }

    public List<Coord> getCellList() {
        return cellList;
    }


    public int getNumOfMines() {
        return numOfMines;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellGroup cellGroup = (CellGroup) o;
        return numOfMines == cellGroup.numOfMines &&
                cellList.containsAll(cellGroup.cellList) &&
                    cellList.size() == cellGroup.cellList.size();
    }

    @Override
    public int hashCode() {
        return Objects.hash(cellList, numOfMines);
    }

    public void subtraction(CellGroup child) {
        for (Coord coord : child.cellList) {
            this.cellList.remove(coord);
        }
        this.numOfMines = this.numOfMines - child.getNumOfMines();
    }


    @Override
    public String toString() {
        return "CellGroup{" +
                "cellList=" + cellList +
                ", numOfMines=" + numOfMines +
                '}' + "\n";
    }

}
