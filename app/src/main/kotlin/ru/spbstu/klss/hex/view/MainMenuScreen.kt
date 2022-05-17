package ru.spbstu.klss.hex.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.utils.ScreenUtils


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
		table!!.background = skin!!.getDrawable("table")

		ImagButtomStyle = ImageButtonStyle()
		ImagButtomStyle!!.up = skin!!.getDrawable("up")
		ImagButtomStyle!!.down = skin!!.getDrawable("down")
		ImagButtomStyle!!.checked = skin!!.getDrawable("check")
		button = ImageButton(ImagButtomStyle)

		val nimb = ImageButtonStyle()
		nimb.up = skin!!.getDrawable("table")
		nimb.down = skin!!.getDrawable("down")
		nimb.checked = skin!!.getDrawable("check")


		table!!.add(button).padLeft(1080f)//.padBottom(900f)
		table!!.row()
		table!!.add(ImageButton(nimb)).padLeft(1080f).padBottom(900f)
		stage!!.addActor(table)
	}

	override fun render(delta: Float) {
		ScreenUtils.clear(0f, 0f, 0.2f, 1f)
		stage!!.draw()
		camera.update()
		game.batch!!.projectionMatrix = camera.combined
//		game.batch!!.begin()
//		game.font!!.draw(game.batch, "Welcome to Drop!!! ", 100f, 150f)
//		game.font!!.draw(game.batch, "Tap anywhere to begin!", 100f, 100f)
//		game.batch!!.end()
		if (Gdx.input.isTouched) {
			dispose()
		}
	}

	override fun resize(width: Int, height: Int) {}


	override fun hide() {}

	override fun pause() {}

	override fun resume() {}

	override fun dispose() {}


}