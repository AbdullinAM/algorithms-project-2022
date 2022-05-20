package tests

import junit.framework.Assert.*
import model.Board
import model.Cell
import org.junit.Test

class TestsForBoard {

    @Test
    fun initTest() {
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
        val board1 = Board(array)
        assertEquals(board1.blankTile, Cell(3, 3, null))
        assertEquals(0, board1.criterion)

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
        val board2 = Board(array)
        board2.tilesField = array
        assertEquals(board2.blankTile, Cell(1, 3, null))
        assertEquals(26, board2.criterion)
    }

    @Test
    fun checkWin() {
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
        val board1 = Board(array)
        assertTrue(board1.checkWin())

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
        val board2 = Board(array)
        board2.tilesField = array
        assertFalse(board2.checkWin())
    }

    @Test
    fun newField() {
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
        val board1 = Board(array)
        assertDeepContentEquals(board1.tilesField, board1.newField())

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
        val board2 = Board(array)
        board2.tilesField = array
        assertDeepContentEquals(board2.tilesField, board2.newField())
    }

    @Test
    fun neighbours() {
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

        array0 = arrayOf(
            Cell(0, 0, 1),
            Cell(0, 1, 5),
            Cell(0, 2, 9),
            Cell(0, 3, 13)
        )
        array1 = arrayOf(
            Cell(1, 0, 2),
            Cell(1, 1, 6),
            Cell(1, 2, 10),
            Cell(1, 3, 14)
        )
        array2 = arrayOf(
            Cell(2, 0, 3),
            Cell(2, 1, 7),
            Cell(2, 2, 11),
            Cell(2, 3, 15)
        )
        array3 = arrayOf(
            Cell(3, 0, 4),
            Cell(3, 1, 8),
            Cell(3, 2, null),
            Cell(3, 3, 12)
        )
        var arrayNeighbour1 = arrayOf(array0, array1, array2, array3)

        array0 = arrayOf(
            Cell(0, 0, 1),
            Cell(0, 1, 5),
            Cell(0, 2, 9),
            Cell(0, 3, 13)
        )
        array1 = arrayOf(
            Cell(1, 0, 2),
            Cell(1, 1, 6),
            Cell(1, 2, 10),
            Cell(1, 3, 14)
        )
        array2 = arrayOf(
            Cell(2, 0, 3),
            Cell(2, 1, 7),
            Cell(2, 2, 11),
            Cell(2, 3, null)
        )
        array3 = arrayOf(
            Cell(3, 0, 4),
            Cell(3, 1, 8),
            Cell(3, 2, 12),
            Cell(3, 3, 15)
        )
        var arrayNeighbour2 = arrayOf(array0, array1, array2, array3)

        val board1 = Board(array)

        val boardList1: MutableSet<Board?> = HashSet()
        boardList1.add(Board(arrayNeighbour1))
        boardList1.add(Board(arrayNeighbour2))
        boardList1.add(null)
        assertEquals(board1.neighbors(), boardList1)

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

        array0 = arrayOf(
            Cell(0, 0, 1),
            Cell(0, 1, 3),
            Cell(0, 2, 9),
            Cell(0, 3, 15)
        )
        array1 = arrayOf(
            Cell(1, 0, 2),
            Cell(1, 1, 4),
            Cell(1, 2, null),
            Cell(1, 3, 10)
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
        arrayNeighbour1 = arrayOf(array0, array1, array2, array3)

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
            Cell(1, 3, 8)
        )
        array2 = arrayOf(
            Cell(2, 0, 7),
            Cell(2, 1, 11),
            Cell(2, 2, 12),
            Cell(2, 3, null)
        )
        array3 = arrayOf(
            Cell(3, 0, 6),
            Cell(3, 1, 5),
            Cell(3, 2, 13),
            Cell(3, 3, 14)
        )

        arrayNeighbour2 = arrayOf(array0, array1, array2, array3)

        array0 = arrayOf(
            Cell(0, 0, 1),
            Cell(0, 1, 3),
            Cell(0, 2, 9),
            Cell(0, 3, null)
        )
        array1 = arrayOf(
            Cell(1, 0, 2),
            Cell(1, 1, 4),
            Cell(1, 2, 10),
            Cell(1, 3, 15)
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

        val arrayNeighbour3 = arrayOf(array0, array1, array2, array3)

        val board2 = Board(array)
        val boardList2: MutableSet<Board?> = HashSet()
        boardList2.add(Board(arrayNeighbour1))
        boardList2.add(Board(arrayNeighbour2))
        boardList2.add(Board(arrayNeighbour3))
        boardList2.add(null)
        assertEquals(board2.neighbors(), boardList2)
    }



    private fun assertDeepContentEquals(array1: Array<Array<Cell>>?, array2: Array<Array<Cell>>?): Boolean {
        for (x in array1!!.indices)
            for (y in array1[x].indices)
                if (array1[x][y] != array2!![x][y]) return false
        return array1.size == array2!!.size
    }

}