package com.nikolai.mazesolver.model;

import java.util.Objects;

public class Cell {
    private final int x;
    private final int y;
    private int value = 1; //по нашему алгоритму у нас изначально все это стены

    private enum CellType {
        VISITED(-1),
        SPACE(0),
        WALL(1),
        START(2),
        END(3);

        private final int code;

        CellType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        @Override
        public String toString() {
            return String.valueOf(code);
        }
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void makeClear() {//проход равен 0
        value = CellType.SPACE.code;
    }

    public boolean isClear() {//это проход или нет
        return value == CellType.SPACE.code;
    }

    public void makeVisited() {//отметить что мы там были
        value = CellType.VISITED.code;
    }

    public boolean isVisited() {
        return  value == CellType.VISITED.code;
    }

    public boolean isWall() {//это стена или нет
        return value == CellType.WALL.code;
    }

    public void makeStart() {//начало равно 2
        value = CellType.START.code;
    }

    public void makeEnd() {//конец равен 3
        value = CellType.END.code;
    }

    public boolean isEnd() {//это конец или нет
        return value == CellType.END.code;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x && y == cell.y && value == cell.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, value);
    }

    @Override
    public String toString() {
        return "[" +
                "x=" + x +
                ", y=" + y +
                ", value=" + value +
                ']';
    }
}
