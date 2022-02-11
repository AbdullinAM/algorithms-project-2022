package com.nikolai.mazesolver.model;

import javafx.util.Pair;

import java.util.*;

public class Maze {
    private final int height;
    private final int width;
    private Cell[][] maze ;
    private Cell start;
    private Cell end;


    public Maze(int height, int width) {
        this.height = height;
        this.width = width;
        this.maze = new Cell[width][height];//создаем массив Cell размер эквивалентным нашим высоте и ширине
        for (int i = 0; i < height; i++) {//заполняем наш массив клетками
            for (int j = 0; j < width; j++) {
                maze[j][i] = new Cell(j, i);
            }
        }
    }

    //Алгоритм Прима
    public void createMaze() {

        //Создаем список для хранения границ
        List<Pair<Cell, Cell>> frontiers = new ArrayList<>();
        //Генерируем случайно координаты нашей стартовой ячейки и помещаем в список
        Random random = new Random();
        int x = random.nextInt(width);
        int y = 1;
        frontiers.add(new Pair<>(new Cell(x, y), new Cell(x, y)));
        //Запоминаем стар, что бы в конце его отметить как старт
        start = maze[x][y];

        while (!frontiers.isEmpty()) {//если в списке есть границы(он не пуст), то заходим в цикл
            Pair<Cell, Cell> pairFron = frontiers.remove(random.nextInt(frontiers.size()));//случайно выбираем пару от 0
            // до размера нашего списка с границами
            x = pairFron.getValue().getX();//Получаем Х нашей клетки(это клетка за нашей клеткой, если это не первая)
            y = pairFron.getValue().getY();//Получаем У нашей клетки(это клетка за нашей клеткой, если это не первая)
            if (maze[x][y].isWall()) {//Проверяем является ли эта клетка стеной, если да то заходим в if
                maze[x][y].makeClear();//Делаем из стены проход
                maze[pairFron.getKey().getX()][pairFron.getKey().getY()].makeClear();//Так же делаем проходом перед нашей клеткой
                frontiers.addAll(findFrontiers(x, y));//Ищем все соседние стены и возвращаем их список
            }
        }
        start.makeStart();//Тут мы делаем из начальной клетки стартовую
        while (true){//Тут мы делаем из рандомной боковой клетки конечную
            x = random.nextInt(width);
            y = height - 1;
            List<Cell> neighbor= findNeighbor(x, y);
            if(!neighbor.isEmpty()){
                System.out.println(1);
                end = maze[x][y];
                end.makeEnd();
                break;
            }

        }
    }

    public List<Cell> findNeighbor(int x, int y) {
        List<Cell> neighbors = new ArrayList<>();
        //проверяем соседей являются они проходом или выходом
        if (x >= 1 && (maze[x - 1][y].isClear() || maze[x - 1][y].isEnd()))
            neighbors.add(maze[x - 1][y]);
        if (y >= 1 && (maze[x][y - 1].isClear() || maze[x][y - 1].isEnd()))
            neighbors.add(maze[x][y - 1]);
        if (x < width - 1 && (maze[x + 1][y].isClear() || maze[x + 1][y].isEnd()))
            neighbors.add(maze[x + 1][y]);
        if (y < height - 1 && (maze[x][y + 1].isClear() || maze[x][y + 1].isEnd()))
            neighbors.add(maze[x][y + 1]);

        return neighbors;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Cell[][] getMaze() {
        return maze;
    }

    public void setMaze(Cell[][] maze) {
        this.maze = maze;
    }

    public Cell getStart() {
        return start;
    }

    public void setStart(Cell start) {
        this.start = start;
    }

    public Cell getEnd() {
        return end;
    }

    public void setEnd(Cell end) {
        this.end = end;
    }

    private List<Pair<Cell, Cell>> findFrontiers(int x, int y) {
        List<Pair<Cell, Cell>> frontiers = new ArrayList<>();

        if (x >= 2 && maze[x - 2][y].isWall())
            frontiers.add(new Pair<>(new Cell(x - 1, y), new Cell(x - 2, y)));
        if (y >= 2 && maze[x][y - 2].isWall())
            frontiers.add(new Pair<>(new Cell(x, y - 1), new Cell(x, y - 2)));
        if (x < width - 2 && maze[x + 2][y].isWall())
            frontiers.add(new Pair<>(new Cell(x + 1, y), new Cell(x + 2, y)));
        if (y < height - 2 && maze[x][y + 2].isWall())
            frontiers.add(new Pair<>(new Cell(x, y + 1), new Cell(x, y + 2)));

        return frontiers;
    }

    public Maze cloneMaze() {
        Maze mazeToClone = new Maze(height, width);
        for (int i = 0; i < height; i++) {//заполняем наш массив клетками
            for (int j = 0; j < width; j++) {
                System.out.println(mazeToClone.getMaze()[j][i]);
                mazeToClone.getMaze()[j][i] = new Cell(j, i);
                mazeToClone.getMaze()[j][i].setValue(maze[j][i].getValue());
            }
        }
        mazeToClone.setStart(start);
        mazeToClone.setEnd(end);
        return mazeToClone;
    }

    public void addPath(Deque<Cell> path) {
        for (Cell it : path) {
            int x = it.getX();
            int y = it.getY();
            maze[x][y].setValue(it.getValue());
        }
    }

}
