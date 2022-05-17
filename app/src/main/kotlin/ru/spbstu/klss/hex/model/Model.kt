package ru.spbstu.klss.hex.model

class Model {

    val board: MutableList<Cell>

    init {
        this.board = mutableListOf<Cell>()
        for (y in 0..10) {
            for (x in 0..10) {
                this.board.add(Cell(x, y))
                if (x > 0) getCell(x, y).connectWith(getCell(x - 1, y))
                if (y > 0) getCell(x, y).connectWith(getCell(x, y - 1))
                if (x > 0 && y > 0) getCell(x, y).connectWith(getCell(x - 1, y - 1))
            }
        }
    }

    override fun toString(): String {
        var result = ""
        for (y in 0..10) {
            var row = ""
            for (x in 0..10) {
                val cell = getCell(x, y)
                when(cell.color) {
                    Color.GRAY -> row += "- "
                    Color.RED -> row += "R "
                    Color.BLUE -> row += "B "
                }
            }
            row += "\n"
            result += row
        }
        return result
    }

    fun getCell(index: Int): Cell = board[index]

    fun getCell(x: Int, y: Int): Cell {
        val index = 11 * y + x
        return getCell(index)
    }

    fun setCellColor(cell: Cell, color: Color) {
        cell.color = color
    }

    fun setCellColor(index: Int, color: Color) {
        getCell(index).color = color
    }

    fun setCellColor(x: Int, y: Int, color: Color) {
        getCell(x, y).color = color
    }

    fun isWinner(color: Color): Boolean {
        if (color == Color.RED) {
            for (y in 0..10) {
                val cell = getCell(0, y)
                if (hasPath(cell, color)) return true
            }
        } else if (color == Color.BLUE) {
            for (x in 0..10) {
                val cell = getCell(x, 0)
                if (hasPath(cell, color)) return true
            }
        }
        return false
    }

    fun hasPath(cell: Cell, color: Color): Boolean {
        val stack = ArrayDeque<Pair<Cell, MutableIterator<Cell>>>()
        var currentCell = cell
        var currentIterator = currentCell.neighbours.iterator()
        val visited = mutableListOf<Cell>(currentCell)
        stack.add(Pair(currentCell, currentIterator))

        while (stack.isNotEmpty()) {
            currentCell = stack.last().first
            currentIterator = stack.last().second
            while (currentIterator.hasNext()) {
                val nextCell = currentIterator.next()
                if (nextCell.color != color || nextCell in visited) continue

                if (nextCell.color == Color.BLUE && nextCell.y == 10) return true
                if (nextCell.color == Color.RED && nextCell.x == 10) return true

                visited.add(currentCell)
                currentCell = nextCell
                currentIterator = nextCell.neighbours.iterator()
                stack.add(Pair(currentCell, currentIterator))
            }
            stack.removeLast()
        }
        return false
    }
}