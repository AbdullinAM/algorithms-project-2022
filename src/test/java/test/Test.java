package test;

import com.nikolai.mazesolver.model.Cell;
import com.nikolai.mazesolver.model.Maze;
import com.nikolai.mazesolver.model.MazeSolver;

import java.util.Deque;

import static org.junit.Assert.*;

public class Test {
    private int width=50;
    private int height=40;


    @org.junit.Test
    public void mazeSize(){//проверка, что лабиринт генерируется правильных размеров
        Maze maze = new Maze(height,width);
        for (int i=0;i<1000;i++) {
            maze.createMaze();
            assertEquals(width, maze.getWidth());
            assertEquals(width, maze.getWidth());
        }
    }

    /*@org.junit.Test
    public void solve(){//проверка, что лабиринт имеет решение
        Maze maze = new Maze(height,width);
        MazeSolver mazeSolver= new MazeSolver();
        for (int i=0;i<1000;i++){
            System.out.println(i);
            maze.createMaze();
            Deque<Cell> cellDeque = mazeSolver.solve(maze);

            assertEquals(maze.getStart(),cellDeque.peekFirst());//проверка начальной клетки в очереди
            assertEquals(maze.getEnd(),cellDeque.peekLast());//проверка последней клетки в очереди
        }
    }*/

    @org.junit.Test
    public void cloneMaze(){//проверка правильности клонирования лабиринта
        Maze maze = new Maze(height,width);
        for (int x=0;x<1000;x++) {
            maze.createMaze();
            Maze cloneMaze = maze.cloneMaze();
            for (int i = 0; i < height; i++) {//заполняем наш массив клетками
                for (int j = 0; j < width; j++) {
                    assertEquals(maze.getMaze()[j][i], cloneMaze.getMaze()[j][i]);//проверка, что ячейки эквиваленты
                }
            }
        }
    }

    /*@org.junit.Test
    public void connectedMaze(){//проверка, что лабиринт связный
        //будем проходить по всем точкам и делать каждую точку новым концом
        Maze maze = new Maze(height,width);
        maze.createMaze();
        MazeSolver mazeSolver= new MazeSolver();
        maze.getEnd().makeClear();
        for (int i = 0; i < height; i++) {//заполняем наш массив клетками
            for (int j = 0; j < width; j++) {
                Cell cell = maze.getMaze()[j][i];
                if(cell.getValue()==0){
                    Maze cloneMaze= maze.cloneMaze();
                    Cell cloneCel=cloneMaze.getMaze()[j][i];

                    cloneMaze.setEnd(cloneCel);
                    cloneCel.makeEnd();
                    assertEquals(cloneCel,cloneMaze.getMaze()[j][i]);
                   assertFalse(mazeSolver.solve(cloneMaze).isEmpty());//проверка, что путь не пуст
                }

            }
        }
    }*/
}
