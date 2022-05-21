package ru.spbstu.klss.testHex.solver

import org.junit.Test
import ru.spbstu.klss.hex.model.Cell
import ru.spbstu.klss.hex.model.Color
import ru.spbstu.klss.hex.model.Model
import ru.spbstu.klss.hex.solver.SemaSolver
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SemaSolverTest {

    @Test
    fun getCellsTest() {
        val model = Model()
        val random = Random()
        val solver = SemaSolver(Color.RED)
        val visited = mutableListOf<Cell>()

        for (i in 0..120) {
            val randomX = random.nextInt(0, 11)
            val randomY = random.nextInt(0, 11)

            if (model.getCell(randomX, randomY) !in visited) {
                val cell = model.getCell(randomX, randomY)
                visited.add(cell)
                model.setCellColor(cell, Color.RED)
            }

            for (cell in visited) {
                assertFalse(cell in solver.getCells(model.board, Color.GRAY))
                assertTrue(cell in solver.getCells(model.board, Color.RED))
                assertTrue(solver.getCells(model.board, Color.BLUE).isEmpty())
            }

        }
        println("All clear")
    }

    @Test
    fun isBridgeTest() {
        val random = Random()
        val solver = SemaSolver(Color.RED)
        val model = Model()

        for (i in 0..100) {
            val x = random.nextInt(2, 8)
            val y = random.nextInt(2, 8)

            // Board view
            // - 4 - - -
            // 3 - - 2 -
            // - - C - -
            // - 1 - - 6
            // - - - 5 -
            // Where numbers are bridgeCells, C - centerCell

            val centerCell = model.getCell(x, y)
            val bridgeCells = mutableListOf(
                model.getCell(x - 1, y + 1),
                model.getCell(x + 1, y - 1),
                model.getCell(x - 2, y - 1),
                model.getCell(x - 1, y - 2),
                model.getCell(x + 1, y + 2),
                model.getCell(x + 2, y + 1)
            )

            centerCell.color = Color.BLUE

            for (element in bridgeCells) {
                assertFalse(solver.isDiagonal(centerCell, element))
                element.color = Color.RED
                assertFalse(solver.isDiagonal(centerCell, element))
                element.reset()
                element.color = Color.BLUE
                assertTrue(solver.isDiagonal(centerCell, element))
                element.reset()
            }

            centerCell.reset()
            println("All clear")
        }
    }

    @Test
    fun isBlockedBridgeTest() {
        val random = Random()
        val solver = SemaSolver(Color.RED)

        for (i in 0..100) {
            val model = Model()
            val x = random.nextInt(2, 9)
            val y = random.nextInt(2, 9)

            // Board view
            // - 4 - - -
            // 3 - - 2 -
            // - - C - -
            // - 1 - - 6
            // - - - 5 -
            // Where numbers are bridgeCells, C - centerCell

            val centerCell = model.getCell(x, y)
            val bridgeCells = mutableListOf(
                model.getCell(x - 1, y + 1),
                model.getCell(x + 1, y - 1),
                model.getCell(x - 2, y - 1),
                model.getCell(x - 1, y - 2),
                model.getCell(x + 1, y + 2),
                model.getCell(x + 2, y + 1)
            )

            centerCell.color = Color.RED
            for (element in bridgeCells) {
                assertFalse(solver.isBlockedDiagonal(centerCell, element, model.board))
                element.color = Color.RED
                assertFalse(solver.isBlockedDiagonal(centerCell, element, model.board))
                assertTrue(solver.isDiagonal(centerCell, element))
            }

            // Board view
            // - 4 - - -
            // 3 B B 2 -
            // - R R B -
            // - 1 R B 6
            // - - - 5 -

            model.setCellColor(x - 1, y - 1, Color.BLUE)
            model.setCellColor(x - 1, y, Color.RED)
            model.setCellColor(x, y - 1, Color.BLUE)
            model.setCellColor(x, y + 1, Color.RED)
            model.setCellColor(x + 1, y, Color.BLUE)
            model.setCellColor(x + 1, y + 1, Color.BLUE)

            assertTrue(solver.isBlockedDiagonal(centerCell, bridgeCells[3], model.board))
            assertTrue(solver.isBlockedDiagonal(centerCell, bridgeCells[1], model.board))
            assertTrue(solver.isBlockedDiagonal(centerCell, bridgeCells[5], model.board))
            assertFalse(solver.isBlockedDiagonal(centerCell, bridgeCells[2], model.board))
            assertFalse(solver.isBlockedDiagonal(centerCell, bridgeCells[0], model.board))
            assertFalse(solver.isBlockedDiagonal(centerCell, bridgeCells[4], model.board))
        }
        println("All clear")
    }

    @Test
    fun isSavedBridge() {
        val random = Random()
        val solver = SemaSolver(Color.RED)

        for (i in 0..100) {
            val model = Model()
            val x = random.nextInt(2, 9)
            val y = random.nextInt(2, 9)

            // Board view
            // - 4 - - -
            // 3 - - 2 -
            // - - C - -
            // - 1 - - 6
            // - - - 5 -
            // Where numbers are bridgeCells, C - centerCell

            val centerCell = model.getCell(x, y)
            val bridgeCells = mutableListOf(
                model.getCell(x - 1, y + 1),
                model.getCell(x + 1, y - 1),
                model.getCell(x - 2, y - 1),
                model.getCell(x - 1, y - 2),
                model.getCell(x + 1, y + 2),
                model.getCell(x + 2, y + 1)
            )

            centerCell.color = Color.RED
            for (element in bridgeCells) {
                assertFalse(solver.isSavedDiagonal(centerCell, element, model.board))
                element.color = Color.RED
                assertFalse(solver.isSavedDiagonal(centerCell, element, model.board))
                assertTrue(solver.isDiagonal(centerCell, element))
            }

            // Board view
            // - 4 - - -
            // 3 B B 2 -
            // - R R B -
            // - 1 R B 6
            // - - - 5 -

            model.setCellColor(x - 1, y - 1, Color.BLUE)
            model.setCellColor(x - 1, y, Color.RED)
            model.setCellColor(x, y - 1, Color.BLUE)
            model.setCellColor(x, y + 1, Color.RED)
            model.setCellColor(x + 1, y, Color.BLUE)
            model.setCellColor(x + 1, y + 1, Color.BLUE)

            assertFalse(solver.isSavedDiagonal(centerCell, bridgeCells[3], model.board))
            assertFalse(solver.isSavedDiagonal(centerCell, bridgeCells[1], model.board))
            assertFalse(solver.isSavedDiagonal(centerCell, bridgeCells[5], model.board))
            assertTrue(solver.isSavedDiagonal(centerCell, bridgeCells[2], model.board))
            assertFalse(solver.isSavedDiagonal(centerCell, bridgeCells[0], model.board))
            assertTrue(solver.isSavedDiagonal(centerCell, bridgeCells[4], model.board))
        }
        println("All clear")
    }

    @Test
    fun countBridgesTest(){
        val emptyModel = Model()
        val solver = SemaSolver(Color.RED)

        assertEquals(0, solver.diagonalsCount(emptyModel.board))

        val model1 = Model()
        //Board view
        // - - - - - - - - - - -
        // - - - - - - - - - - -
        // - - - - - - - - - - -
        // - R - - - - - - - - -
        // - R R - - R - - - - -
        // - B R R R - - - - - -
        // - B B B B R - - - - -
        // - R - - R - - - - - -
        // - - - - - - - - - - -
        // - - - - - - - - - - -
        // - - - - - - - - - - -

        model1.setCellColor(1, 3, Color.RED)
        model1.setCellColor(1, 4, Color.RED)
        model1.setCellColor(1, 5, Color.BLUE)
        model1.setCellColor(1, 6, Color.BLUE)
        model1.setCellColor(1, 7, Color.RED)
        model1.setCellColor(2, 4, Color.RED)
        model1.setCellColor(2, 5, Color.RED)
        model1.setCellColor(2, 6, Color.BLUE)
        model1.setCellColor(3, 5, Color.RED)
        model1.setCellColor(3, 6, Color.BLUE)
        model1.setCellColor(4, 5, Color.RED)
        model1.setCellColor(4, 6, Color.BLUE)
        model1.setCellColor(4, 7, Color.RED)
        model1.setCellColor(5, 4, Color.RED)
        model1.setCellColor(5, 6, Color.RED)

        assertEquals(6, solver.diagonalsCount(model1.board))
    }

    @Test
    fun countSavedBridgesTest(){
        val emptyModel = Model()
        val solver = SemaSolver(Color.RED)

        assertEquals(0, solver.countSavedDiagonals(emptyModel.board, Color.BLUE))

        val model1 = Model()
        //Board view
        // - - - - - - - - - - -
        // - - - - - - - - - - -
        // - - - - - - - - - - -
        // - R - - - - - - - - -
        // R R R - - R - - - - -
        // R B R R R - - - - - -
        // - B B B B R - - - - -
        // - R - - R R - - - - -
        // - - - - - - - - - - -
        // - - - - - - - - - - -
        // - - - - - - - - - - -

        model1.setCellColor(0, 4, Color.RED)
        model1.setCellColor(0, 5, Color.RED)
        model1.setCellColor(1, 3, Color.RED)
        model1.setCellColor(1, 4, Color.RED)
        model1.setCellColor(1, 5, Color.BLUE)
        model1.setCellColor(1, 6, Color.BLUE)
        model1.setCellColor(1, 7, Color.RED)
        model1.setCellColor(2, 4, Color.RED)
        model1.setCellColor(2, 5, Color.RED)
        model1.setCellColor(2, 6, Color.BLUE)
        model1.setCellColor(3, 5, Color.RED)
        model1.setCellColor(3, 6, Color.BLUE)
        model1.setCellColor(4, 5, Color.RED)
        model1.setCellColor(4, 6, Color.BLUE)
        model1.setCellColor(4, 7, Color.RED)
        model1.setCellColor(5, 4, Color.RED)
        model1.setCellColor(5, 6, Color.RED)
        model1.setCellColor(5, 7, Color.RED)

        assertEquals(6, solver.countSavedDiagonals(model1.board, Color.RED))
    }

    @Test
    fun countMaxLength() {
        val emptyModel = Model()
        val solver = SemaSolver(Color.RED)

        assertEquals(0, solver.countMaxLength(emptyModel.board, Color.RED))

        val model1 = Model()

        //Board view
        // - - - - - - - - - - -
        // - - - - - - - - - - -
        // - - - R R R R R R R -
        // - - - R - - R - - - -
        // - - - R - R - - - - -
        // - - - R R - - - - - -
        // - - - - R - - - - - -
        // R - - - - R - - - - -
        // - R R R R R R - - - -
        // - - - - - - - - - - -
        // - - - - - - - - - - -

        for (x in 1..6) model1.setCellColor(x, 8, Color.RED)
        for (x in 3..7) model1.setCellColor(x, 2, Color.RED)
        for (y in 2..5) model1.setCellColor(3, y, Color.RED)
        model1.setCellColor(6, 3, Color.RED)
        model1.setCellColor(5, 4, Color.RED)
        model1.setCellColor(4, 5, Color.RED)
        model1.setCellColor(4, 6, Color.RED)
        model1.setCellColor(5, 7, Color.RED)
        model1.setCellColor(0, 7, Color.RED)
        model1.setCellColor(8, 2, Color.RED)
        model1.setCellColor(9, 2, Color.RED)


        assertEquals(11, solver.countMaxLength(model1.board, Color.RED))
    }
}