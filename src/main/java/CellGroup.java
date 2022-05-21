import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CellGroup {
    private List<Coord> cellList;
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

    public void setCellList(List<Coord> cellList) {
        this.cellList = cellList;
    }

    public int getNumOfMines() {
        return numOfMines;
    }

    public void setNumOfMines(int numOfMines) {
        this.numOfMines = numOfMines;
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

    public int overlaps(CellGroup group) {
        int k = 0;
        for (Coord coord: group.cellList) {
            if (this.cellList.contains(coord))
                k++;
        }
        return k;
    }

    @Override
    public String toString() {
        return "CellGroup{" +
                "cellList=" + cellList +
                ", numOfMines=" + numOfMines +
                '}' + "\n";
    }

    public CellGroup getOverlap(CellGroup child) {
        List<Coord> list = new ArrayList<>();
        for (Coord coord: child.cellList) {
            if (this.cellList.contains(coord)) {
                list.add(coord);
            }
        }
        int mines = this.numOfMines - (child.cellList.size() - list.size());
        return new CellGroup(list, mines);
    }
}
