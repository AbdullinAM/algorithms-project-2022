package ru.spbstu.klss.hex.solver

import ru.spbstu.klss.hex.model.Cell
import ru.spbstu.klss.hex.model.Color
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class SemaSolver(color: Color) : Solver{

    val selfColor = color
    val enemyColor = if (selfColor == Color.RED) Color.BLUE else Color.RED
    val limitDepth = 3

    val bridgeScore = 2
    val savedBridgeScore = 4
    val sizeMultiplier = 10
    val cellCountMultiplier = 1
    val attackedBridgeScore = -6
    val blockedEnemyBridgeScore = 4

    override fun action(board: MutableList<Cell>): Pair<Int, Int> { // returns X and Y
        if (getCells(board, selfColor).size == 0) {
            if (board[5 * 11 + 5].color == Color.GRAY)
                return Pair(5, 5)
            else
                return Pair(4, 5)
        }

        var maxScore = Int.MIN_VALUE
        var result_x = -1
        var result_y = -1
        for (element in getCells(board, Color.GRAY)) {
            element.color = selfColor
            val stepScore = alphaBeta(board, 1, Int.MIN_VALUE, Int.MAX_VALUE)
            if (stepScore > maxScore) {
                maxScore = stepScore
                result_x = element.x
                result_y = element.y
            }
            element.reset()
        }
        return Pair(result_x, result_y)
    }

    fun minimax(board: MutableList<Cell>, depth: Int): Int {
        val scoreList = mutableListOf<Int>()
        var result = if (depth % 2 == 1) Int.MAX_VALUE else Int.MIN_VALUE
        if (depth == limitDepth) return countScore(board)
        for (element in getCells(board, Color.GRAY)) {
            element.color = if (depth % 2 == 1) enemyColor else selfColor
            val score = minimax(board, depth + 1)
            scoreList.add(score)
            element.reset()
            if (depth % 2 == 1) {
                result = min(result, score)

            } else {
                result = max(result, score)
            }
        }
        //return if (depth % 2 == 1) scoreList.minOrNull() ?: -1000 else scoreList.maxOrNull() ?: 1000
        return result
    }

    fun alphaBeta(board: MutableList<Cell>, depth: Int, alpha: Int, beta: Int): Int {
        var result = if (depth % 2 == 1) Int.MAX_VALUE else Int.MIN_VALUE
        var beta = beta
        var alpha = alpha
        if (depth == limitDepth) return countScore(board)
        for (element in getCells(board, Color.GRAY)) {
            element.color = if (depth % 2 == 1) enemyColor else selfColor
            val score = alphaBeta(board, depth + 1, alpha, beta)
            element.reset()
            if (depth % 2 == 1) {
                result = min(result, score)
                beta = min(beta, score)

            } else {
                result = max(result, score)
                alpha = max(alpha, score)
            }
            if (beta <= alpha) {
                break
            }
        }
        return result
    }

    fun countScore(board: MutableList<Cell>): Int {
        var score = 0
        score += (countBridges(board, selfColor) - countBridges(board, enemyColor)) * bridgeScore
        score += countMaxLength(board, selfColor) - countMaxLength(board, enemyColor) * sizeMultiplier
        score += (countAttackedBridges(board, selfColor) - countAttackedBridges(board, enemyColor)) * attackedBridgeScore
        score += (countSavedBridges(board, selfColor) - countSavedBridges(board, enemyColor)) * savedBridgeScore
        score += (countEnemyBlockedBridges(board, selfColor) - countEnemyBlockedBridges(board, enemyColor)) * blockedEnemyBridgeScore
        score += (getCells(board, selfColor).size - getCells(board, enemyColor).size) * cellCountMultiplier
        if (countMaxLength(board, selfColor) == 11) score += 1000
        else if (countMaxLength(board, enemyColor) == 11) score -= 1000
        return score
    }

    fun countBridges(board: MutableList<Cell>, color: Color): Int {
        var bridgeCount = 0
        val currentCellList = getCells(board, color)

        for (i in 0 until currentCellList.size) {
            for (j in i + 1 until currentCellList.size) {
                val cell1 = currentCellList[i]
                val cell2 = currentCellList[j]
                if (isBridge(cell1, cell2) and !isBlockedBridge(cell1, cell2, board)) {
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

            minCoord = Int.MAX_VALUE
            maxCoord = Int.MIN_VALUE

            while (stack.isNotEmpty()) {
                currentCell = stack.last().first
                currentIterator = stack.last().second
                while (currentIterator.hasNext()) {
                    if (currentColor == Color.BLUE) {
                        minCoord = min(minCoord, currentCell.y)
                        maxCoord = max(maxCoord, currentCell.y)
                    } else if (currentColor == Color.RED) {
                        minCoord = min(minCoord, currentCell.x)
                        maxCoord = max(maxCoord, currentCell.x)
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

            maxLenght = max(maxLenght, maxCoord - minCoord + 1)
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

    fun countAttackedBridges(board: MutableList<Cell>, currentColor: Color): Int {
        var bridgeCount = 0
        val currentCellList = getCells(board, currentColor)

        for (i in 0 until currentCellList.size) {
            for (j in i + 1 until currentCellList.size) {
                val cell1 = currentCellList[i]
                val cell2 = currentCellList[j]
                if (isAttackedBridge(cell1, cell2, board)) {
                    bridgeCount++
                }
            }
        }
        return bridgeCount
    }

    fun countEnemyBlockedBridges(board: MutableList<Cell>, currentColor: Color): Int {
        var bridgeCount = 0
        val oppositeColor = if (currentColor == Color.RED) Color.BLUE else Color.RED
        val currentCellList = getCells(board, oppositeColor)

        for (i in 0 until currentCellList.size) {
            for (j in i + 1 until currentCellList.size) {
                val cell1 = currentCellList[i]
                val cell2 = currentCellList[j]
                if (isBlockedBridge(cell1, cell2, board)) {
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

    fun isBlockedBridge(cell1: Cell, cell2: Cell, board: MutableList<Cell>): Boolean {
        if (!isBridge(cell1, cell2)) return false
        val deltaX = cell1.x - cell2.x
        val deltaY = cell1.y - cell2.y
        val bridgeNeibhours: Pair<Cell, Cell>
        val oppositeColor = if (cell1.color == Color.RED) Color.BLUE else Color.RED

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

    fun isAttackedBridge(cell1: Cell, cell2: Cell, board: MutableList<Cell>): Boolean {
        if (!isBridge(cell1, cell2)) return false
        val deltaX = cell1.x - cell2.x
        val deltaY = cell1.y - cell2.y
        val bridgeNeibhours: Pair<Cell, Cell>
        val oppositeColor = if (cell1.color == Color.RED) Color.BLUE else Color.RED

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
            Color.GRAY in listOf(bridgeNeibhours.first.color, bridgeNeibhours.second.color) &&
            oppositeColor in listOf(bridgeNeibhours.first.color, bridgeNeibhours.second.color)
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