package ru.spbstu.klss.hex.model

class Cell(val x: Int, val y: Int) {
    private var color: Color = Color.GREY
    private val neighbours: HashSet<Cell> = hashSetOf()

    fun connectWith(cell: Cell) {
        if (cell !in neighbours) {
            neighbours.add(cell)
            cell.connectWith(this)
        }
    }

    fun turnTo(color: Color): Boolean {
        if (color == Color.GREY) {
            this.color = color
            return true
        }
        return false
    }

}