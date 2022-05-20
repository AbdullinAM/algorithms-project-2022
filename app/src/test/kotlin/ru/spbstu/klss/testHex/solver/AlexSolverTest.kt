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
    fun isBridgeTest() {
        val random = Random()
        val alexSolver = AlexSolver(Color.RED)

        for (i in 0..100) {
            val x = random.nextInt()
            val y = random.nextInt()

            // Board view
            // - 4 - - -
            // 3 - - 2 -
            // - - C - -
            // - 1 - - 6
            // - - - 5 -
            // Where numbers are bridgeCells, C - centerCell

            val centerCell = Cell(x, y)
            val bridgeCells = mutableListOf(
                Cell(x - 1, y + 1),
                Cell(x + 1, y - 1),
                Cell(x - 2, y - 1),
                Cell(x - 1, y - 2),
                Cell(x + 1, y + 2),
                Cell(x + 2, y + 1)
            )

            for (element in bridgeCells) {
                assertFalse(alexSolver.isBridge(centerCell, element))
            }

            centerCell.color = Color.BLUE

            for (element in bridgeCells) {
                assertFalse(alexSolver.isBridge(centerCell, element))
                element.color = Color.RED
                assertFalse(alexSolver.isBridge(centerCell, element))
                element.reset()
                element.color = Color.BLUE
                assertTrue(alexSolver.isBridge(centerCell, element))
            }
            println("All clear")
        }
    }

    @Test
    fun isBlockedBridgeTest() {
        val random = Random()
        val alexSolver = AlexSolver(Color.RED)

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
                assertFalse(alexSolver.isBlockedBridge(centerCell, element, model.board))
                element.color = Color.RED
                assertFalse(alexSolver.isBlockedBridge(centerCell, element, model.board))
                assertTrue(alexSolver.isBridge(centerCell, element))
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

            assertTrue(alexSolver.isBlockedBridge(centerCell, bridgeCells[3], model.board))
            assertTrue(alexSolver.isBlockedBridge(centerCell, bridgeCells[1], model.board))
            assertTrue(alexSolver.isBlockedBridge(centerCell, bridgeCells[5], model.board))
            assertFalse(alexSolver.isBlockedBridge(centerCell, bridgeCells[2], model.board))
            assertFalse(alexSolver.isBlockedBridge(centerCell, bridgeCells[0], model.board))
            assertFalse(alexSolver.isBlockedBridge(centerCell, bridgeCells[4], model.board))
        }
        println("All clear")
    }

    @Test
    fun isSavedBridge() {
        val random = Random()
        val alexSolver = AlexSolver(Color.RED)

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
                assertFalse(alexSolver.isSavedBridge(centerCell, element, model.board))
                element.color = Color.RED
                assertFalse(alexSolver.isSavedBridge(centerCell, element, model.board))
                assertTrue(alexSolver.isBridge(centerCell, element))
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

            assertFalse(alexSolver.isSavedBridge(centerCell, bridgeCells[3], model.board))
            assertFalse(alexSolver.isSavedBridge(centerCell, bridgeCells[1], model.board))
            assertFalse(alexSolver.isSavedBridge(centerCell, bridgeCells[5], model.board))
            assertTrue(alexSolver.isSavedBridge(centerCell, bridgeCells[2], model.board))
            assertFalse(alexSolver.isSavedBridge(centerCell, bridgeCells[0], model.board))
            assertTrue(alexSolver.isSavedBridge(centerCell, bridgeCells[4], model.board))
        }
        println("All clear")
    }

    @Test
    fun countBridgesTest(){
        val emptyModel = Model()
        val alexSolver = AlexSolver(Color.RED)

        assertEquals(0, alexSolver.countBridges(emptyModel.board, Color.BLUE))

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

        assertEquals(6, alexSolver.countBridges(model1.board, Color.RED))
    }

    @Test
    fun countSavedBridgesTest(){
        val emptyModel = Model()
        val alexSolver = AlexSolver(Color.RED)

        assertEquals(0, alexSolver.countSavedBridges(emptyModel.board, Color.BLUE))

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

        assertEquals(4, alexSolver.countSavedBridges(model1.board, Color.RED))
    }
}