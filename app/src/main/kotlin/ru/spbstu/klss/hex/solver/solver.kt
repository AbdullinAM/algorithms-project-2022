package ru.spbstu.klss.hex.solver

import ru.spbstu.klss.hex.model.Model

interface Solver {

    fun action(model: Model): Pair<Int, Int> // returns X and Y

}
