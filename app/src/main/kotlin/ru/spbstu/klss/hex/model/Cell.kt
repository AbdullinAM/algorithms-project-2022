package ru.spbstu.klss.hex.model

class Cell(val x: Int, val y: Int) {
    private var color: Color = Color.GRAY
        set(color: Color) {
            if (field == Color.GRAY) {
                field = color
            }
        }

    private val neighbours: HashSet<Cell> = hashSetOf()

    fun connectWith(cell: Cell) {
        if (cell !in neighbours) {
            neighbours.add(cell)
            cell.connectWith(this)
        }
    }
}