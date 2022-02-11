package com.nikolai.mazesolver.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MazeTest {
    private int width = 50;
    private int height = 50;


    @Test
    void createMaze() {
        Maze maze = new Maze(height, width);
        maze.createMaze();
        assertEquals(width, maze.getWidth());//проверка, что лабиринт создан с правильной width
        assertEquals(height, maze.getHeight());//проверка, что лабиринт создан с правильным height

    }

    @Test
    void cloneMaze() {// проверка, что лабиринты клонируются правильно
        Maze maze = new Maze(height, width);
        Maze cloneMaze = maze.cloneMaze();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                assertEquals(maze.getMaze()[j][i], cloneMaze.getMaze()[j][i]);
            }
        }
    }

    @Test
    void solve() {//проверка, что лабиринт имеет решение
        Maze maze = new Maze(height, width);
        maze.createMaze();
        MazeSolver mazeSolver=new MazeSolver();
        assertFalse(mazeSolver.solve(maze).isEmpty());// если очередь пути не пуста, то значит он найден
    }


    @Test
    void addPath() {
        Maze maze = new Maze(height, width);
        maze.createMaze();
        MazeSolver mazeSolver=new MazeSolver();
        Deque<Cell> path = mazeSolver.solve(maze);
        List<Cell> cellList = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = maze.getMaze()[j][i];
                if(cell.getValue()==-1){
                    cellList.add(cell);
                }
            }
        }
        assertEquals(0,cellList.size());//проверка, что путь еще не добавлен
        maze.addPath(path);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = maze.getMaze()[j][i];
                if(cell.getValue()==-1){
                    cellList.add(cell);
                }
            }
        }
        assertEquals(path.size(),cellList.size()+2);//проверка, что путь добавлен (+2 потому есть start и end)
        assertEquals(maze.getStart(),path.peekFirst());//проверка, что первая клетка в очереди это начало пути
        assertEquals(maze.getEnd(),path.peekLast());//проверка, что первая клетка в очереди это конец пути
    }

    //Проверка лабиринта на связность, она заключается в том что мы проверяем, что каждая клетка достижима из стартовой клетки
    @Test
    void connectedGraph() {
        Maze maze = new Maze(height, width);
        maze.createMaze();
        maze.getEnd().setValue(0);//меняем сгенерированный конец на проход
        MazeSolver mazeSolver=new MazeSolver();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = maze.getMaze()[j][i];
                if(cell.getValue()==0){
                    Maze cloneMaze = maze.cloneMaze();//клонируем лабиринт
                    Cell cloneCell = cloneMaze.getMaze()[j][i];
                    cloneCell.makeEnd();//делаем ячейку новым выходом
                    cloneMaze.setEnd(cloneCell);//делаем в клонированном лабиринте новый выход
                    Deque<Cell> path = mazeSolver.solve(cloneMaze);//сохраняем наш путь
                    assertFalse(path.isEmpty());//проверяем что путь не пуст(если список окажется пуст, то пути найдено не было)
                }
            }
        }
    }

}