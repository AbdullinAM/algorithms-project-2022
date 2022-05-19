package ru.spbstu.klss.hex.solver

import ru.spbstu.klss.hex.model.Cell

interface Solver {

    fun action(board: MutableList<Cell>): Pair<Int, Int> // returns X and Y

}
