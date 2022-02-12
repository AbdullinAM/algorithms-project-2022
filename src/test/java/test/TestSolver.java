package test;

import com.nikolai.mazesolver.model.Cell;
import com.nikolai.mazesolver.model.Maze;
import com.nikolai.mazesolver.model.MazeSolver;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TestSolver {

    private final int width = 50;
    private final int height = 40;


    @Test
    public void mazeSize() {//проверка, что лабиринт генерируется правильных размеров
        Maze maze = new Maze(height, width);

        for (int i = 0; i < 1000; i++) {
            maze.createMaze();
            assertEquals(width, maze.getWidth());
            assertEquals(width, maze.getWidth());
        }

        assertThrows(NumberFormatException.class, () -> {
            Maze maze1 = new Maze(3, width);//проверка когда одна из границ неправильная
        });
        assertThrows(NumberFormatException.class, () -> {
            Maze maze1 = new Maze(height, 3);//проверка когда одна из границ неправильная
        });
        assertThrows(NumberFormatException.class, () -> {
            Maze maze1 = new Maze(3, 3);//проверка на неправильные границы
        });
    }

    @Test
    public void solve() {//проверка, что лабиринт имеет решение
        MazeSolver mazeSolver = new MazeSolver();
        for (int i = 0; i < 1000; i++) {
            Maze maze = new Maze(height, width);

            maze.createMaze();
            Deque<Cell> cellDeque = mazeSolver.solve(maze);
            List<Cell> pathCalls = new ArrayList<>();
            maze.addPath(cellDeque);
            pathCalls.add(maze.getStart());
            pathCalls.add(maze.getEnd());
            for (int x = 0; x < height; x++) {
                for (int j = 0; j < width; j++) {
                    Cell cell = maze.getMaze()[j][x];
                    if (cell.isVisited()) {
                        pathCalls.add(cell);
                    }
                }
            }
            //Суть проверки, что если путь найденный нами перебором всех клеток, эти клетки содержатся
            //в найденному нами с помощью метода, размеры очереди и списка найденного пути равны, а так же очередь содержит
            //начальную и конечную клетку в правильных местах(старт в самом начале очереди, а конец в конце), то можно
            //сделать вывод, что весь путь найден корректно.
            for (Cell it : cellDeque) {
                assertTrue(pathCalls.contains(it));//проверка всего пути
            }
            assertEquals(maze.getStart(), cellDeque.peekFirst());//проверка начальной клетки в очереди
            assertEquals(maze.getEnd(), cellDeque.peekLast());//проверка последней клетки в очереди
            assertEquals(cellDeque.size(), pathCalls.size());//проверка, что размер пути одинаков
        }
    }

    @Test
    public void cloneMaze() {//проверка правильности клонирования лабиринта
        Maze maze = new Maze(height, width);
        for (int x = 0; x < 1000; x++) {
            maze.createMaze();
            Maze cloneMaze = maze.cloneMaze();
            for (int i = 0; i < height; i++) {//заполняем наш массив клетками
                for (int j = 0; j < width; j++) {
                    assertEquals(maze.getMaze()[j][i], cloneMaze.getMaze()[j][i]);//проверка, что ячейки эквиваленты
                }
            }
        }
    }

    @Test
    public void connectedMaze() {//проверка, что лабиринт связный
        //будем проходить по всем точкам и делать каждую точку новым концом
        MazeSolver mazeSolver = new MazeSolver();
        //Обоснование связности(что в лабиринте нет недостижимых островов), если от начальной точки можно дойти до любой
        //другой, то можно сделать вывод, что в лабиринте нет островов и любая точка достижима.
        for (int x = 0; x < 300; x++) {
            Maze maze = new Maze(height, width);
            maze.createMaze();
            maze.getEnd().makeClear();
            for (int i = 0; i < height; i++) {//заполняем наш массив клетками
                for (int j = 0; j < width; j++) {
                    Cell cell = maze.getMaze()[j][i];
                    if (cell.getValue() == 0) {
                        Maze cloneMaze = maze.cloneMaze();
                        Cell cloneCel = cloneMaze.getMaze()[j][i];

                        cloneMaze.setEnd(cloneCel);
                        cloneCel.makeEnd();
                        assertEquals(cloneCel, cloneMaze.getMaze()[j][i]);
                        assertFalse(mazeSolver.solve(cloneMaze).isEmpty());//проверка, что путь не пуст
                    }
                }
            }
        }
    }

    @Test
    public void setCell() {//проверка, что значение в клетке меняется
        Cell cell = new Cell(5, 6);
        assertTrue(cell.isWall());//проверка, что начальное значение верное(то есть стена)
        cell.makeClear();
        assertTrue(cell.isClear());//проверка, что проход делается
        cell.makeEnd();
        assertTrue(cell.isEnd());//проверка, что конец делается
        cell.makeStart();
        assertEquals(2, cell.getValue());//проверка, что старт делается
        cell.makeVisited();
        assertTrue(cell.isVisited());//проверка, что клетка отмечается как посещенная
    }

    @Test
    public void addPath() {//проверка, что значение в клетке меняется
        MazeSolver mazeSolver = new MazeSolver();
        for (int x = 0; x < 1000; x++) {
            Maze maze = new Maze(height, width);

            maze.createMaze();
            Deque<Cell> cellDeque = mazeSolver.solve(maze);
            List<Cell> pathCalls = new ArrayList<>();
            maze.addPath(cellDeque);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Cell cell = maze.getMaze()[j][i];
                    if (cell.isVisited()) {
                        pathCalls.add(cell);
                    }
                }
            }
            assertEquals(cellDeque.size(), pathCalls.size() + 2);
        }
    }

}
