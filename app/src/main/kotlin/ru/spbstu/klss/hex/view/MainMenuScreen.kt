package ru.spbstu.klss.hex.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.ScreenUtils
import javax.swing.event.ChangeEvent


class MainMenuScreen(val game: Hex) : Screen {
    var camera: OrthographicCamera = OrthographicCamera()

    var stage: Stage? = null
    var skin: Skin? = null
    var buttonAtlas: TextureAtlas? = null
    var table: Table? = null


    var button: ImageButton? = null
    var ImagButtomStyle: ImageButtonStyle? = null


    init {
        this.show()
    }

    override fun show() {
        camera.setToOrtho(false, 1080f, 720f)
        stage = Stage()
        Gdx.input.inputProcessor = stage
        skin = Skin()
        buttonAtlas = TextureAtlas(Gdx.files.internal("assets/packs/start_pack/start.atlas"))
        skin!!.addRegions(buttonAtlas)

        table = Table()
        table!!.setFillParent(true)
        table!!.align(Align.center)
        table!!.debug(Table.Debug.table)

        ImagButtomStyle = ImageButtonStyle()
        ImagButtomStyle!!.up = skin!!.getDrawable("up")
        ImagButtomStyle!!.down = skin!!.getDrawable("down")
        ImagButtomStyle!!.checked = skin!!.getDrawable("check")

        button = ImageButton(ImagButtomStyle)
        button!!.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                TODO("Not yet implemented")
            }
        }
        )

        table!!.add(button)//.padLeft(1080f)//.padBottom(900f)
//		table!!.row()
//		table!!.add(ImageButton(nimb))//.padLeft(1080f).padBottom(900f)
        stage!!.addActor(table)
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(0f, 0f, 0.2f, 1f)
        stage!!.draw()

    }

    override fun resize(width: Int, height: Int) {}


    override fun hide() {}

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {}


}