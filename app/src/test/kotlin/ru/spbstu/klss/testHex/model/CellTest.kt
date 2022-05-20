package ru.spbstu.klss.testHex.model

import ru.spbstu.klss.hex.model.Cell
import ru.spbstu.klss.hex.model.Color
import org.junit.Test
import java.util.Random
import kotlin.test.*

class CellTest {

    @Test
    fun cellInitTest() {
        val random = Random()
        for (i in 0..100) {
            val x = random.nextInt()
            val y = random.nextInt()
            val cell = Cell(x, y)

            assertEquals(x, cell.x, message = "Wrong x coordinate initialization: expected $x, got ${cell.x}")
            assertEquals(y, cell.y, message = "Wrong y coordinate initialization: expected $y, got ${cell.y}")
            assertEquals(Color.GRAY, cell.color, message = "Wrong init color: expected ${Color.GRAY}, got ${cell.color}")
        }
        println("All clear")
    }

    @Test
    fun cellChangeColorTest() {
        val cellRedToBlue = Cell(0, 0)
        val cellBlueToRed = Cell(1, 1)

        assertEquals(Color.GRAY, cellRedToBlue.color, message = "Wrong init color: expected ${Color.GRAY}, got ${cellRedToBlue.color}")
        assertEquals(Color.GRAY, cellBlueToRed.color, message = "Wrong init color: expected ${Color.GRAY}, got ${cellBlueToRed.color}")

        cellRedToBlue.color = Color.RED
        cellBlueToRed.color = Color.BLUE

        assertEquals(Color.RED, cellRedToBlue.color, message = "Cell doen't change color from ${Color.GRAY} to ${Color.RED}")
        assertEquals(Color.BLUE, cellBlueToRed.color, message = "Cell doen't change color from ${Color.GRAY} to ${Color.BLUE}")

        cellRedToBlue.color = Color.BLUE
        cellBlueToRed.color = Color.RED

        assertEquals(Color.RED, cellRedToBlue.color, message = "Cell changes color from ${Color.RED} to ${Color.BLUE}, when doesn't")
        assertEquals(Color.BLUE, cellBlueToRed.color, message = "Cell changes color from ${Color.BLUE} to ${Color.RED}, when doesn't")

        cellRedToBlue.reset()
        cellBlueToRed.reset()

        cellRedToBlue.color = Color.BLUE
        cellBlueToRed.color = Color.RED

        assertEquals(Color.BLUE, cellRedToBlue.color)
        assertEquals(Color.RED, cellBlueToRed.color)

        println("All clear")
    }

    @Test
    fun cellConnectWithTest() {
        val random = Random()
        for (i in 0..100) {
            val cell1 = Cell(random.nextInt(), random.nextInt())
            val cell2 = Cell(random.nextInt(), random.nextInt())

            assertFalse(cell1 in cell2.neighbours, message = "Cells are connected before connection")
            assertFalse(cell2 in cell1.neighbours, message = "Cells are connected before connection")

            cell1.connectWith(cell2)

            assertTrue(cell1 in cell2.neighbours, message = "First cell is not in Second's neighbours")
            assertTrue(cell2 in cell1.neighbours, message = "Second cell is not in First's neighbours")
        }
        println("All clear")
    }



}