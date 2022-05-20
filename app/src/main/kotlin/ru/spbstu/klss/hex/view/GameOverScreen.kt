package ru.spbstu.klss.hex.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.ScreenUtils

class GameOverScreen(val game: Hex, private val currentColor: Color) : Screen {

    private lateinit var table: Table
    private lateinit var stage: Stage

    override fun show() {

        stage = Stage()
        table = Table()
        Gdx.input.inputProcessor = stage

        val gameOverAtlas: TextureAtlas = TextureAtlas(Gdx.files.internal("assets/packs/gameAtlas/gameOver.atlas"))
        val skin: Skin = Skin()
        skin.addRegions(gameOverAtlas)

        val color = if (currentColor == Color.RED) "red"
        else "blue"
        val winningImage = Image(skin.getDrawable("winImg_$color"))

        val backMenuButtonStyle: ImageButton.ImageButtonStyle = ImageButton.ImageButtonStyle()
        backMenuButtonStyle.up = skin.getDrawable("back_up")
        backMenuButtonStyle.down = skin.getDrawable("back_down")
        backMenuButtonStyle.checked = skin.getDrawable("back_up")

        val backToMenuButton = ImageButton(backMenuButtonStyle)
        backToMenuButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                game.screen = MainMenuScreen(game)
            }
        }
        )

        table.setFillParent(true)
        table.add(winningImage)
        table.row()
        table.add(backToMenuButton)
        stage.addActor(table)
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(1f, 1f, 1f, 1f)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {
       dispose()
    }

    override fun dispose() {
        stage.dispose()
    }
}