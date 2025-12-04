package com.dam2.proyecto2048.vistas

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dam2.proyecto2048.R
import com.dam2.proyecto2048.databinding.GameBinding

class Game : AppCompatActivity() {
    lateinit var gameBinding: GameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameBinding = GameBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(gameBinding.main)
        ViewCompat.setOnApplyWindowInsetsListener(gameBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonBehavior()
        createBoard()
    }

    private fun buttonBehavior() {
        val salirButton = gameBinding.salirButton

        salirButton.setOnClickListener {
            finish()
        }
    }

    private fun createBoard(){
        var tablero = gameBinding.tablero;

        var lista2d = MutableList(4){ MutableList(4){""} }

        for (i in 0 until 4) {
            for (j in 0 until 4) {
                lista2d[i][j] = "$i $j"
            }
        }

        Log.i("GAME", "${lista2d}")

        tablero.removeAllViews() // Limpiar si es necesario

        tablero.columnCount = 4
        tablero.rowCount = 4

        for (i in 0 until lista2d.size) {
            for (j in 0 until lista2d[i].size) {
                val textView = TextView(this).apply {
                    text = lista2d[i][j]
                    gravity = Gravity.CENTER
                    height = 160
                    width = 160
                    setBackgroundResource(R.drawable.cell_border)
                    layoutParams = GridLayout.LayoutParams().apply {
                        columnSpec = GridLayout.spec(j, 1f)
                        rowSpec = GridLayout.spec(i, 1f)
                    }
                }

                tablero.addView(textView)
            }
        }
    }
}