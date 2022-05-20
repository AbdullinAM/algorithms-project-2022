package tests

import junit.framework.Assert.*
import model.*
import org.junit.Test

class TestsForSolver {

    @Test
    fun criterion() {

        val solver = Solver(Game15(listener = null))

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
        val board = Board(array)
        val container = BoardContainer(null, board)
        assertEquals(0, solver.criterion(container))

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
        val board1 = Board(array)
        val container1 = BoardContainer(null, board1)
        assertEquals(26, solver.criterion(container1))
    }

    @Test
    fun wasInParent() {
        val solver = Solver(Game15(listener = null))

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
            Cell(3, 1, null),
            Cell(3, 2, 8),
            Cell(3, 3, 12)
        )

        var array = arrayOf(array0, array1, array2, array3)

        val container = BoardContainer(null, Board(array))

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

        array = arrayOf(array0, array1, array2, array3)

        val container1 = BoardContainer(container, Board(array))

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
            Cell(3, 2, 12),
            Cell(3, 3, null)
        )

        array = arrayOf(array0, array1, array2, array3)

        assertFalse(solver.wasInParent(container1, Board(array)))

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
            Cell(3, 1, null),
            Cell(3, 2, 8),
            Cell(3, 3, 12)
        )

        array = arrayOf(array0, array1, array2, array3)

        assertTrue(solver.wasInParent(container1, Board(array)))

    }

    @Test
    fun insertInPath() {
        val solver = Solver(Game15(listener = null))

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
            Cell(3, 1, null),
            Cell(3, 2, 8),
            Cell(3, 3, 12)
        )

        var array = arrayOf(array0, array1, array2, array3)

        val container = BoardContainer(null, Board(array))

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

        array = arrayOf(array0, array1, array2, array3)

        val container1 = BoardContainer(container, Board(array))

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
            Cell(3, 2, 12),
            Cell(3, 3, null)
        )

        array = arrayOf(array0, array1, array2, array3)

        val container2 = BoardContainer(container1, Board(array))

        val container3 = BoardContainer(container2, Board(array))

        val path = mutableListOf<Board>()
        path.add(0, container.board)
        path.add(1, container1.board)
        path.add(2, container2.board)

        solver.insertInPath(container3)

        assertEquals(path, solver.solvingPath)
    }

    @Test
    fun solver() {

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

        val field = Game15(listener = null)
        field.tilesField = array
        field.blankTile = array3[3]

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
            Cell(3, 1, null),
            Cell(3, 2, 8),
            Cell(3, 3, 12)
        )

        array = arrayOf(array0, array1, array2, array3)


        val checkableField = Game15(listener = null)
        checkableField.tilesField = array
        checkableField.blankTile = array3[1]

        val solver = Solver(checkableField)
        solver.solver()

        assertDeepContentEquals(field.tilesField, solver.solvingPath.last().tilesField)

    }

    private fun assertDeepContentEquals(array1: Array<Array<Cell>>?, array2: Array<Array<Cell>>?): Boolean {
        for (x in array1!!.indices)
            for (y in array1[x].indices)
                if (array1[x][y] != array2!![x][y]) return false
        return array1.size == array2!!.size
    }

}