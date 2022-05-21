package ru.spbstu.klss.hex.solver

import ru.spbstu.klss.hex.model.Cell
import ru.spbstu.klss.hex.model.Color
import ru.spbstu.klss.hex.model.Model
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class SemaSolver(color: Color) : Solver {

    private val selfColor = color
    private val enemyColor = if (selfColor == Color.RED) Color.BLUE else Color.RED
    private val limitDepth = 2

    //koef
    private val connectCountMultiplayer = 5
    private val diagonalCountMultiplayer = 10
    private val savedDiagonalMultiplayer = 6
    private val sizeMultiplier = 10
    private val cellCountMultiplier = 1
    private val attackedDiagonalMultiplayer = -8
    private val blockedEnemyDiagonalMultiplayer = 7


    override fun action(model: Model): Pair<Int, Int> { // returns X and Y
        val board = model.board
        if (getCells(board, selfColor).size == 0) {
            return if (board[5 * 11 + 5].color == Color.GRAY)
                Pair(5, 5)
            else
                Pair(4, 5)
        }

        var maxScore = Int.MIN_VALUE
        var resultX = -1
        var resultY = -1
        for (element in getCells(board, Color.GRAY)) {
            element.color = selfColor
            val stepScore = minMaxAlphaBeta(model, 1, Int.MIN_VALUE, Int.MAX_VALUE, false)
            if (stepScore > maxScore) {
                maxScore = stepScore
                resultX = element.x
                resultY = element.y
            }
            element.reset()
        }
        return Pair(resultX, resultY)
    }


    private fun minMaxAlphaBeta(model: Model, depth: Int, alpha: Int, beta: Int, ourTurn: Boolean): Int {
        if (depth == limitDepth) return evaluationOfPosition(model)
        var bestEval = 0
        var curAlfa = alpha
        var curBeta = beta
        val board = model.board
        for (element in getCells(board, Color.GRAY)) {
            if (ourTurn) {

                element.color = selfColor
                bestEval = Int.MIN_VALUE
                val eval = minMaxAlphaBeta(model, depth + 1, curAlfa, curBeta, false)
                element.reset()
                bestEval = max(bestEval, eval)
                curAlfa = max(curAlfa, eval)

            } else {

                element.color = enemyColor
                bestEval = Int.MAX_VALUE
                val eval = minMaxAlphaBeta(model, depth + 1, curAlfa, curBeta, true)
                element.reset()
                bestEval = min(bestEval, eval)
                curBeta = min(curBeta, eval)

            }
            if (curBeta <= curAlfa) {
                break
            }
        }
        return bestEval
    }


    private fun djhxtra(startCell: Cell, endCell: Cell, color: Color): Int {
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
                    row += when {
                        y == startCell.y + 1 && x == startCell.x + 1 -> "$number  "
                        y == endCell.y + 1 && x == endCell.x + 1 -> "$number "
                        y == 0 || y == 12 -> "*  "
                        x == 0 || x == 12 -> "*  "
                        number == Int.MAX_VALUE -> "-  "
                        number < 10 -> "$number  "
                        else -> "$number "
                    }
                }
                row += "\n"
                resultString += row
            }
        }
        return matrix[index(endCell.x, endCell.y)]
    }

    private fun evaluationOfPosition(model: Model): Int {
        var score = 0
        val board = model.board

        score += connectsCount(board) * connectCountMultiplayer
        score += diagonalsCount(board) * diagonalCountMultiplayer
        score += countMaxLength(board, selfColor) - countMaxLength(board, enemyColor) * sizeMultiplier
        score += (countAttackedDiagonals(board, selfColor) - countAttackedDiagonals(
            board,
            enemyColor
        )) * attackedDiagonalMultiplayer
        
        score += (countSavedDiagonals(board, selfColor) - countSavedDiagonals(
            board,
            enemyColor
        )) * savedDiagonalMultiplayer
        
        score += (countEnemyBlockedDiagonals(board, selfColor) - countEnemyBlockedDiagonals(
            board,
            enemyColor
        )) * blockedEnemyDiagonalMultiplayer
        
        score += (getCells(board, selfColor).size - getCells(board, enemyColor).size) * cellCountMultiplier
        
        if (countMaxLength(board, selfColor) == 11) score += 1000
        else if (countMaxLength(board, enemyColor) == 11) score -= 1000

//        val redCoefficient = if (selfColor == Color.RED) 1 else -1
//        val blueCoefficient = redCoefficient * -1
//        score += redCoefficient * (11 - djhxtra(model.redStartBase, model.redEndBase, Color.RED))
//        score += blueCoefficient * (11 - djhxtra(model.blueStartBase, model.blueEndBase, Color.BLUE))
        return score
    }

    private fun connectsCount(board: MutableList<Cell>): Int {
        var result = 0
        for (cell in board) {
            if (cell.color == selfColor) result += cellConnects(cell).size
            if (cell.color == enemyColor) result -= cellConnects(cell).size
        }
        return result
    }
    
    private fun cellConnects(cell: Cell): HashSet<Cell> {
        val result = hashSetOf<Cell>()
        for (neighbour in cell.neighbours) {
            if (cell.color == neighbour.color) result.add(neighbour)
        }
        return result
    }

    fun diagonalsCount(board: MutableList<Cell>): Int {
        var result = 0
        for (y in 0..10) {
            for (x in 0..10) {
                val currentCell = board[11 * y + x]
                if (currentCell.color == Color.GRAY) continue
                var connectionsCount = 0

                if (y != 0 && x != 10) {
                    val checkedCell = board[11 * (y - 1) + (x + 1)]
                    if (isDiagonal(currentCell, checkedCell)) connectionsCount++
                }
                if (y != 10 && x < 9) {
                    val checkedCell = board[11 * (y + 1) + (x + 2)]
                    if (isDiagonal(currentCell, checkedCell)) connectionsCount++
                }
                if (y < 9 && x != 10) {
                    val checkedCell = board[11 * (y + 2) + (x + 1)]
                    if (isDiagonal(currentCell, checkedCell)) connectionsCount++
                }
                if (currentCell.color == enemyColor) connectionsCount *= -1
                result += connectionsCount
            }
        }
        return result
    }

    fun isDiagonal(cell1: Cell, cell2: Cell): Boolean {
        if (cell1.color != cell2.color) return false
        var ways = 0
        for (neighbour in cell1.neighbours) {
            if (neighbour.color == Color.GRAY && cell2 in neighbour.neighbours) ways++
        }
        return ways == 2
    }


    fun countMaxLength(board: MutableList<Cell>, currentColor: Color): Int {
        val currentCellList = getCells(board, currentColor)
        var minCord: Int
        var maxCord: Int
        var maxLength = 0
        val visited = mutableSetOf<Cell>()

        for (cell in currentCellList) {
            if (cell in visited) continue
            val stack = ArrayDeque<Pair<Cell, MutableIterator<Cell>>>()
            var currentCell = cell
            var currentIterator = currentCell.neighbours.iterator()
            visited.add(currentCell)
            stack.add(Pair(currentCell, currentIterator))

            minCord = Int.MAX_VALUE
            maxCord = Int.MIN_VALUE

            while (stack.isNotEmpty()) {
                currentCell = stack.last().first
                currentIterator = stack.last().second
                while (currentIterator.hasNext()) {
                    if (currentColor == Color.BLUE) {
                        minCord = min(minCord, currentCell.y)
                        maxCord = max(maxCord, currentCell.y)
                    } else if (currentColor == Color.RED) {
                        minCord = min(minCord, currentCell.x)
                        maxCord = max(maxCord, currentCell.x)
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

            maxLength = max(maxLength, maxCord - minCord + 1)
        }
        return maxLength
    }

    fun countSavedDiagonals(board: MutableList<Cell>, currentColor: Color): Int {
        var diagonalCount = 0
        val currentCellList = getCells(board, currentColor)

        for (i in 0 until currentCellList.size) {
            for (j in i + 1 until currentCellList.size) {
                val cell1 = currentCellList[i]
                val cell2 = currentCellList[j]
                if (isSavedDiagonal(cell1, cell2, board)) {
                    diagonalCount++
                }
            }
        }
        return diagonalCount
    }

    private fun countAttackedDiagonals(board: MutableList<Cell>, currentColor: Color): Int {
        var diagonalCount = 0
        val currentCellList = getCells(board, currentColor)

        for (i in 0 until currentCellList.size) {
            for (j in i + 1 until currentCellList.size) {
                val cell1 = currentCellList[i]
                val cell2 = currentCellList[j]
                if (isAttackedDiagonal(cell1, cell2, board)) {
                    diagonalCount++
                }
            }
        }
        return diagonalCount
    }

    private fun countEnemyBlockedDiagonals(board: MutableList<Cell>, currentColor: Color): Int {
        var diagonalCount = 0
        val oppositeColor = if (currentColor == Color.RED) Color.BLUE else Color.RED
        val currentCellList = getCells(board, oppositeColor)

        for (i in 0 until currentCellList.size) {
            for (j in i + 1 until currentCellList.size) {
                val cell1 = currentCellList[i]
                val cell2 = currentCellList[j]
                if (isBlockedDiagonal(cell1, cell2, board)) {
                    diagonalCount++
                }
            }
        }
        return diagonalCount
    }
    
    fun isBlockedDiagonal(cell1: Cell, cell2: Cell, board: MutableList<Cell>): Boolean {
        if (!isDiagonal(cell1, cell2)) return false
        val deltaX = cell1.x - cell2.x
        val deltaY = cell1.y - cell2.y
        val oppositeColor = if (cell1.color == Color.RED) Color.BLUE else Color.RED

        val diagonalNeighbours: Pair<Cell, Cell> = if (abs(deltaX) == 2) Pair(
            board[countIndex((cell1.x + cell2.x) / 2, cell1.y)],
            board[countIndex((cell1.x + cell2.x) / 2, cell2.y)]
        ) else if (abs(deltaY) == 2) Pair(
            board[countIndex(cell1.x, (cell1.y + cell2.y) / 2)],
            board[countIndex(cell2.x, (cell1.y + cell2.y) / 2)]
        ) else Pair(
            board[countIndex(cell1.x, cell2.y)],
            board[countIndex(cell2.x, cell1.y)]
        )

        if (
            diagonalNeighbours.first.color == oppositeColor &&
            diagonalNeighbours.second.color == diagonalNeighbours.first.color
        ) return true
        return false


    }

    private fun isAttackedDiagonal(cell1: Cell, cell2: Cell, board: MutableList<Cell>): Boolean {
        if (!isDiagonal(cell1, cell2)) return false
        val deltaX = cell1.x - cell2.x
        val deltaY = cell1.y - cell2.y
        val oppositeColor = if (cell1.color == Color.RED) Color.BLUE else Color.RED

        val diagonalNeighbours: Pair<Cell, Cell> = if (abs(deltaX) == 2) Pair(
            board[countIndex((cell1.x + cell2.x) / 2, cell1.y)],
            board[countIndex((cell1.x + cell2.x) / 2, cell2.y)]
        ) else if (abs(deltaY) == 2) Pair(
            board[countIndex(cell1.x, (cell1.y + cell2.y) / 2)],
            board[countIndex(cell2.x, (cell1.y + cell2.y) / 2)]
        ) else Pair(
            board[countIndex(cell1.x, cell2.y)],
            board[countIndex(cell2.x, cell1.y)]
        )

        if ((diagonalNeighbours.first.color == Color.GRAY || diagonalNeighbours.second.color == Color.GRAY) &&
            (diagonalNeighbours.first.color == oppositeColor || diagonalNeighbours.second.color == oppositeColor)
        ) return true
        return false


    }

    fun isSavedDiagonal(cell1: Cell, cell2: Cell, board: MutableList<Cell>): Boolean {
        if (!isDiagonal(cell1, cell2)) return false
        val deltaX = cell1.x - cell2.x
        val deltaY = cell1.y - cell2.y

        val diagonalNeighbours: Pair<Cell, Cell> = if (abs(deltaX) == 2) Pair(
            board[countIndex((cell1.x + cell2.x) / 2, cell1.y)],
            board[countIndex((cell1.x + cell2.x) / 2, cell2.y)]
        ) else if (abs(deltaY) == 2) Pair(
            board[countIndex(cell1.x, (cell1.y + cell2.y) / 2)],
            board[countIndex(cell2.x, (cell1.y + cell2.y) / 2)]
        ) else Pair(
            board[countIndex(cell1.x, cell2.y)],
            board[countIndex(cell2.x, cell1.y)]
        )

        if (
            (diagonalNeighbours.first.color == Color.RED) and
            (diagonalNeighbours.second.color != diagonalNeighbours.first.color)
        ) return true
        return false
    }

    private fun countIndex(x: Int, y: Int) = y * 11 + x

    fun getCells(board: MutableList<Cell>, color: Color): MutableList<Cell> {
        val result = mutableListOf<Cell>()
        for (cell in board) {
            if (cell.color == color) result.add(cell)
        }
        return result
    }
}