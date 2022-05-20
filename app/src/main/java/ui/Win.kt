package ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.app.R

class Win : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_win)
    }

    fun moveToStartMenu(view: View) {
        val winIntent = Intent(this, MainActivity::class.java)
        startActivity(winIntent)
    }


}