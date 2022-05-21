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
        for (element in getCells(model.board, Color.GRAY).sortedBy { abs(5 - it.x) + abs(5 - it.y)}) {
            element.color = selfColor
            val stepScore = alphaBeta(model, 1, Int.MIN_VALUE, Int.MAX_VALUE)
            if (stepScore > maxScore) {
                maxScore = stepScore
                result_x = element.x
                result_y = element.y
            }
            element.reset()
        }
        return Pair(result_x, result_y)
    }

    fun minimax(model: Model, depth: Int): Int {
        var result = if (depth % 2 == 1) Int.MAX_VALUE else Int.MIN_VALUE
        if (depth == limitDepth) return countScore(model)
        for (element in getCells(model.board, Color.GRAY).sortedBy { abs(5 - it.x) + abs(5 - it.y)}) {
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
        for (element in getCells(model.board, Color.GRAY).sortedBy { abs(5 - it.x) + abs(5 - it.y)}) {
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
        val redCoefficient = if (selfColor == Color.RED) 1 else -1
        val blueCoefficient = redCoefficient * -1

        score += redCoefficient * (11 - aStar(model.redStartBase, model.redEndBase, Color.RED))
        score += blueCoefficient * (11 - aStar(model.blueStartBase, model.blueEndBase, Color.BLUE))
        return score
    }

    fun getCells(board: MutableList<Cell>, color: Color): MutableList<Cell> {
        val result = mutableListOf<Cell>()
        for (cell in board) {
            if (cell.color == color) result.add(cell)
        }
        return result
    }

    fun dijkstra(startCell: Cell, endCell: Cell, color: Color): Int {
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

    fun aStar(startCell: Cell, endCell: Cell, color: Color): Int {
        class CellStats(gCost: Int, hCost: Int) {
            var gCost = gCost
            var hCost = hCost
            var fCost = gCost + hCost
            var path = Int.MAX_VALUE
        }
        fun index(x: Int, y: Int) = (y + 1) * 13 + x + 1
        fun hCost(cell: Cell) = if (cell.color == Color.BLUE) abs(cell.x - endCell.x) else abs(cell.y - endCell.y)
        val oppositeColor = if (color == Color.RED) Color.BLUE else Color.RED
        val matrix = Array(13 * 13) { CellStats(Int.MAX_VALUE, Int.MAX_VALUE) }

        val toCheck = mutableListOf(startCell)
        val visited = mutableListOf<Cell>()
        matrix[index(startCell.x, startCell.y)] = CellStats(0, hCost(startCell))
        matrix[index(startCell.x, startCell.y)].path = 0


        while (toCheck.isNotEmpty()) {
            val currentCell = toCheck.sortedBy { matrix[index(it.x, it.y)].fCost }[0]
            val currentGCost = matrix[index(currentCell.x, currentCell.y)].gCost
            val currentPath = matrix[index(currentCell.x, currentCell.y)].path
            toCheck.remove(currentCell)
            visited.add(currentCell)

            if (currentCell == endCell) return currentPath

            for (element in currentCell.neighbours) {
                if (element in visited || element.color == oppositeColor) {
                    continue
                }
                val elementGCost = matrix[index(element.x, element.y)].gCost
                val elementPath = matrix[index(element.x, element.y)].path
                var weight = 1


                if (element !in toCheck || currentGCost < elementGCost) {
                    if (element !in toCheck) toCheck.add(element)
                    if (element.color == color) {
                        weight = 0
                    }
                    matrix[index(element.x, element.y)] = CellStats(currentGCost + weight, hCost(element))
                    matrix[index(element.x, element.y)].path = currentPath + weight

                }
            }
            var resultString = ""
            for (y in 0..12) {
                var row = ""
                for (x in 0..12) {
                    val number = matrix[y * 13 + x].path
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
        return Int.MIN_VALUE
    }
}