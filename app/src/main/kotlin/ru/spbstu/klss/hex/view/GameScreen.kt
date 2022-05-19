package ru.spbstu.klss.hex.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport


class GameScreen : Screen {


    private lateinit var viewport: FitViewport
    private lateinit var camera: OrthographicCamera
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var batch: SpriteBatch

    override fun show() {
        batch = SpriteBatch()
        shapeRenderer = ShapeRenderer()
        camera = OrthographicCamera(720f, 480f)
        camera.position.set(300f /*center of field*/, 200f /*center of field*/, 0f)
        camera.update()
        batch.projectionMatrix = camera.combined
        viewport = FitViewport(camera.viewportWidth, camera.viewportHeight, camera)
        shapeRenderer.projectionMatrix = camera.combined

    }

    override fun render(delta: Float) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.color = Color.LIGHT_GRAY

        shapeRenderer.rectLine(0f,0f,10f,10f,5f, Color.BLUE, Color.RED)
        shapeRenderer.triangle(10f,50f,50f,60f,100f,50f, Color.LIGHT_GRAY,Color.LIGHT_GRAY,Color.LIGHT_GRAY)
        shapeRenderer.box(10f,20f,0f,90f,30f,0f)
        shapeRenderer.triangle(10f,20f,100f,20f,50f,10f, Color.LIGHT_GRAY,Color.LIGHT_GRAY,Color.LIGHT_GRAY)

        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.color = Color.BLACK

        val vertices = floatArrayOf(10f,20f,10f,50f,50f,60f,100f,50f,100f,20f,50f,10f)
        shapeRenderer.polygon(vertices)

        shapeRenderer.end();
    }

    override fun resize(width: Int, height: Int) {
        
    }

    override fun pause() {
        
    }

    override fun resume() {
        
    }

    override fun hide() {
        
    }

    override fun dispose() {
        
    }
}