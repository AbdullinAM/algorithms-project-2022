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
    fun aStarTest(){
        val solver = AlexSolver(Color.RED)

        val emptyModel = Model()

        assertEquals(11, solver.aStar(emptyModel.blueStartBase, emptyModel.blueEndBase, Color.BLUE))
        assertEquals(11, solver.aStar(emptyModel.redStartBase, emptyModel.redEndBase, Color.RED))

        val model1 = Model()

        //Board view
        //   | 0 1 2 3 4 5 6 7 8 9 10
        // -------------------------
        // 0 | - - - - - - - - - - -
        // 1 | - - R R R R - B - - -
        // 2 | - - R - - - R B - - -
        // 3 | - - - - - - - B - - -
        // 4 | - - - - - - B - - - -
        // 5 | - - - - - - B - - - -
        // 6 | - - - - - - B - - - -
        // 7 | - - - - - B - - - - -
        // 8 | - - - - - B - - - - -
        // 9 | - - - - - - - - - - -
        // 10| - - - - - - - - - - -

        model1.setCellColor(2, 1, Color.RED)
        model1.setCellColor(2, 2, Color.RED)
        model1.setCellColor(3, 1, Color.RED)
        model1.setCellColor(4, 1, Color.RED)
        model1.setCellColor(5, 1, Color.RED)
        model1.setCellColor(6, 2, Color.RED)
        model1.setCellColor(7, 1, Color.BLUE)
        model1.setCellColor(7, 2, Color.BLUE)
        model1.setCellColor(7, 3, Color.BLUE)
        model1.setCellColor(6, 4, Color.BLUE)
        model1.setCellColor(6, 5, Color.BLUE)
        model1.setCellColor(6, 6, Color.BLUE)
        model1.setCellColor(5, 7, Color.BLUE)
        model1.setCellColor(5, 8, Color.BLUE)

        assertEquals(5, solver.aStar(model1.blueStartBase, model1.blueEndBase, Color.BLUE))
        assertEquals(8, solver.aStar(model1.redStartBase, model1.redEndBase, Color.RED))

        val model2 = Model()

        //Board view
        //   | 0 1 2 3 4 5 6 7 8 9 10
        // -------------------------
        // 0 | - - - - - - - - - - -
        // 1 | - B - - - - - - - - -
        // 2 | - B - - - - - - - - -
        // 3 | - B  - - - - - - - -
        // 4 | - B B - - - - - - - -
        // 5 | - - B B - - - - - - -
        // 6 | - - - B - - - - - - -
        // 7 | - - - B B - - - - - -
        // 8 | - R R R R R R - - - -
        // 9 | - - - - - - - - - - -
        // 10| - - - - - - - - - - -

        for (y in 1..4) model2.setCellColor(1, y, Color.BLUE)
        for (y in 4..5) model2.setCellColor(2, y, Color.BLUE)
        for (y in 5..7) model2.setCellColor(3, y, Color.BLUE)
        model2.setCellColor(4, 7, Color.BLUE)
        for (x in 1..6) model2.setCellColor(x, 8, Color.RED)

        assertEquals(6, solver.aStar(model2.blueStartBase, model2.blueEndBase, Color.BLUE))
        assertEquals(5, solver.aStar(model2.redStartBase, model2.redEndBase, Color.RED))

    }

    @Test
    fun countScoreTest() {
        val solver = AlexSolver(Color.RED)
        val emptyModel = Model()

        assertEquals(0, solver.countScore(emptyModel))

        val model1 = Model()

        //Board view
        //   | 0 1 2 3 4 5 6 7 8 9 10
        // -------------------------
        // 0 | - - - - - - - - - - -
        // 1 | - - R R R R - B - - -
        // 2 | - - R - - - R B - - -
        // 3 | - - - - - - - B - - -
        // 4 | - - - - - - B - - - -
        // 5 | - - - - - - B - - - -
        // 6 | - - - - - - B - - - -
        // 7 | - - - - - B - - - - -
        // 8 | - - - - - B - - - - -
        // 9 | - - - - - - - - - - -
        // 10| - - - - - - - - - - -

        model1.setCellColor(2, 1, Color.RED)
        model1.setCellColor(2, 2, Color.RED)
        model1.setCellColor(3, 1, Color.RED)
        model1.setCellColor(4, 1, Color.RED)
        model1.setCellColor(5, 1, Color.RED)
        model1.setCellColor(6, 2, Color.RED)
        model1.setCellColor(7, 1, Color.BLUE)
        model1.setCellColor(7, 2, Color.BLUE)
        model1.setCellColor(7, 3, Color.BLUE)
        model1.setCellColor(6, 4, Color.BLUE)
        model1.setCellColor(6, 5, Color.BLUE)
        model1.setCellColor(6, 6, Color.BLUE)
        model1.setCellColor(5, 7, Color.BLUE)
        model1.setCellColor(5, 8, Color.BLUE)

        assertEquals(-3, solver.countScore(model1))

        val model2 = Model()

        //Board view
        //   | 0 1 2 3 4 5 6 7 8 9 10
        // -------------------------
        // 0 | - - - - - - - - - - -
        // 1 | - B - - - - - - - - -
        // 2 | - B - - - - - - - - -
        // 3 | - B  - - - - - - - -
        // 4 | - B B - - - - - - - -
        // 5 | - - B B - - - - - - -
        // 6 | - - - B - - - - - - -
        // 7 | - - - B B - - - - - -
        // 8 | - R R R R R R - - - -
        // 9 | - - - - - - - - - - -
        // 10| - - - - - - - - - - -

        for (y in 1..4) model2.setCellColor(1, y, Color.BLUE)
        for (y in 4..5) model2.setCellColor(2, y, Color.BLUE)
        for (y in 5..7) model2.setCellColor(3, y, Color.BLUE)
        model2.setCellColor(4, 7, Color.BLUE)
        for (x in 1..6) model2.setCellColor(x, 8, Color.RED)

        assertEquals(1, solver.countScore(model2))
    }
}