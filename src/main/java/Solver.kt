import Game.tiles
import Game.move
import kotlin.math.abs

public class Solver {
    private val dx = arrayOf(0, -1, 0, 1)
    private val dy = arrayOf(1, 0, -1, 0)
    private var resultString = ""

    private var moveName = arrayOf('D', 'L', 'U', 'R')
    private var oppositeMove = arrayOf(2, 3, 0, 1)

    private val infinity = 10000
    private var x0 = 0
    private var y0 = 0
    private var goalX = IntArray(16)
    private var goalY = IntArray(16)
    private val board = Array(4) { IntArray(4) }

    init {
        for (i in 0..3) {
            for (j in 0..3) {
                board[i][j] = tiles[i][j]
            }
        }
    }

    private var step = 0
    private val boardGoal = Array(4) { IntArray(4) }
    private var minPrevIteration = 0
    private var deepness = 0

    private fun initGoalArray() {
        var m = 1
        for (i in 0..3) {
            for (j in 0..3) {
                boardGoal[i][j] = m
                m++
            }
        }
        boardGoal[3][3] = 0
    }

    private fun initGoalArrays() {
        for (i in 0..14) {
            goalX[i + 1] = i % 4
            goalY[i + 1] = i / 4
        }
        goalX[0] = 4
        goalY[0] = 4
    }

    private fun swap(y1: Int, x1: Int, y2: Int, x2: Int) {
        val value1 = board[y1][x1]
        val value2 = board[y2][x2]
        board[y1][x1] = value2
        board[y2][x2] = value1
    }

    private fun estimate(): Int {
        var manhattan = 0
        var value: Int
        var m: Int

        for (i in 0..3) {
            for (j in 0..3) {
                value = board[i][j]
                if ((value > 0) && (value != boardGoal[i][j])) {
                    m = abs(i - goalY[value]) + abs(j - goalX[value])
                    manhattan += m
                }
            }
        }
        return manhattan
    }


    private fun search(g: Int, prevMove: Int, x0: Int, y0: Int): Boolean {
        val h = estimate()
        if (h == 0) {
            return true
        }
        val f = g + h
        if (f > deepness) {
            if (minPrevIteration > f) {
                minPrevIteration = f
            }
            return false
        }
        var newx: Int
        var newy: Int
        var res: Boolean
        for (i in 0..3) {
            if (oppositeMove[i] != prevMove) {
                newx = x0 + dx[i]
                newy = y0 + dy[i]
                if ((newy <= 3) && (newy >= 0) && (newx <= 3) && (newx >= 0)) {
                    swap(y0, x0, newy, newx)
                    res = search(g + 1, i, newx, newy)
                    swap(y0, x0, newy, newx)
                    if (res) {
                        resultString += moveName[i]
                        step++
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun idaStar(): Boolean {
        var res = false
        deepness = estimate()
        while (deepness <= 150) {
            minPrevIteration = infinity
            for (i in 0..3) {
                for (j in 0..3) {
                    if (board[i][j] == 0) {
                        x0 = j
                        y0 = i
                    }
                }
            }
            step = 0
            res = search(0, -1, x0, y0)
            deepness = minPrevIteration
            if (res) break

        }
        resultString = resultString.reversed()
        when (resultString[0]) {
            'D' -> move(y0 + 1, x0)
            'U' -> move(y0 - 1, x0)
            'L' -> move(y0, x0 - 1)
            'R' -> move(y0, x0 + 1)
        }
        return res
    }

    fun helper(): String {
        initGoalArray()
        initGoalArrays()
        return if (idaStar()) resultString else "Sorry"
    }
}