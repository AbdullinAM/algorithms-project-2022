package ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.app.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
    }
    fun moveToRules(view: View) {
        val rulesIntent = Intent(this, Rules::class.java)
        startActivity(rulesIntent)
    }
    fun finishApp(view: View) {
        finishAffinity()
    }
    fun moveToStart(view: View) {
        val startIntent = Intent(this, StartField::class.java)
        startActivity(startIntent)
    }
}
