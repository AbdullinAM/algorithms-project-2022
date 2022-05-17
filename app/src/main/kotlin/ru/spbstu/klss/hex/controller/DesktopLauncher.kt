package ru.spbstu.klss.hex.controller

import kotlin.jvm.JvmStatic
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import ru.spbstu.klss.hex.view.Hex

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = Lwjgl3ApplicationConfiguration()
        config.setForegroundFPS(60)
        config.setTitle("HEX")
        Lwjgl3Application(Hex(), config)
    }
}