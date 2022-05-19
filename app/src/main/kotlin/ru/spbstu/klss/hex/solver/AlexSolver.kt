package ru.spbstu.klss.hex.solver

import ru.spbstu.klss.hex.model.Cell
import ru.spbstu.klss.hex.model.Color
import ru.spbstu.klss.hex.model.Model
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max

class AlexSolver(color: Color) : Solver{

    val selfColor = color
    val enemyColor = if (selfColor == Color.RED) Color.BLUE else Color.RED
    val currentBoard: MutableList<Cell> = Model().board
    val moveOrder = mutableListOf<Pair<Int, Int>>()

    val bridgeScore = 2
    val savedBridgeScore = 3
    val sizeMultiplier = 2
    val cellCountMultiplier = 1

    override fun action(board: MutableList<Cell>): Pair<Int, Int> { // returns X and Y
        TODO()
    }

    fun countScore(board: MutableList<Cell>, color: Color): Int {
        var score = 0
        score += countBridges(board, color) * bridgeScore
        score += countMaxLength(board, color) * sizeMultiplier
        score += countSavedBridges(board, color) * savedBridgeScore
        score += getCells(board, color).size * sizeMultiplier
        return score
    }

    fun countBridges(board: MutableList<Cell>, color: Color): Int {
        var bridgeCount = 0
        val currentCellList = getCells(board, color)

        for (i in 0 until currentCellList.size) {
            for (j in i + 1 until currentCellList.size) {
                val cell1 = currentCellList[i]
                val cell2 = currentCellList[j]
                if (isBridge(cell1, cell2) and !isBlockedBridge(cell1, cell2, board, color)) {
                    bridgeCount++
                }
            }
        }
        return bridgeCount
    }


    fun countMaxLength(board: MutableList<Cell>, currentColor: Color): Int {
        val currentCellList = getCells(board, currentColor)
        var minCoord: Int
        var maxCoord: Int
        var maxLenght = 0
        val visited = mutableSetOf<Cell>()

        for (cell in currentCellList) {
            if (cell in visited) continue
            val stack = ArrayDeque<Pair<Cell, MutableIterator<Cell>>>()
            var currentCell = cell
            var currentIterator = currentCell.neighbours.iterator()
            visited.add(currentCell)
            stack.add(Pair(currentCell, currentIterator))

            minCoord = -1
            maxCoord = 11

            while (stack.isNotEmpty()) {
                currentCell = stack.last().first
                currentIterator = stack.last().second
                while (currentIterator.hasNext()) {
                    if (currentColor == Color.BLUE) {
                        minCoord = min(minCoord, currentCell.y)
                        maxCoord = max(maxCoord, currentCell.y)
                    } else if (currentColor == Color.RED) {
                        minCoord = min(minCoord, currentCell.y)
                        maxCoord = max(maxCoord, currentCell.y)
                    }

                    val nextCell = currentIterator.next()
                    if (nextCell.color != currentColor || nextCell in visited) continue

                    visited.add(currentCell)
                    currentCell = nextCell
                    currentIterator = nextCell.neighbours.iterator()
                    stack.add(Pair(currentCell, currentIterator))
                }
                stack.removeLast()
            }

            maxLenght = max(maxLenght, maxCoord - minCoord)
        }
        return maxLenght
    }

    fun countSavedBridges(board: MutableList<Cell>, currentColor: Color): Int {
        var bridgeCount = 0
        val currentCellList = getCells(board, currentColor)

        for (i in 0 until currentCellList.size) {
            for (j in i + 1 until currentCellList.size) {
                val cell1 = currentCellList[i]
                val cell2 = currentCellList[j]
                if (isSavedBridge(cell1, cell2, board)) {
                    bridgeCount++
                }
            }
        }
        return bridgeCount
    }

    fun isBridge(cell1: Cell, cell2: Cell): Boolean {
        if (cell1.color != cell2.color || Color.GRAY in listOf(cell1.color, cell2.color)) return false
        val deltaX = cell1.x - cell2.x
        val deltaY = cell1.y - cell2.y
        val resultMultiplication = deltaX * deltaY
        if (resultMultiplication == 2 || resultMultiplication == -1) return true
        return false
    }

    fun isBlockedBridge(cell1: Cell, cell2: Cell, board: MutableList<Cell>, currentColor: Color): Boolean {
        if (!isBridge(cell1, cell2)) return false
        val deltaX = cell1.x - cell2.x
        val deltaY = cell1.y - cell2.y
        val bridgeNeibhours: Pair<Cell, Cell>
        val oppositeColor = if (currentColor == Color.RED) Color.BLUE else Color.RED

        if (abs(deltaX) == 2) bridgeNeibhours = Pair(
            board[countIndex((cell1.x + cell2.x) / 2, cell1.y)],
            board[countIndex((cell1.x + cell2.x) / 2, cell2.y)]
        ) else if (abs(deltaY) == 2) bridgeNeibhours = Pair(
            board[countIndex(cell1.x, (cell1.y + cell2.y) / 2)],
            board[countIndex(cell2.x, (cell1.y + cell2.y) / 2)]
        ) else bridgeNeibhours = Pair(
            board[countIndex(cell1.x, cell2.y)],
            board[countIndex(cell2.x, cell1.y)]
        )

        if (
            bridgeNeibhours.first.color == oppositeColor &&
            bridgeNeibhours.second.color == bridgeNeibhours.first.color
        ) return true
        return false


    }

    fun isSavedBridge(cell1: Cell, cell2: Cell, board: MutableList<Cell>): Boolean {
        if (!isBridge(cell1, cell2)) return false
        val deltaX = cell1.x - cell2.x
        val deltaY = cell1.y - cell2.y
        val bridgeNeibhours: Pair<Cell, Cell>

        if (abs(deltaX) == 2) bridgeNeibhours = Pair(
            board[countIndex((cell1.x + cell2.x) / 2, cell1.y)],
            board[countIndex((cell1.x + cell2.x) / 2, cell2.y)]
        ) else if (abs(deltaY) == 2) bridgeNeibhours = Pair(
            board[countIndex(cell1.x, (cell1.y + cell2.y) / 2)],
            board[countIndex(cell2.x, (cell1.y + cell2.y) / 2)]
        ) else bridgeNeibhours = Pair(
            board[countIndex(cell1.x, cell2.y)],
            board[countIndex(cell2.x, cell1.y)]
        )

        if (
            (bridgeNeibhours.first.color == Color.RED) and
            (bridgeNeibhours.second.color != bridgeNeibhours.first.color)
        ) return true
        return false
    }

    fun countIndex(x: Int, y: Int) = y * 11 + x

    fun getCells(board: MutableList<Cell>, color: Color): MutableList<Cell> {
        val result = mutableListOf<Cell>()
        for (cell in board) {
            if (cell.color == color) result.add(cell)
        }
        return result
    }
}