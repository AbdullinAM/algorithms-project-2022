package ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.app.R
import model.Cell
import model.Game15
import model.Solver
import java.lang.String
import kotlin.Any
import kotlin.toString


class StartField : AppCompatActivity(), Game15.ActionListener {

    private var startField = Game15(this)
    private lateinit var table: LinearLayout
    private lateinit var views: MutableList<View?>
    private lateinit var solver: Solver


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_start_field)
        table = findViewById(R.id.table)
        startField.actionListener(this)
        startField.field()
        views = mutableListOf()
    }

    private fun moveToEnd() {
        val endIntent = Intent(this, Win::class.java)
        startActivity(endIntent)
    }

    fun solvingButton(view: View) {
        solver = Solver(startField)
        Thread {
            solver.solver()
            checkForWin()
        }.start()
    }

    private fun findTag(viewGroup: ViewGroup, tag: Any): View? {
        for (i in 0 until viewGroup.childCount)
            if (viewGroup.getChildAt(i).tag == tag) return viewGroup.getChildAt(i)
        return null
    }

    private fun getTileLayout(cell: Cell): LinearLayout {
        val column = findTag(table, String.valueOf(cell.posY))
        return findTag(column as ViewGroup, String.valueOf(cell.posX)) as LinearLayout
    }

    @SuppressLint("ResourceAsColor")
    override fun tileAdded(position: Cell) {
        Thread.sleep(5)
        val view = getTileLayout(position)
        val textView = TextView(this)
        textView.text = position.value.toString()
        textView.textSize = 35F
        textView.width = 400
        textView.height = 400
        textView.setTextColor(resources.getColor(R.color.white))
        textView.gravity = Gravity.CENTER
        textView.setTypeface(null, Typeface.BOLD)
        view.addView(textView)
        Thread.sleep(5)
    }

    override fun tileRemoved(position: Cell) {
        val numberOfTile = getTileLayout(position).getChildAt(0)
        getTileLayout(position).removeView(numberOfTile)
    }

    override fun tileMoved(start: Cell, end: Cell) {
        runOnUiThread {
            val numberOfTile = getTileLayout(end).getChildAt(0)
            getTileLayout(end).removeView(numberOfTile)
            getTileLayout(start).addView(numberOfTile)
        }

    }

    fun onClick(view: View) {
        val posX = view.tag.toString().toInt()
        val posY = (view.parent as ViewGroup).tag.toString().toInt()
        startField.move(posX, posY)
        checkForWin()
    }

    private fun checkForWin() {
        if (startField.checkGameOver()) {
            Thread.sleep(5000)
            moveToEnd()
        }
    }

}