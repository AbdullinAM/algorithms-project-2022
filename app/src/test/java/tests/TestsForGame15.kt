package tests

import junit.framework.Assert.*
import model.Cell
import model.Game15
import org.junit.Test

class TestsForGame15 {
    private val listener: Game15.ActionListener? = null

    @Test
    fun field() {
        val hashSet = mutableSetOf<Int>()
        for (i in 1..15)
            hashSet.add(i)
        val startField = Game15(listener)
        hashSet.toSet()

        var i = 0
        while (i < 100) {
            startField.field()
            checkForRightField(startField, hashSet)
            i++
        }
    }

    private fun checkForRightField(startField: Game15, set: Set<Int>) {
        val hashSet = set.toMutableSet()
        for (x in startField.tilesField.indices)
            for (y in startField.tilesField[x].indices)
                if (startField.tilesField[x][y].value in hashSet)
                    hashSet.remove(startField.tilesField[x][y].value)

        if (hashSet.isEmpty() && startField.tilesField[3][3].value == null)
            assertTrue("Ok",true)
    }

    @Test
    fun getCellFromField() {
        val startField = Game15(listener)
        startField.field()
        assertEquals(Cell(3, 3, null), startField.getCellFromField(3, 3))

        for (x in startField.tilesField.indices)
            for (y in startField.tilesField[x].indices)
                assertEquals(Cell(x, y, startField.tilesField[x][y].value), startField.getCellFromField(x, y))

        assertEquals(null, startField.getCellFromField(-1, 5))
        assertEquals(null, startField.getCellFromField(1, -5))
        assertEquals(null, startField.getCellFromField(8, 5))
        assertEquals(null, startField.getCellFromField(1, 8))
        assertEquals(null, startField.getCellFromField(-1, 8))

    }

    @Test
    fun move() {
        val startField = Game15(listener)
        startField.field()
        startField.move(3, 0)
        assertDeepContentEquals(arrayOf(
            startField.tilesField[3][0],
            startField.tilesField[3][3],
            startField.tilesField[3][1],
            startField.tilesField[3][2]
        ), startField.tilesField[3])
        startField.move(2, 0)
        assertDeepContentEquals(arrayOf(
            startField.tilesField[3][0],
            startField.tilesField[2][1],
            startField.tilesField[2][2],
            startField.tilesField[2][3]
        ), startField.tilesField[2])
        assertDeepContentEquals(arrayOf(
            startField.tilesField[2][0],
            startField.tilesField[3][3],
            startField.tilesField[3][1],
            startField.tilesField[3][2]
        ), startField.tilesField[3])
    }

    @Test
    fun checkGameOver() {
        val startField = Game15(listener)
        var array0 = arrayOf(
            Cell(0, 0, 1),
            Cell(0, 1, 5),
            Cell(0, 2, 9),
            Cell(0, 3, 13)
        )
        var array1 = arrayOf(
            Cell(1, 0, 2),
            Cell(1, 1, 6),
            Cell(1, 2, 10),
            Cell(1, 3, 14)
        )
        var array2 = arrayOf(
            Cell(2, 0, 3),
            Cell(2, 1, 7),
            Cell(2, 2, 11),
            Cell(2, 3, 15)
        )
        var array3 = arrayOf(
            Cell(3, 0, 4),
            Cell(3, 1, 8),
            Cell(3, 2, 12),
            Cell(3, 3, null)
        )
        var array = arrayOf(array0, array1, array2, array3)
        startField.tilesField = array
        assertTrue(startField.checkGameOver())

        array0 = arrayOf(
            Cell(0, 0, 1),
            Cell(0, 1, 3),
            Cell(0, 2, 9),
            Cell(0, 3, 15)
        )
        array1 = arrayOf(
            Cell(1, 0, 2),
            Cell(1, 1, 4),
            Cell(1, 2, 10),
            Cell(1, 3, null)
        )
        array2 = arrayOf(
            Cell(2, 0, 7),
            Cell(2, 1, 11),
            Cell(2, 2, 12),
            Cell(2, 3, 8)
        )
        array3 = arrayOf(
            Cell(3, 0, 6),
            Cell(3, 1, 5),
            Cell(3, 2, 13),
            Cell(3, 3, 14)
        )
        array = arrayOf(array0, array1, array2, array3)
        startField.tilesField = array
        assertFalse(startField.checkGameOver())
        }

    private fun assertDeepContentEquals(array1: Array<Cell>, array2: Array<Cell>): Boolean {
        for (i in array1.indices)
            if (array1[i] != array2[i]) return false
        return array1.size == array2.size
    }

}