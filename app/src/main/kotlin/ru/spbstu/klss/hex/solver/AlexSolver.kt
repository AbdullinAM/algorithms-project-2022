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
            return Pair(4, 5)
        }

        var maxScore = Int.MIN_VALUE
        var resultX = -1
        var resultY = -1
        for (element in getCells(model.board, Color.GRAY).sortedBy { abs(5 - it.x) + abs(5 - it.y)}) {
            element.color = selfColor
            val stepScore = alphaBeta(model, 1, Int.MIN_VALUE, Int.MAX_VALUE)
            if (stepScore > maxScore) {
                maxScore = stepScore
                resultX = element.x
                resultY = element.y
            }
            element.reset()
        }
        return Pair(resultX, resultY)
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
        val redCoefficient = if (selfColor == Color.RED) -2 else 2
        val blueCoefficient = redCoefficient * -1 / 2

        score += redCoefficient * aStar(model.redStartBase, model.redEndBase, Color.RED)
        score += blueCoefficient * aStar(model.blueStartBase, model.blueEndBase, Color.BLUE)
        return score
    }

    fun getCells(board: MutableList<Cell>, color: Color): MutableList<Cell> {
        val result = mutableListOf<Cell>()
        for (cell in board) {
            if (cell.color == color) result.add(cell)
        }
        return result
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
        }
        return Int.MAX_VALUE
    }

    override fun toString(): String {
        return "AlexSolver"
    }
}