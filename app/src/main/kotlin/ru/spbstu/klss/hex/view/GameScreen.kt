package ru.spbstu.klss.hex.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils.sin
import com.badlogic.gdx.math.MathUtils.cos
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.FitViewport
import ru.spbstu.klss.hex.model.Model
import java.lang.Math.PI
import kotlin.math.sqrt
import ru.spbstu.klss.hex.model.Color as ModelColor
import ru.spbstu.klss.hex.model.Color.RED as RED
import ru.spbstu.klss.hex.model.Color.BLUE as BLUE

class GameScreen(val game: Hex) : Screen {

    private lateinit var table: Table
    private lateinit var stage: Stage
    private lateinit var viewport: FitViewport
    private lateinit var camera: OrthographicCamera
    private lateinit var linedShapeRenderer: ShapeRenderer
    private lateinit var filledShapeRenderer: ShapeRenderer
    private lateinit var batch: SpriteBatch

    private val model = Model()
    private var currentColor = Color.RED
    private var coordinatesToReDraw = Pair(-1, 1)
    private val size = 30f
    private val centerX = 300f
    private val centerY = -100f
    private var gameOver = false

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
    }

    override fun render(delta: Float) {
        gameOver = model.isWinner(fromViewToModelColor())
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)

        linedShapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        filledShapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        if (gameOver) {
            gameOver()
        } else fieldRender()

        linedShapeRenderer.end()
        filledShapeRenderer.end()
    }

    private fun touch() {
        val x = coordinatesToReDraw.first
        val y = coordinatesToReDraw.second
        if (x == -1) return
        val oldCellColor = model.getCell(x, y).color
        val newCellColor = fromViewToModelColor()

        if (oldCellColor == newCellColor) return

        model.getCell(x, y).color = newCellColor
        if (!model.isWinner(fromViewToModelColor()))
            changeTurn()
    }

    private fun changeTurn() {
        currentColor = if (currentColor == Color.RED) Color.BLUE
        else Color.RED
    }

    private fun gameOver() {
        game.screen = GameOverScreen(game, currentColor)
    }

    private fun fromPixelsToInts(screenX: Float, screenY: Float): Pair<Int, Int> {
        println("screenX: $screenX , ScreenY: $screenY")
        val padX = centerX - 5.5 * sqrt(3f) * size
        val sectorHeight = 3.0 / 2.0 * size
        val sectorWidth = sqrt(3f) * size
        val pixY = screenY + centerY + sectorHeight / 2.0
        var pixX = screenX - padX
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
            if (deltaY < sectorHeight / 2.0) {
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
        } else if (deltaX < sectorWidth / 4.0) {
            //левый угол
            pixWidth = deltaX
            if (deltaY < sectorHeight / 2.0) {
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

    private fun fromViewToModelColor(): ModelColor {
        if (this.currentColor == Color.RED) return RED
        return BLUE
    }

    private fun fromModelToViewColor(modelColor: ModelColor): Color {
        if (modelColor == RED) return Color.RED
        if (modelColor == BLUE) return Color.BLUE
        return Color.LIGHT_GRAY
    }


    private fun fieldRender() {
        for (y in 0..10) {
            for (x in 0..10) {
                linedShapeRenderer.color = Color.WHITE
                val vertices = fillHexPattern(
                    centerX + x * (1.75f * size) - y * (1.75f * size) / 2,
                    centerY + -y * (1.75f * size) * sqrt(3f) / 2,
                )
                val color = model.getCell(x, y).color
                fillHexWithColor(vertices, fromModelToViewColor(color))
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

        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                val pair = fromPixelsToInts(screenX.toFloat(), screenY.toFloat())
                this@GameScreen.coordinatesToReDraw = Pair(pair.first, pair.second)
                this@GameScreen.touch()
                return true
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