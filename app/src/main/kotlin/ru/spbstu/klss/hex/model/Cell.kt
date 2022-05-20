package ru.spbstu.klss.hex.model

class Cell(val x: Int, val y: Int) {
    var color: Color = Color.GRAY
        set(color) {
            if (field == Color.GRAY) {
                field = color
            }
        }

    val neighbours: HashSet<Cell> = hashSetOf()

    fun connectWith(cell: Cell) {
        if (cell !in neighbours) {
            neighbours.add(cell)
            cell.connectWith(this)
        }
    }

    fun copy(): Cell {
        val result = Cell(x, y)
        result.color = color
        return result
    }

    fun reset() {
        color = Color.GRAY
    }
}