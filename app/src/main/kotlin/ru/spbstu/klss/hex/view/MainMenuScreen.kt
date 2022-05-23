package ru.spbstu.klss.hex.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.ScreenUtils
import ru.spbstu.klss.hex.controller.Hex
import ru.spbstu.klss.hex.model.Color
import ru.spbstu.klss.hex.solver.AlexSolver
import ru.spbstu.klss.hex.solver.SemaSolver


class MainMenuScreen(val game: Hex) : Screen {

    private lateinit var twoSolverButton: ImageButton
    private lateinit var humanAndAlex: ImageButton
    private lateinit var humanAndSema: ImageButton
    private lateinit var twoPlayerButton: ImageButton
    private lateinit var startButton: ImageButton
    var camera: OrthographicCamera = OrthographicCamera()

    private val stage: Stage = Stage()
    private val skin: Skin = Skin()
    private val buttonAtlas: TextureAtlas =
        TextureAtlas(Gdx.files.internal("assets/packs/mainMenuAtlas/mainMenu.atlas"))
    private val table: Table = Table()

    private val startButtonStyle: ImageButtonStyle = ImageButtonStyle()
    private val twoSolverButtonStyle: ImageButtonStyle = ImageButtonStyle()
    private val humanAndAlexStyle: ImageButtonStyle = ImageButtonStyle()
    private val humanAndSemaStyle: ImageButtonStyle = ImageButtonStyle()
    private val twoPlayerButtonStyle: ImageButtonStyle = ImageButtonStyle()

    override fun show() {
        camera.setToOrtho(false, 1080f, 720f)
        Gdx.input.inputProcessor = stage

        skin.addRegions(buttonAtlas)

        table.setFillParent(true)
        //       table.debugAll()

        startButtonStyle.up = skin.getDrawable("up")
        startButtonStyle.down = skin.getDrawable("down")
        startButtonStyle.checked = skin.getDrawable("check")

        twoSolverButtonStyle.up = skin.getDrawable("up_twoBots")
        twoSolverButtonStyle.down = skin.getDrawable("down_twoBots")
        twoSolverButtonStyle.checked = skin.getDrawable("up_twoBots")

        humanAndAlexStyle.up = skin.getDrawable("up_humanAndBot_Alex")
        humanAndAlexStyle.down = skin.getDrawable("down_humanAndBot_Alex")
        humanAndAlexStyle.checked = skin.getDrawable("up_humanAndBot_Alex")

        humanAndSemaStyle.up = skin.getDrawable("up_humanAndBot_Sema")
        humanAndSemaStyle.down = skin.getDrawable("down_humanAndBot_Sema")
        humanAndSemaStyle.checked = skin.getDrawable("up_humanAndBot_Sema")

        twoPlayerButtonStyle.up = skin.getDrawable("up_twoPlayer")
        twoPlayerButtonStyle.down = skin.getDrawable("down_twoPlayer")
        twoPlayerButtonStyle.checked = skin.getDrawable("up_twoPlayer")

        startButton = ImageButton(startButtonStyle)
        startButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                twoPlayerButton.isVisible = !twoPlayerButton.isVisible
                twoSolverButton.isVisible = !twoSolverButton.isVisible
                humanAndAlex.isVisible = !humanAndAlex.isVisible
                humanAndSema.isVisible = !humanAndSema.isVisible
                changePadOf(startButton)
                table.invalidate()
            }
        }
        )

        twoPlayerButton = ImageButton(twoPlayerButtonStyle)
        twoPlayerButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                game.screen = GameScreen(game, human = true)
            }
        }
        )

        twoSolverButton = ImageButton(twoSolverButtonStyle)
        twoSolverButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                game.screen = GameScreen(game, AlexSolver(Color.RED),SemaSolver(Color.BLUE))
            }
        }
        )

        humanAndAlex = ImageButton(humanAndAlexStyle)
        humanAndAlex.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                game.screen = GameScreen(game, AlexSolver(Color.BLUE), human = true)
            }
        }
        )

        humanAndSema = ImageButton(humanAndSemaStyle)
        humanAndSema.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                game.screen = GameScreen(game, SemaSolver(Color.BLUE), human = true)
            }
        }
        )

        twoPlayerButton.isVisible = false
        twoSolverButton.isVisible = false
        humanAndAlex.isVisible = false
        humanAndSema.isVisible = false

        table.bottom()
        table.add(twoPlayerButton)
        table.row()
        table.add(twoSolverButton)
        table.row()
        table.add(humanAndAlex)
        table.row()
        table.add(humanAndSema)
        table.row()
        table.add(startButton).padBottom(stage.height * 0.7f)

        stage.addActor(table)
    }

    private fun changePadOf(button: Button) {
        var cell = table.getCell(button)
        if (twoPlayerButton.isVisible) {
            cell.padBottom(0f)
            cell.padTop(table.height * 0.7f - 4 * button.height)

        } else {
            cell.padBottom(table.height * 0.7f)
            cell.padTop(0f)
        }

    }

    override fun render(delta: Float) {
        ScreenUtils.clear(0f, 0f, 0.2f, 1f)
        stage.draw()

    }

    override fun resize(width: Int, height: Int) {}


    override fun hide() {
        dispose()
    }

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {
        stage.dispose()
        buttonAtlas.dispose()

    }
}