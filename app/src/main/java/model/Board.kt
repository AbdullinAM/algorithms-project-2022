package model

import java.util.*
import kotlin.math.abs

class Board(field: Array<Array<Cell>>?) {
    var tilesField = field
    lateinit var blankTile: Cell
    var criterion: Int = 0

    init {
        criterion = 0
        for (x in 0..3) {
            for (y in 0..3) {
                if (tilesField!![x][y].value != null) {
                    for (k in 1..15) {
                        if (k == tilesField!![x][y].value) {
                            val x0 = when {
                                k < 5 -> (k - 1) % 4
                                k < 9 -> (k - 5) % 4
                                k < 13 -> (k - 9) % 4
                                else -> (k - 13) % 4
                            }
                            val y0 = (k - 1) / 4
                            criterion += abs(x - x0) + abs(y - y0)
                        }
                    }
                }
                if (tilesField!![x][y].value == null)
                    blankTile = tilesField!![x][y]
            }
        }
        println("Criterion: $criterion")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is Board) return false
        for (x in 0..3) {
            for (y in 0..3) {
                if (tilesField!![x][y] != other.tilesField!![x][y]) {
                    return false
                }
            }
        }
        return true
    }

    // Проверка массивов на совпадение номеров элементов и содержимого
    override fun hashCode(): Int {
        return tilesField.contentDeepHashCode()
    }

    // Если критерий неупорядоченности = 0, то поле в выигрышной позиции
    fun checkWin(): Boolean {
        return criterion == 0
    }

    // Ищем соседей у Board
    fun neighbors(): Iterable<Board?> {
        val boardList: MutableSet<Board?> = HashSet()

        boardList.add(changePositions(newField(), blankTile.posX, blankTile.posY, blankTile.posX, blankTile.posY + 1))

        boardList.add(changePositions(newField(), blankTile.posX, blankTile.posY, blankTile.posX, blankTile.posY - 1))

        boardList.add(changePositions(newField(), blankTile.posX, blankTile.posY, blankTile.posX - 1, blankTile.posY))

        boardList.add(changePositions(newField(), blankTile.posX, blankTile.posY, blankTile.posX + 1, blankTile.posY))
        return boardList
    }

    private fun changePositions(field: Array<Array<Cell>>?, x1: Int, y1: Int, x2: Int, y2: Int): Board? {
        return if (x2 > -1 && x2 < 4 && y2 > -1 && y2 < 4) {
            val t = field!![x2][y2].value
            field[x2][y2].value = field[x1][y1].value
            field[x1][y1].value = t
            Board(field)
        } else null
    }

    fun newField(): Array<Array<Cell>>? = deepCopy(tilesField)

    // Копия поля для сохранения значения ссылочной переменной
    private fun deepCopy(original: Array<Array<Cell>>?): Array<Array<Cell>>? {
        if (original == null) {
            return null
        }
        val result = Array(original.size) { x ->
            Array(original.size) { y -> Cell(x, y, null) }
        }
        for (x in 0..3) {
            for (y in 0..3) {
                result[x][y] = Cell(x, y, original[x][y].value)
            }
        }
        return result
    }
}