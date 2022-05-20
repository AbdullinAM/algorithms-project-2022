package model

import kotlin.random.Random

class Game15(listener: ActionListener?) {
    var tilesField = Array(4) { x ->
        Array(4) { y -> Cell(x, y, null) }
    }
    private val numbers = hashSetOf<Int>()
    private var actionListener: ActionListener? = listener
    lateinit var blankTile: Cell
    private var flag = false

    interface ActionListener {
        fun tileAdded(position: Cell)
        fun tileRemoved(position: Cell)
        fun tileMoved(start: Cell, end: Cell)
    }

    fun actionListener(actionListener: ActionListener?) {
        this.actionListener = actionListener
    }

    fun field() {
        for (x in 0..3) {
            for (y in 0..3) {
                if (x == 3 && y == 3) {
                    blankTile = tilesField[x][y]
                    break
                }
                var numbersContainsC = false
                var value = 0
                while (!numbersContainsC) {
                    value = Random.nextInt(1, 16)
                    if (!numbers.contains(value)) {
                        numbers.add(value)
                        numbersContainsC = true
                    }
                }
                tilesField[x][y] = Cell(x, y, value)
                if (flag)
                    actionListener?.tileRemoved(tilesField[x][y])
                actionListener?.tileAdded(tilesField[x][y])
            }
        }
        if (!isSolvable()) {
            numbers.clear()
            flag = true
            field()
        }
        numbers.clear()
    }

    private fun isSolvable(): Boolean {
        var isSolvable = true
        var counter = 0
        for (x in 0..15) {
            for (y in x + 1..15) {
                if (tilesField[y % 4][y / 4].value != null)
                    if (tilesField[x % 4][x / 4].value!! > tilesField[y % 4][y / 4].value!!) {
                    counter++
                }
            }
        }
        if ((counter + blankTile.posY) % 2 == 0) isSolvable = false
        return isSolvable
    }

    private fun cellInStartField(posX: Int, posY: Int): Boolean = (posX in 0..3 && posY in 0..3)

    fun getCellFromField(posX: Int, posY: Int): Cell? {
        return if (cellInStartField(posX, posY)) {
            tilesField[posX][posY]
        } else null
    }

    fun move(x: Int, y: Int): Boolean {
        val res: Boolean
        if (!(blankTile.posX == x && blankTile.posY == y)) {
                if (blankTile.posX == x) {
                    if (blankTile.posY < y) {
                        for (i in blankTile.posY + 1..y) {
                            actionListener?.tileMoved(getCellFromField(x, i - 1)!!, getCellFromField(x, i)!!)
                            tilesField[x][i - 1].value = tilesField[x][i].value
                        }
                    } else {
                        for (i in blankTile.posY downTo y + 1) {
                            actionListener?.tileMoved(getCellFromField(x, i)!!, getCellFromField(x, i - 1)!!)
                            tilesField[x][i].value = tilesField[x][i - 1].value

                        }
                    }
                }
                if (blankTile.posY == y) {
                    if (blankTile.posX < x) {
                        for (i in blankTile.posX + 1..x) {
                            actionListener?.tileMoved(getCellFromField(i - 1, y)!!, getCellFromField(i, y)!!)
                            tilesField[i - 1][y].value = tilesField[i][y].value
                        }
                    } else {
                        for (i in blankTile.posX downTo x + 1) {
                            actionListener?.tileMoved(getCellFromField(i, y)!!, getCellFromField(i - 1, y)!!)
                            tilesField[i][y].value = tilesField[i - 1][y].value
                        }
                    }
                }
                tilesField[x][y].value = null
                blankTile = tilesField[x][y]
                res = true
            } else {
                res = false
            }
        return res
    }

    fun checkGameOver(): Boolean {
        var a = 1
        var res = true
        for (i in 0..15) {
            if (i % 4 == 3 && i / 4 == 3) {
                if (tilesField[i % 4][i / 4].value != null) {
                    res = false
                    break
                }
            } else {
                if (tilesField[i % 4][i / 4].value != a) {
                    res = false
                    break
                }
                a++
            }

        }
        return res
    }

}

data class Cell(var posX: Int, var posY: Int, var value: Int?) {

    override fun equals(other: Any?): Boolean {
        return if (other !is Cell?) false
        else {
            !(this.posX != other?.posX || this.posY != other?.posY || this.value != other?.value)
        }
    }

    override fun hashCode(): Int {
        var result = posX
        result = 31 * result + posY
        result = 31 * result + (value ?: 0)
        return result
    }


}