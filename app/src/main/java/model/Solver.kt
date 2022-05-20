package model

import java.util.*
import kotlin.Comparator

class Solver(private val game15: Game15) {

    var solvingPath = mutableListOf<Board>() // Цепочка состояний Board - шаги решения

    fun solver() {
        Thread.sleep(1000)
        // Будем использовать очередь с приоритетом, для нахождения приоритета сравниваем критерии
        // Минимальный критерий -> Максимальный приоритет
        val comparator = Comparator.comparingInt { container: BoardContainer -> criterion(container) }
        val queue: PriorityQueue<BoardContainer> = PriorityQueue(comparator)

        // Начальное поле
        queue.add(BoardContainer(null, Board(game15.tilesField)))

        while (true) {
            val container: BoardContainer = queue.remove()

            if (container.board.checkWin()) {
                insertInPath(BoardContainer(container, container.board))
                break
            }

            for (neighbour in container.board.neighbors()) {
                // Проверка на наличие neighbour в родителях
                if (neighbour != null && !wasInParent(container, neighbour)) {
                    queue.add(BoardContainer(container, neighbour))
                }
            }

        }

        for (i in 0..solvingPath.size - 2) {
            game15.move(solvingPath[i + 1].blankTile.posX, solvingPath[i + 1].blankTile.posY)
            Thread.sleep(100)
        }

    }


    fun insertInPath(container: BoardContainer) {
        var newContainer: BoardContainer? = container

        while (true) {
            newContainer = newContainer!!.parent
            if (newContainer == null) return
            solvingPath.add(0, newContainer.board)
        }

    }

    fun wasInParent(container: BoardContainer?, board: Board): Boolean {
        var checkableContainer = container

        while (true) {
            if (checkableContainer!!.board == board) return true
            checkableContainer = checkableContainer.parent
            if (checkableContainer == null) return false
        }

    }

    fun criterion(container: BoardContainer): Int = container.board.criterion

}

data class BoardContainer(var parent: BoardContainer?, var board: Board)