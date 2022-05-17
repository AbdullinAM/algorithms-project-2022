package ru.spbstu.klss.testHex.model

import ru.spbstu.klss.hex.model.Color
import org.junit.Test
import ru.spbstu.klss.hex.model.Model
import java.util.Random
import kotlin.test.*

class ModelTest {

    @Test
    fun modelInitTest() {
        val model = Model()

        assertEquals(11 * 11, model.board.size, "Size of the board is not 11 * 11. Got ${model.board.size} sections")

        for (x in 0..10) {
            for (y in 0..10) {
                val cell = model.getCell(x, y)

                // Checking these (X) neighbours of cell (C)
                //  X   Х   O
                //  Х   С   Х
                //  O   Х   X

                try {
                    if (x > 0 && y > 0) assertTrue(cell in model.getCell(x-1, y-1).neighbours)
                    if (y > 0) assertTrue(cell in model.getCell(x, y-1).neighbours)
                    if (x < 10 && y > 0) assertFalse(cell in model.getCell(x+1, y-1).neighbours)
                    if (x > 0) assertTrue(cell in model.getCell(x-1, y).neighbours)
                    if (x < 10) assertTrue(cell in model.getCell(x+1, y).neighbours)
                    if (x > 0 && y < 10) assertFalse(cell in model.getCell(x-1, y+1).neighbours)
                    if (y < 10) assertTrue(cell in model.getCell(x, y+1).neighbours)
                    if (x < 10 && y < 10) assertTrue(cell in model.getCell(x+1, y+1).neighbours)
                } catch (error: AssertionError) {
                    throw AssertionError("Invalid neighbours connection with cell $x, $y")
                }
            }
        }
    }

    @Test
    fun setCellColorTest() {
        val random = Random()

        for (i in 0..100) {
            val model = Model()
            val x = random.nextInt(0, 11)
            val y = random.nextInt(0, 11)

            assertEquals(Color.GRAY, model.getCell(x, y).color, "Wrong init cell color. Expected ${Color.GRAY}, got ${model.getCell(x, y).color}")

            model.setCellColor(x, y, Color.BLUE)

            assertEquals(Color.BLUE, model.getCell(x, y).color, message = "Cell doen't change color from ${Color.GRAY} to ${Color.BLUE}")

            model.setCellColor(x, y, Color.RED)

            assertEquals(Color.BLUE, model.getCell(x, y).color, message = "Cell changes color from ${Color.RED} to ${Color.BLUE}, when doesn't")
        }
        println("All clear")
    }

    @Test
    fun getCellEqualityTest() {
        val random = Random()
        val model = Model()

        for (i in 0..100) {
            val x = random.nextInt(0, 11)
            val y = random.nextInt(0, 11)
            val index = y * 11 + x

            assertEquals(model.getCell(index), model.getCell(x, y), message = "Cells from getCell by index and getCell by x and y are not the same")
        }
        println("All clear")
    }

    @Test
    fun findWinnerTest() {
        val random = Random()

        val emptyModel = Model()
        for (x in 0..10) {
            val cell = emptyModel.getCell(x, 0)
            assertFalse(emptyModel.hasPath(cell, Color.BLUE))
        }
        for (y in 0..10) {
            val cell = emptyModel.getCell(0, y)
            assertFalse(emptyModel.hasPath(cell, Color.RED))
        }
        assertFalse(emptyModel.isWinner(Color.RED))
        assertFalse(emptyModel.isWinner(Color.BLUE))

        val straightRedLineModel = Model()
        val y_red = random.nextInt(0, 11)
        for (x in 0..10) {
            straightRedLineModel.setCellColor(x, y_red, Color.RED)
        }
        val cell_red = straightRedLineModel.getCell(0, y_red)
        assertTrue(straightRedLineModel.hasPath(cell_red, Color.RED))
        assertTrue(straightRedLineModel.isWinner(Color.RED))
        assertFalse(straightRedLineModel.isWinner(Color.BLUE))

        val straightBlueLineModel = Model()
        val x_blue = random.nextInt(0, 11)
        for (y in 0..10) {
            straightBlueLineModel.setCellColor(x_blue, y, Color.BLUE)
        }
        val cell_blue = straightBlueLineModel.getCell(x_blue, 0)
        assertTrue(straightBlueLineModel.hasPath(cell_blue, Color.BLUE))
        assertFalse(straightBlueLineModel.isWinner(Color.RED))
        assertTrue(straightBlueLineModel.isWinner(Color.BLUE))

        // Complex examples
        val redCellCoords = listOf( // Pair <Y, X>
            Pair(1, 8), Pair(2, 8),
            Pair(2, 9), Pair(2, 5),
            Pair(3, 1), Pair(3, 2),
            Pair(3, 4), Pair(3, 8),
            Pair(3, 10), Pair(4, 2),
            Pair(4, 5), Pair(4, 8),
            Pair(5, 2), Pair(5, 3),
            Pair(5, 4), Pair(5, 5),
            Pair(5, 6), Pair(5, 8),
            Pair(6, 5), Pair(6, 7),
            Pair(6, 8), Pair(7, 2),
            Pair(7, 5), Pair(7, 8),
            Pair(8, 5), Pair(8, 0),
            Pair(9, 1), Pair(9, 2),
            Pair(9, 3), Pair(9, 4),
            Pair(9, 5), Pair(9, 7),
            Pair(10, 7)
        )

        val complexModel = Model()
        for (pair in redCellCoords) {
            complexModel.setCellColor(pair.second, pair.first, Color.RED)
        }
        val cell = complexModel.getCell(0, 8)
        assertTrue(complexModel.hasPath(cell, Color.RED))
        assertTrue(complexModel.isWinner(Color.RED))
        assertFalse(complexModel.isWinner(Color.BLUE))
    }
}