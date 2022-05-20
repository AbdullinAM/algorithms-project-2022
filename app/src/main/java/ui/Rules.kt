package ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.app.R

class Rules : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_rules)
    }

    fun moveToStart(view: View) {
        val rulesIntent = Intent(this, MainActivity::class.java)
        startActivity(rulesIntent)
    }
}