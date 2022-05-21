package ru.spbstu.klss.testHex.solver

import org.junit.Test
import ru.spbstu.klss.hex.model.Cell
import ru.spbstu.klss.hex.model.Color
import ru.spbstu.klss.hex.model.Model
import ru.spbstu.klss.hex.solver.AlexSolver
import java.util.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AlexSolverTest {

    @Test
    fun getCellsTest() {
        val model = Model()
        val random = Random()
        val alexSolver = AlexSolver(Color.RED)
        val visited = mutableListOf<Cell>()

        for (i in 0..120) {
            val random_x = random.nextInt(0, 11)
            val random_y = random.nextInt(0, 11)

            if (model.getCell(random_x, random_y) !in visited) {
                val cell = model.getCell(random_x, random_y)
                visited.add(cell)
                model.setCellColor(cell, Color.RED)
            }

            for (cell in visited) {
                assertFalse(cell in alexSolver.getCells(model.board, Color.GRAY))
                assertTrue(cell in alexSolver.getCells(model.board, Color.RED))
                assertTrue(alexSolver.getCells(model.board, Color.BLUE).isEmpty())
            }

        }
        println("All clear")
    }

    @Test
    fun findMinPathTest(){
        val solver = AlexSolver(Color.RED)

        val emptyModel = Model()

        assertEquals(11, solver.aStar(emptyModel.blueStartBase, emptyModel.blueEndBase, Color.BLUE))
        assertEquals(11, solver.aStar(emptyModel.redStartBase, emptyModel.redEndBase, Color.RED))

    }
}