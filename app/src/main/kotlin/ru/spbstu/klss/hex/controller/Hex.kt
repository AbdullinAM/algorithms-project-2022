package ru.spbstu.klss.hex.controller

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ru.spbstu.klss.hex.view.MainMenuScreen

class Hex : Game() {
    var batch: SpriteBatch? = null
    var font: BitmapFont? = null

    override fun create() {
        batch = SpriteBatch()
        font = BitmapFont() // use libGDX's default Arial font
        setScreen(MainMenuScreen(this))
    }


    override fun dispose() {
        batch!!.dispose()
        font!!.dispose()
    }
}