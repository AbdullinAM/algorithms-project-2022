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
    val limitDepth = 2

    override fun action(model: Model): Pair<Int, Int> { // returns X and Y
        if (getCells(model.board, selfColor).size == 0) {
            if (model.getCell(5, 5).color == Color.GRAY)
                return Pair(5, 5)
            else
                return Pair(4, 5)
        }

        var maxScore = Int.MIN_VALUE
        var result_x = -1
        var result_y = -1
        for (element in getCells(model.board, Color.GRAY)) {
            println("Проверяем ход: ${element.x}, ${element.y}")
            element.color = selfColor
            val stepScore = alphaBeta(model, 1, Int.MIN_VALUE, Int.MAX_VALUE)
            if (stepScore > maxScore) {
                maxScore = stepScore
                result_x = element.x
                result_y = element.y
            }
            element.reset()
        }
        println("Ход: $result_x, $result_y")
        return Pair(result_x, result_y)
    }

    fun minimax(model: Model, depth: Int): Int {
        var result = if (depth % 2 == 1) Int.MAX_VALUE else Int.MIN_VALUE
        if (depth == limitDepth) return countScore(model)
        for (element in getCells(model.board, Color.GRAY)) {
            element.color = if (depth % 2 == 1) enemyColor else selfColor
            val score = minimax(model, depth + 1)
            element.reset()
            if (depth % 2 == 1) {
                result = min(result, score)

            } else {
                result = max(result, score)
            }
        }
        return result
    }

    fun alphaBeta(model: Model, depth: Int, alpha: Int, beta: Int): Int {
        var result = if (depth % 2 == 1) Int.MAX_VALUE else Int.MIN_VALUE
        var beta = beta
        var alpha = alpha
        if (depth == limitDepth) return countScore(model)
        for (element in getCells(model.board, Color.GRAY)) {
            element.color = if (depth % 2 == 1) enemyColor else selfColor
            val score = alphaBeta(model, depth + 1, alpha, beta)
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

    fun countScore(model: Model): Int {
        var score = 0
        val redCoefficient: Int
        val blueCoefficient: Int
        if (selfColor == Color.RED) {
            redCoefficient = 1
            blueCoefficient = -1
        } else {
            redCoefficient = -1
            blueCoefficient = 1
        }
        score += redCoefficient * (11 - findMinPath(model.redStartBase, model.redEndBase, Color.RED))
        score += blueCoefficient * (11 - findMinPath(model.blueStartBase, model.blueEndBase, Color.BLUE))
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

    fun countBlockedBridges(board: MutableList<Cell>, currentColor: Color): Int {
        var bridgeCount = 0
        val currentCellList = getCells(board, currentColor)

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

    fun findMinPath(startCell: Cell, endCell: Cell, color: Color): Int {
        fun index(x: Int, y: Int) = (y + 1) * 13 + x + 1
        val oppositeColor = if (color == Color.RED) Color.BLUE else Color.RED
        val matrix = Array(13 * 13) { Int.MAX_VALUE }
        val visited = mutableListOf<Cell>()
        var currentCell: Cell
        var currentIterator: Iterator<Cell>
        val stack = ArrayDeque<Cell>()

        matrix[index(startCell.x, startCell.y)] = 0
        stack.addLast(startCell)

        while (stack.isNotEmpty()) {
            currentCell = stack.last()
            currentIterator = currentCell.neighbours.iterator()
            var minPathWay = Int.MAX_VALUE
            var nextCell: Cell? = null

            while (currentIterator.hasNext()) {
                var weight = 1
                val checkedCell = currentIterator.next()
                if (checkedCell in visited) continue
                if (checkedCell.color == oppositeColor) continue
                else if (checkedCell.color == color) {
                    weight = 0
                }
                val currentCellPathWay = matrix[index(currentCell.x, currentCell.y)]
                val checkedCellPathWay = matrix[index(checkedCell.x, checkedCell.y)]
                matrix[index(checkedCell.x, checkedCell.y)] = min(currentCellPathWay + weight, checkedCellPathWay)
                if (matrix[index(checkedCell.x, checkedCell.y)] < minPathWay) {
                    nextCell = checkedCell
                    minPathWay = matrix[index(checkedCell.x, checkedCell.y)]
                }

            }
            visited.add(currentCell)
            if (nextCell != null) {
                stack.addLast(nextCell)
            } else {
                stack.removeLast()
            }
            var resultString = ""
            for (y in 0..12) {
                var row = ""
                for (x in 0..12) {
                    val number = matrix[y * 13 + x]
                    when {
                        y == startCell.y + 1 && x == startCell.x + 1 -> row += "$number  "
                        y == endCell.y + 1 && x == endCell.x + 1 -> row += "$number "
                        y == 0 || y == 12 -> row += "*  "
                        x == 0 || x == 12 -> row += "*  "
                        number == Int.MAX_VALUE -> row += "-  "
                        number < 10 -> row += "$number  "
                        else -> row += "$number "
                    }
                }
                row += "\n"
                resultString += row
            }
            //println(resultString)
        }

        return matrix[index(endCell.x, endCell.y)]
    }
}