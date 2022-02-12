package com.nikolai.mazesolver.model;

import java.util.*;

public class MazeSolver {

    public Deque<Cell> solve(Maze maze) {
        Maze mazeToSolve = maze.cloneMaze();
        Cell start = mazeToSolve.getStart();
        List<Cell> neighbors = new ArrayList<>();//создали список для соседей
        neighbors.addAll(mazeToSolve.findNeighbor(start.getX(), start.getY()));//нашли соседей начальной точки и добавили их в список
        Random random = new Random();// для рандома
        Deque<Cell> deque = new ArrayDeque<>();// создали очередь
        deque.addLast(start);// добавили в очередь первое звено лабиринта
        while (true) {
            if (!neighbors.isEmpty()) {//проверяем пустой ли список соседей
                Cell randNei = neighbors.get(random.nextInt(neighbors.size()));//случайно выбираем соседа
                if (randNei.isEnd()) {//если это точка конца, то добавляем конец в очередь и выходим из цикла
                    deque.addLast(randNei);
                    break;
                }
                //если был не конец, то отмечаем точку как посещенную и записываем в верх очереди и очищаем список соседей
                randNei.makeVisited();
                deque.addLast(randNei);
                neighbors.clear();
            } else {//если список соседей пуст, то удалить из очереди и отметить узел, как посещенный
                deque.removeLast().makeVisited();
            }
            Cell lastCall = deque.peekLast();// вернуть верхнюю точку из очереди
            neighbors.addAll(mazeToSolve.findNeighbor(lastCall.getX(), lastCall.getY()));// найти ее соседей
        }
        return deque;
    }

}
