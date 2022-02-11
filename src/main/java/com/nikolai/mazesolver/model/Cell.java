package com.nikolai.mazesolver.model;

import java.util.Objects;

public class Cell {
    private final int x;
    private final int y;
    private int value = 1; //по нашему алгоритму у нас изначально все это стены
    //private int priority;


    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void makeClear() {//проход равен 0
        value = 0;
    }

    public boolean isClear() {//это проход или нет
        if (value == 0) {
            return true;
        }
        return false;
    }

    public void makeWall() {//стена равна 1
        value = 1;
    }

    public void makeVisited() {//отметить что мы там были
        value = -1;
    }

    public boolean isWall() {//это стена или нет
        if (value == 1) {
            return true;
        }
        return false;
    }

    public void makeStart() {//начало равно 2
        value = 2;
    }

    public void makeEnd() {//конец равен 3
        value = 3;
    }
    public boolean isEnd() {//это конец или нет
        if (value == 3) {
            return true;
        }
        return false;
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
