package ru.spbstu.klss.hex.model

class Board {

    val board: MutableList<Cell>

    init {
        this.board = mutableListOf<Cell>()
        for (y in 0..10) {
            for (x in 0..10) {
                this.board.add(Cell(x, y))
                if (x > 0) getCell(x, y).connectWith(getCell(x - 1, y))
                if (y > 0) getCell(x, y).connectWith(getCell(x, y - 1))
                if (x < 10 && y > 0) getCell(x, y).connectWith(getCell(x + 1, y - 1))
            }
        }
    }

    fun getCell(index: Int): Cell = board[index]
    fun getCell(x: Int, y: Int): Cell {
        val index = 11 * y + x
        return getCell(index)
    }

}