package ru.spbstu.klss.hex.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.utils.ScreenUtils


class MainMenuScreen(val game: Hex) : Screen {
	var camera: OrthographicCamera = OrthographicCamera()

	var stage: Stage? = null
	var skin: Skin? = null
	var buttonAtlas: TextureAtlas? = null
	var table: Table? = null


	var button: TextButton? = null
	var textButtonStyle: TextButtonStyle? = null



	init {
		this.show()
	}

	override fun show() {
		camera.setToOrtho(false, 720f, 480f)
		stage = Stage()
		Gdx.input.inputProcessor = stage
		skin = Skin()
		buttonAtlas = TextureAtlas(Gdx.files.internal("buttons/buttons.pack"))
		skin!!.addRegions(buttonAtlas)

		table = Table()

		textButtonStyle = TextButtonStyle()
		textButtonStyle!!.font = game.font
		textButtonStyle!!.up = skin!!.getDrawable("up-button")
		textButtonStyle!!.down = skin!!.getDrawable("down-button")
		textButtonStyle!!.checked = skin!!.getDrawable("checked-button")
		button = TextButton("Start game", textButtonStyle)

		table!!.add(button)
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