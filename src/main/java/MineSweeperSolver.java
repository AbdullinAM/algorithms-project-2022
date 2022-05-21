import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineSweeperSolver {
    private Cell[][] visibleBoard;
    private final int cols;
    private final int rows;

    public MineSweeperSolver(Cell[][] visibleBoard, Coord size) {
        this.visibleBoard = visibleBoard;
        this.cols = size.getX();
        this.rows = size.getY();
    }

    public void setVisibleBoard(Cell[][] visibleBoard) {
        this.visibleBoard = visibleBoard;
    }

    public List<CellGroup> solve() {
        List<CellGroup> groups = createGroups();

        List<CellGroup> solution = solveGroups(groups);
        System.out.println("Optimised groups: \n" + solution);
        System.out.println("Group size: " + solution.size());
        if (solution.size() == 0) {
            groups = createGroups();
            solution = guessingByPossibilities(groups);
            System.out.println("Guessing");
        }
        return solution;
    }


    private List<CellGroup> checkSolutions(List<CellGroup> groups) {
        List<CellGroup> solutions = new ArrayList<>();
        for (CellGroup group: groups) {
            if (group.getCellList().size() == group.getNumOfMines() || group.getNumOfMines() <= 0) {
                solutions.add(group);
            }
        }
        return solutions;
    }
    //create unique groups
    private List<CellGroup> createGroups() {
        List<CellGroup> allGroups = new ArrayList<>();
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (isNumGreaterThatZero(i, j)) {
                    List<Coord> group = new ArrayList<>();
                     List<Coord> neighbours = neighbours(i, j);
                     int flagsAmount = 0;
                     for (Coord neighbor : neighbours) {
                         int x = neighbor.getX();
                         int y = neighbor.getY();
                         if (visibleBoard[x][y] != Cell.FLAGGED) {
                             group.add(neighbor);
                         } else
                             flagsAmount++;
                     }
                     CellGroup grp = new CellGroup(group, intByCell(visibleBoard[i][j]) - flagsAmount);
                    if (grp.size() != 0 && !allGroups.contains(grp)) {
                        allGroups.add(grp);
                    }
                }
            }
        }
        return allGroups;
    }

    /**
     * Создает список групп ячеек, связанных одним значением открытого поля, а также разбивает их на более мелкие, удаляет повторяющиеся.
     */
    private List<CellGroup> solveGroups(List<CellGroup> groups) {
        //System.out.println("Input groups: \n" + groups);
        List<CellGroup> solutions;
        boolean repeat;
        do {
            repeat = false;
            for (int i = 0; i < groups.size() - 1; i++) {
                for (int j = i + 1; j < groups.size(); j++) {
                    CellGroup groupI = groups.get(i);
                    CellGroup groupJ = groups.get(j);

                    //delete duplicates
                    if (groupI.equals(groupJ)) {
                        groups.remove(j);
                        break;
                    }

                    CellGroup parent;
                    CellGroup child;
                    int parentIndex;
                    if (groupI.size() > groupJ.size()) {
                        parent = groupI;
                        child = groupJ;
                        parentIndex = i;
                    } else {
                        child = groupI;
                        parent = groupJ;
                        parentIndex = j;
                    }
                    if (parent.getCellList().containsAll(child.getCellList())) {
                        parent.subtraction(child);
                        groups.set(parentIndex, parent);
                        repeat = true;
                    }
                    solutions = checkSolutions(groups);
                    if (solutions.size() != 0) {
                        //System.out.println("Parent: " + parent + " Child: " + child);
                        return solutions;
                    }
                    /*else if (groupI.overlaps(groupJ) > 0) {    // иначе если группы пересекаются
                        if (groupI.getNumOfMines() > groupJ.getNumOfMines()) {
                            parent = groupI;
                            child = groupJ;
                        } else {
                            child = groupI;
                            parent = groupJ;
                        }
                        CellGroup overlap = parent.getOverlap(child);// то берем результат пересечения
                        if (overlap != null) {                  //  и если он имеет смысл (в результате пересечения выявились ячейки с 0% или 100%)
                            groups.add(overlap);                //  то вносим соответствующие коррективы в список
                            parent.subtraction(overlap);
                            child.subtraction(overlap);
                            repeat = true;
                        }
                    }*/
                }
            }
        }
        while(repeat);
        solutions = checkSolutions(groups);
        return solutions;
    }

    private List<CellGroup> guessingByPossibilities(List<CellGroup> groups) {
        Map<Coord, Double> cells = new HashMap<>();
        for (CellGroup group : groups) {
            for (Coord coord: group.getCellList()) {
                Double value;
                if ((value = cells.get(coord)) == null)
                    cells.put(coord,(double) group.getNumOfMines() / group.size());
                else
                    //A = 1 - (1 - A1) * (1 - A2) * ... * (1 - An)
                    cells.put(coord,(double) 1 - (1 - value) * (1 - (double) group.getNumOfMines() / group.size()));
            }
        }
        //correct by NumOfMines in group
        boolean repeat;
        do {
            repeat = false;
            for (CellGroup group : groups) {
                List<Double> prob = getProbabilities(group, cells);
                Double sum = 0.0;
                for (Double elem:prob) sum += elem;
                int mines = group.getNumOfMines() * 100;
                if (Math.abs(sum-mines) > 1) {
                    repeat = true;
                    Double correction = mines / sum;
                    for (Coord coord: group.getCellList()) {
                        cells.put(coord, correction * cells.get(coord));
                    }
                }
            }
        }
        while (repeat);

        List<CellGroup> solution = new ArrayList<>();
        Coord coord = null;
        Double min = 99.0;
        for (Map.Entry<Coord, Double> entry : cells.entrySet()) {
            if (entry.getValue() < min) {
                min = entry.getValue();
                coord = entry.getKey();
            }
        }
        if (coord != null)
            solution.add(new CellGroup(List.of(coord), 0));
        return solution;
    }

    private List<Double> getProbabilities(CellGroup group, Map<Coord, Double> cells) {
        List<Double> prob = new ArrayList<>();
        group.getCellList().forEach(e -> prob.add(cells.get(e) * 100));
        return prob;
    }

    private boolean isNumGreaterThatZero(int i, int j) {
        return visibleBoard[i][j] != Cell.FLAGGED &&
                visibleBoard[i][j] != Cell.CLOSED &&
                visibleBoard[i][j] != Cell.NUM0;
    }

    private int intByCell(Cell cell) {
        return cell.ordinal();
    }

    private List<Coord> neighbours(int x, int y) {
        List<Coord> list = new ArrayList<>();
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (!inRange(i, j)) continue;
                if (visibleBoard[i][j] == Cell.CLOSED || visibleBoard[i][j] == Cell.FLAGGED) {
                    list.add(new Coord(i, j));
                }
            }
        }
        return list;
    }

    private boolean inRange(int x, int y) {
        return x >= 0 && x < cols && y >= 0 && y < rows;
    }
}
