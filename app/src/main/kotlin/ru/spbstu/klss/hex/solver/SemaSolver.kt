package ru.spbstu.klss.hex.solver

import ru.spbstu.klss.hex.model.Cell
import ru.spbstu.klss.hex.model.Color
import ru.spbstu.klss.hex.model.Model
import kotlin.math.max
import kotlin.math.min

class SemaSolver(color: Color) : Solver {

    val selfColor = color
    val enemyColor = if (selfColor == Color.RED) Color.BLUE else Color.RED
    val limitDepth = 1

    //koef
    private val closeConnectMultiplayer = 5
    private val emptyConnectMultiplayer = 10

    private lateinit var finalCell: Cell


    override fun action(model: Model): Pair<Int, Int> { // returns X and Y
        val board = model.board
        if (getCells(board, selfColor).size == 0) {
            return if (board[5 * 11 + 5].color == Color.GRAY)
                Pair(5, 5)
            else
                Pair(6, 5)
        }

        var maxScore = Int.MIN_VALUE
        var resultX = -1
        var resultY = -1
        for (element in getCells(board, Color.GRAY)) {
            element.color = selfColor
            val stepScore = alphaBeta(board, 1, Int.MIN_VALUE, Int.MAX_VALUE, false)
            if (stepScore > maxScore) {
                maxScore = stepScore
                resultX = element.x
                resultY = element.y
            }
            element.reset()
        }
        return Pair(resultX, resultY)
    }


    fun alphaBeta(board: MutableList<Cell>, depth: Int, alpha: Int, beta: Int, ourTurn: Boolean): Int {

        if (depth == limitDepth) return evaluaionOfPosition(board)
        var bestEval = 0
        var curAlfa = alpha
        var curBeta = beta

        for (element in getCells(board, Color.GRAY)) {
            if (ourTurn) {

                element.color = selfColor
                bestEval = Int.MIN_VALUE
                val eval = alphaBeta(board, depth + 1, curAlfa, curBeta, false)
                element.reset()
                bestEval = max(bestEval, eval)
                curAlfa = max(curAlfa, eval)

            } else {

                element.color = enemyColor
                bestEval = Int.MAX_VALUE
                val eval = alphaBeta(board, depth + 1, curAlfa, curBeta, true)
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

    fun evaluaionOfPosition(board: MutableList<Cell>): Int {
        var score = 0

        score += closeConnectionCount(board) * closeConnectMultiplayer
        score += emptyConnectionCount(board) * emptyConnectMultiplayer


        return score
    }

    fun isWinnigWay(board: MutableList<Cell>,color: Color):Boolean {
        if(isWinner(color,board)) return true
        return false
    }

    fun isWinner(color: Color, board: MutableList<Cell>): Boolean {
        if (color == Color.RED) {
            for (y in 0..10) {
                val cell = board[11 * y]
                if (hasPath(cell, color)) return true
            }
        } else if (color == Color.BLUE) {
            for (x in 0..10) {
                val cell = board[x]
                if (hasPath(cell, color)) return true
            }
        }
        return false
    }

    fun hasPath(cell: Cell, color: Color): Boolean {
        if (cell.color == Color.GRAY) return false
        val stack = ArrayDeque<Pair<Cell, MutableIterator<Cell>>>()
        var currentCell = cell
        var currentIterator = currentCell.neighbours.iterator()
        val visited = mutableSetOf(currentCell)
        stack.add(Pair(currentCell, currentIterator))

        while (stack.isNotEmpty()) {
            currentCell = stack.last().first
            currentIterator = stack.last().second
            while (currentIterator.hasNext()) {
                val nextCell = currentIterator.next()
                if (nextCell.color != color || nextCell in visited) continue

                if (color == nextCell.color && nextCell.color == Color.BLUE && nextCell.y == 10) return true
                if (color == nextCell.color && nextCell.color == Color.RED && nextCell.x == 10) return true

                visited.add(currentCell)
                currentCell = nextCell
                currentIterator = nextCell.neighbours.iterator()
                stack.add(Pair(currentCell, currentIterator))
            }
            stack.removeLast()
        }
        return false
    }

    fun longestWay(board: MutableList<Cell>, color: Color): MutableList<Cell> {
        val allColorCells = getCells(board, color)
        var longestWay = mutableListOf<Cell>()
        for (cell in allColorCells) {
            val mapOfWays = findWays(cell)
            val curWay = mutableListOf<Cell>()

            var curCell = finalCell
            while (curCell != cell) {
                curWay.add(curCell)
                curCell = mapOfWays[curCell]!!
            }
            if (curWay.size > longestWay.size)
                longestWay = curWay
        }
        return longestWay
    }

    fun findWays(cell: Cell): MutableMap<Cell, Cell> {
        val mapCameFrom = mutableMapOf<Cell, Cell>()
        val cellColor = cell.color
        if (cellColor == Color.GRAY) return mapCameFrom

        val stack = ArrayDeque<Cell>()
        stack.add(cell)

        while (stack.isNotEmpty()) {
            val currentCell = stack.removeLast()
            for (neighbour in currentCell.neighbours) {
                if (neighbour.color == cellColor && neighbour !in mapCameFrom.values) {
                    stack.add(neighbour)
                    mapCameFrom[neighbour] = currentCell
                    finalCell = neighbour
                }
                if (neighbour.color == Color.GRAY) {
                    for (doubleNeighbour in neighbour.neighbours)
                        if (hasEmptyConnect(currentCell, doubleNeighbour)) {
                            mapCameFrom[neighbour] = currentCell
                            mapCameFrom[doubleNeighbour] = neighbour
                            finalCell = doubleNeighbour
                        }
                }
            }
        }
        return mapCameFrom
    }

    fun closeConnectionCount(board: MutableList<Cell>): Int {
        var result = 0
        for (cell in board) {
            if (cell.color == selfColor) result += cellCloseConnect(cell).size
            if (cell.color == enemyColor) result -= cellCloseConnect(cell).size
        }
        return result
    }

    fun cellCloseConnect(cell: Cell): HashSet<Cell> {
        val result = hashSetOf<Cell>()
        for (neighbour in cell.neighbours) {
            if (cell.color == neighbour.color) result.add(neighbour)
        }
        return result
    }

    fun emptyConnectionCount(board: MutableList<Cell>): Int {
        var result = 0
        for (y in 0..10) {
            for (x in 0..10) {
                val currentCell = board[11 * y + x]
                if (currentCell.color == Color.GRAY) continue
                var connectionsCount = 0

                if (y != 0 && x != 10) {
                    val checkedCell = board[11 * (y - 1) + (x + 1)]
                    if (hasEmptyConnect(currentCell, checkedCell)) connectionsCount++
                }
                if (y != 10 && x < 9) {
                    val checkedCell = board[11 * (y + 1) + (x + 2)]
                    if (hasEmptyConnect(currentCell, checkedCell)) connectionsCount++
                }
                if (y < 9 && x != 10) {
                    val checkedCell = board[11 * (y + 2) + (x + 1)]
                    if (hasEmptyConnect(currentCell, checkedCell)) connectionsCount++
                }

                if (currentCell.color == enemyColor) connectionsCount *= -1
                result += connectionsCount
            }
        }
        return result
    }

    fun hasEmptyConnect(cell1: Cell, cell2: Cell): Boolean {
        if (cell1.color != cell2.color) return false
        var ways = 0
        for (neighbour in cell1.neighbours) {
            if (neighbour.color == Color.GRAY && cell2 in neighbour.neighbours) ways++
        }
        return ways == 2
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