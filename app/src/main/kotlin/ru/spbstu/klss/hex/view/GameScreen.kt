package ru.spbstu.klss.hex.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils.cos
import com.badlogic.gdx.math.MathUtils.sin
import com.badlogic.gdx.utils.viewport.FitViewport
import ru.spbstu.klss.hex.controller.Hex
import ru.spbstu.klss.hex.model.Color.*
import ru.spbstu.klss.hex.model.Model
import ru.spbstu.klss.hex.solver.Solver
import java.lang.Math.PI
import kotlin.math.sqrt
import ru.spbstu.klss.hex.model.Color as ModelColor


class GameScreen(
    val game: Hex,
    private val solverFirst: Solver? = null,
    private val solverSecond: Solver? = null,
    val human: Boolean = false
) : Screen {

    private val debug: Boolean = false
    private lateinit var viewport: FitViewport
    private lateinit var camera: OrthographicCamera
    private lateinit var linedShapeRenderer: ShapeRenderer
    private lateinit var filledShapeRenderer: ShapeRenderer
    private lateinit var batch: SpriteBatch

    private val model = Model()
    private var currentColor = Color.RED
    private var currentPlayer = 0//Random.nextInt(0, 1)
    private val turnQueue = ArrayList<String>()

    private val delayConst = 1.6f
    private var delay: Float = delayConst
    private var playerMakeMove = !human

    private var coordinatesToReDraw = Pair(-1, 1)
    private var cellToSelect = Pair(-1, -1)
    private val size = 30f
    private val centerX = 300f
    private val centerY = -100f
    private var gameOver = false

    private lateinit var mainFont: BitmapFont
    private lateinit var subFont: BitmapFont

    override fun show() {
        batch = SpriteBatch()
        linedShapeRenderer = ShapeRenderer()
        filledShapeRenderer = ShapeRenderer()
        camera = OrthographicCamera(1080f, 720f)
        camera.position.set(camera.viewportWidth * 0.5f, camera.viewportHeight * -0.5f, 0f)
        camera.update()
        batch.projectionMatrix = camera.combined
        viewport = FitViewport(camera.viewportWidth, camera.viewportHeight, camera)
        linedShapeRenderer.projectionMatrix = camera.combined
        filledShapeRenderer.projectionMatrix = camera.combined
        if (human) turnQueue.add("human")
        if (solverFirst != null) turnQueue.add("solverFirst")
        if (solverSecond != null) turnQueue.add("solverSecond")
        if (turnQueue.size == currentPlayer) currentPlayer--
        createFont()
    }

    override fun render(delta: Float) {
        gameOver = model.isWinner(fromViewToModelColor())
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)

        val textColor = if (currentColor == Color.RED) "Red"
        else "Blue"

        var textPlayer1 = ""
        var textPlayer2 = ""
        var string = ""
        if (turnQueue.size == 1) {
            if (textColor == "Red") textPlayer1 ="firstHuman"
            else textPlayer2 = "secondHuman"
        } else {
            if (turnQueue[currentPlayer] == "human") textPlayer1 = "human"
            else {
                if (solverFirst != null && turnQueue[currentPlayer] == "solverFirst") {
                    textPlayer1 = solverFirst.toString()
                    string = "Turn: " + textPlayer1 + System.getProperty("line.separator") + "" +
                            "turnColor: $textColor"
                }
                if (solverSecond != null && turnQueue[currentPlayer] == "solverSecond") {
                    textPlayer2 = solverSecond.toString()
                    string = "Turn: " + textPlayer2 + System.getProperty("line.separator") + "" +
                            "turnColor: $textColor"
                }
            }
        }


        batch.begin();
        mainFont.draw(batch, string, 600f, -550f);
        batch.end();

        linedShapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        filledShapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        var xtest = 36f
        val xchange = 26.5f
        if (debug) {
            for (i in 1..22) {
                linedShapeRenderer.line(xtest, 0f, xtest, -600f)
                xtest += xchange
            }
        }

        if (gameOver) {
            gameOver()
        } else {
            fieldRender()
        }

        if (playerMakeMove && delay > 0f)
            delay -= delta
        else {
            if (turnQueue[currentPlayer] == "solverFirst") {
                if (solverFirst != null) {
                    coordinatesToReDraw = solverFirst.action(model)
                }
                makeMove()
                delay = delayConst
                playerMakeMove = !human
            }
        }

        if (playerMakeMove && delay > 0f)
            delay -= delta
        else {
            if (turnQueue[currentPlayer] == "solverSecond") {
                if (solverSecond != null)
                    coordinatesToReDraw = solverSecond.action(model)
                makeMove()
                delay = delayConst
                playerMakeMove = !human
            }
        }

        linedShapeRenderer.end()
        filledShapeRenderer.end()
    }

    private fun makeMove() {
        val x = coordinatesToReDraw.first
        val y = coordinatesToReDraw.second

        //human clicks
        if (x == -1) return
        val currentCellColor = model.getCell(x, y).color
        if (currentCellColor != GRAY) return

        model.getCell(x, y).color = fromViewToModelColor()

        if (!model.isWinner(fromViewToModelColor()))
            changeTurn()
    }

    private fun changeTurn() {
        currentColor = if (currentColor == Color.RED) Color.BLUE
        else Color.RED
        currentPlayer++
        if (currentPlayer == turnQueue.size) currentPlayer = 0
    }

    private fun gameOver() {
        game.screen = GameOverScreen(game, currentColor)
    }


    private fun fromViewToModelColor(): ModelColor {
        if (this.currentColor == Color.RED) return RED
        return BLUE
    }

    private fun fromModelToViewColor(modelColor: ModelColor): Color {
        if (modelColor == RED) return Color.RED
        if (modelColor == BLUE) return Color.BLUE
        return Color.LIGHT_GRAY
    }

    private fun createFont() {
        mainFont = BitmapFont()
        mainFont.setColor(Color.BLACK)
        mainFont.setUseIntegerPositions(false)
        mainFont.getData().setScale(2.5f)
    }


    private fun fieldRender() {

        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                if (turnQueue[currentPlayer] == "human") {
                    val pair = fromPixelsToInts(screenX.toFloat(), screenY.toFloat())
                    this@GameScreen.coordinatesToReDraw = pair
                    this@GameScreen.makeMove()
                    if (turnQueue[currentPlayer] != "human")
                        this@GameScreen.playerMakeMove = true
                }
                return true
            }

            override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
                val pair = fromPixelsToInts(screenX.toFloat(), screenY.toFloat())
                this@GameScreen.cellToSelect = pair
//                println("dragged: $pair")
                return true
            }
        }

        for (y in 0..10) {
            for (x in 0..10) {
                linedShapeRenderer.color = Color.WHITE
                val vertices = fillHexPattern(
                    centerX + x * (1.75f * size) - y * (1.75f * size) / 2.0f,
                    centerY + -y * (1.75f * size) * sqrt(3f) / 2.0f,
                )
                val color = model.getCell(x, y).color
                fillHexWithColor(vertices, fromModelToViewColor(color))
                if (x == cellToSelect.first && y == cellToSelect.second) {
                    filledShapeRenderer.color = Color.YELLOW
                    for (i in 0..5) {
                        filledShapeRenderer.rectLine(
                            vertices[2 * i],
                            vertices[2 * i + 1],
                            vertices[(2 * i + 2) % 12],
                            vertices[(2 * i + 3) % 12],
                            3f
                        )
                    }
                }

                linedShapeRenderer.polygon(vertices)
                if (y == 0) {
                    filledShapeRenderer.color = Color.BLUE
                    filledShapeRenderer.rectLine(
                        vertices[0],
                        vertices[1] + 2f,
                        vertices[2],
                        vertices[3] + 2f,
                        2f
                    )
                    filledShapeRenderer.rectLine(
                        vertices[2],
                        vertices[3] + 2f,
                        vertices[4],
                        vertices[5] + 2f,
                        2f
                    )
                }
                if (y == 10) {
                    filledShapeRenderer.color = Color.BLUE
                    filledShapeRenderer.rectLine(
                        vertices[6],
                        vertices[7] - 2f,
                        vertices[8],
                        vertices[9] - 2f,
                        2f
                    )
                    filledShapeRenderer.rectLine(
                        vertices[8],
                        vertices[9] - 2f,
                        vertices[10],
                        vertices[11] - 2f,
                        2f
                    )
                }
                if (x == 0) {
                    filledShapeRenderer.color = Color.RED
                    filledShapeRenderer.rectLine(
                        vertices[2] - 2f,
                        vertices[3] + 1f,
                        vertices[4] - 2f,
                        vertices[5] + 1f,
                        2f
                    )
                    filledShapeRenderer.rectLine(
                        vertices[4] - 2f,
                        vertices[5] + 1f,
                        vertices[6] - 2f,
                        vertices[7] + 1f,
                        2f
                    )
                }
                if (x == 10) {
                    filledShapeRenderer.color = Color.RED
                    filledShapeRenderer.rectLine(
                        vertices[10] + 2f,
                        vertices[11] - 1f,
                        vertices[0] + 2f,
                        vertices[1] - 1f,
                        2f
                    )
                    filledShapeRenderer.rectLine(
                        vertices[8] + 2f,
                        vertices[9] - 1f,
                        vertices[10] + 2f,
                        vertices[11] - 1f,
                        2f
                    )
                }
            }
        }

    }

    private fun fillHexWithColor(vertices: FloatArray, color: Color) {
        filledShapeRenderer.color = color
        filledShapeRenderer.triangle(
            vertices[0],
            vertices[1],
            vertices[2],
            vertices[3],
            vertices[4],
            vertices[5]
        )
        filledShapeRenderer.box(
            vertices[6],
            vertices[7],
            0f,
            vertices[10] - vertices[6],
            vertices[5] - vertices[7],
            0f
        )
        filledShapeRenderer.triangle(
            vertices[6],
            vertices[7],
            vertices[8],
            vertices[9],
            vertices[10],
            vertices[11]
        )
    }

    private fun fillHexPattern(centerX: Float, centerY: Float): FloatArray {
        val result = FloatArray(12)
        for (i in 0..5) {
            val xyPair = hexCorner(centerX, centerY, size, i)
            result[2 * i] = xyPair.first
            result[2 * i + 1] = xyPair.second
        }
        return result
    }

    private fun hexCorner(centerX: Float, centerY: Float, size: Float, i: Int): Pair<Float, Float> {
        val angleDeg = 60f * i + 30f
        val angleRad = PI.toFloat() / 180f * angleDeg
        return Pair(centerX + size * cos(angleRad), centerY + size * sin(angleRad))
    }

    private fun fromPixelsToInts(screenX: Float, screenY: Float): Pair<Int, Int> {
        println("screenX: $screenX , ScreenY: $screenY")
        val sectorHeight = 3.0 / 2.0 * size + 1f
        val sectorWidth = sqrt(3f) * size + 1f
        val padX = centerX - 5.5 * sectorWidth
        val pixY = screenY + centerY + sectorHeight / 2.0
        var pixX = screenX - padX
        if (pixX < 0 || pixY < 0) return Pair(-1, 1)
        //ориентировочные координаты клетки в массиве
        var cellY = (pixY / sectorHeight.toInt()).toInt()
        pixX -= (sectorWidth / 2.0) * (10 - cellY)
        var cellX = (pixX / sectorWidth.toInt()).toInt()
        println("OrintX: $cellX , OrintY: $cellY")

        if (cellX < 0 || cellY < 0
            || cellX > sqrt(model.board.size.toDouble()) - 1
            || cellY > sqrt(model.board.size.toDouble()) - 1
        )
            return Pair(-1, 1)
        //deltas
        val deltaY = pixY % sectorHeight
        val deltaX = pixX % sectorWidth

        val tan = (size / 2.0) / (sqrt(3f) / 2.0 * size)

        val pixHeight: Double
        val pixWidth: Double

        if (deltaX > 3.0 / 4.0 * sectorWidth) {
            // правый угол
            pixWidth = deltaX - 3.0 / 4.0 * sectorWidth
            if (deltaY < sectorHeight * 3.0f / 4.0f) {
                //верних угол
                pixHeight = deltaY
                if (pixHeight / pixWidth < tan) {
                    cellY--
                    println("right-up : X: $cellX , Y: $cellY")
                }
            } else {
                //нижний угол
                pixHeight = (sectorHeight - deltaY)
                if (pixHeight / pixWidth < tan) {
                    cellY++
                    cellX++
                    println("right-down : X: $cellX , Y: $cellY")
                }
            }
        } else if (deltaX < sectorWidth / 4.0f) {
            //левый угол
            pixWidth = deltaX
            if (deltaY < sectorHeight * 3.0 / 4.0) {
                //верних угол
                pixHeight = deltaY
                if (pixHeight / pixWidth < tan) {
                    cellY--
                    cellX--
                    println("left-up : X: $cellX , Y: $cellY")
                }
            } else {
                //нижний угол
                pixHeight = (sectorHeight - deltaY)
                if (pixHeight / pixWidth < tan) {
                    cellY++
                    println("left-down : X: $cellX , Y: $cellY")
                }
            }
        }

        if (cellX < 0 || cellY < 0
            || cellX > sqrt(model.board.size.toDouble()) - 1
            || cellY > sqrt(model.board.size.toDouble()) - 1
        )
            return Pair(-1, 1)
        return Pair(cellX, cellY)
    }


    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        camera.update()
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {
        dispose()
    }

    override fun dispose() {
        batch.dispose()
        linedShapeRenderer.dispose()
        filledShapeRenderer.dispose()
    }
}