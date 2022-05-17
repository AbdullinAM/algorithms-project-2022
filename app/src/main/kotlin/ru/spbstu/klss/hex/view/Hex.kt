package ru.spbstu.klss.hex.view

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils

class Hex : ApplicationAdapter() {
    var batch: SpriteBatch? = null
    var img: Texture? = null
    override fun create() {
        batch = SpriteBatch()
        img = Texture("assets/badlogic.jpg")
    }

    override fun render() {
        ScreenUtils.clear(1f, 0f, 0f, 1f)
        batch!!.begin()
        batch!!.draw(img, 50f, 50f)
        batch!!.end()
    }

    override fun dispose() {
        batch!!.dispose()
        img!!.dispose()
    }
}