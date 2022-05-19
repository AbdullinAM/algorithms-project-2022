package ru.spbstu.klss.hex.solver

import ru.spbstu.klss.hex.model.Cell
import ru.spbstu.klss.hex.model.Color

class AlexSolver(color: Color) : Solver{

    val selfColor = color
    val enemyColor = if (selfColor == Color.RED) Color.BLUE else Color.RED
    val moveOrder = mutableListOf<Pair<Int, Int>>()
    val selfCells = mutableListOf<Cell>()
    val enemyCells = mutableListOf<Cell>()

    override fun action(board: MutableList<Cell>): Pair<Int, Int> { // returns X and Y
        return Pair(-1, -1)
    }

    fun countScore(board: MutableList<Cell>, color: Color): Int {
        var score = 0
        score += countBridges(board, color)
        score += countMaxLength(board, color)
        score += countSavedBridges(board, color)
        if (color == selfColor) score += selfCells.size
        else if (color == enemyColor) score += enemyCells.size
        return score
    }

    fun countBridges(board: MutableList<Cell>, color: Color): Int {
        TODO()
    }

    fun countMaxLength(board: MutableList<Cell>, color: Color): Int {
        TODO()
    }

    fun countSavedBridges(board: MutableList<Cell>, color: Color): Int {
        TODO()
    }

    fun isBridge(cell1: Cell, cell2: Cell): Boolean {
        val deltaX = cell1.x - cell2.x
        val deltaY = cell1.y - cell2.y
        val resultMultiplication = deltaX * deltaY
        if (resultMultiplication == 2 || resultMultiplication == -1) return true
        return false
    }

    fun isBlockedBridge(cell1: Cell, cell2: Cell): Boolean {
        if (!isBridge(cell1, cell2)) return false
        TODO()
    }

    fun isAttackedBridge(cell1: Cell, cell2: Cell): Boolean {
        if (!isBridge(cell1, cell2)) return false
        TODO()
    }
}