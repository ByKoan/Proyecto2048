package com.dam2.proyecto2048.vistas

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dam2.proyecto2048.R
import com.dam2.proyecto2048.clases.Tablero
import com.dam2.proyecto2048.databinding.GameBinding
import kotlin.math.abs

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

        Tablero.get().reset()
        Tablero.get().spawnNewCells()

        gestureBehavior()

        buttonBehavior()
        createBoard()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun gestureBehavior() {
        val gridLayout = gameBinding.tablero

        val tablero = Tablero.get()

        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {

            private val SWIPE_THRESHOLD = 100
            private val SWIPE_VELOCITY_THRESHOLD = 100

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {

                if (e1 == null || e2 == null) return false

                val diffX = e2.x - e1.x
                val diffY = e2.y - e1.y

                return when {
                    // ---- DESLIZAMIENTO HORIZONTAL ----
                    Math.abs(diffX) > Math.abs(diffY) -> {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                Log.i("GAME", "DESLIZADO DERECHA")
                                tablero.move(Tablero.Direction.RIGHT)
                                createBoard()
                            } else {
                                Log.i("GAME", "DESLIZADO IZQUIERDA")
                                tablero.move(Tablero.Direction.LEFT)
                                createBoard()
                            }
                            true
                        } else false
                    }

                    // ---- DESLIZAMIENTO VERTICAL ----
                    Math.abs(diffY) > Math.abs(diffX) -> {
                        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY > 0) {
                                Log.i("GAME", "DESLIZADO ABAJO")
                                tablero.move(Tablero.Direction.DOWN)
                                createBoard()
                            } else {
                                Log.i("GAME", "DESLIZADO ARRIBA")
                                tablero.move(Tablero.Direction.UP)
                                createBoard()
                            }
                            true
                        } else false
                    }

                    else -> false
                }
            }
        })

        gridLayout.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }


    private fun buttonBehavior() {
        val salirButton = gameBinding.salirButton

        salirButton.setOnClickListener {
            finish()
        }
    }

    private fun createBoard(){
        var tableroView = gameBinding.tablero;

        var tablero = Tablero.get()
        var lista2d = tablero.board

        tableroView.removeAllViews() // Limpiar si es necesario

        tableroView.columnCount = 4
        tableroView.rowCount = 4

        for (i in 0 until lista2d.size) {
            for (j in 0 until lista2d[i].size) {
                val textView = TextView(this).apply {
                    text = lista2d[i][j].toString()
                    gravity = Gravity.CENTER
                    height = 160
                    width = 160
                    setBackgroundColor(getColorForValue(lista2d[i][j]))
                    layoutParams = GridLayout.LayoutParams().apply {
                        columnSpec = GridLayout.spec(j, 1f)
                        rowSpec = GridLayout.spec(i, 1f)
                    }
                }

                if (textView.text == "0"){
                    textView.text == ""
                }


                tableroView.addView(textView)
            }
        }
    }

    private fun getColorForValue(value: Int): Int {
        return when (value) {
            0 -> Color.parseColor("#CDC1B4")   // vacío
            2 -> Color.parseColor("#EEE4DA")
            4 -> Color.parseColor("#EDE0C8")
            8 -> Color.parseColor("#F2B179")
            16 -> Color.parseColor("#F59563")
            32 -> Color.parseColor("#F67C5F")
            64 -> Color.parseColor("#F65E3B")
            128 -> Color.parseColor("#EDCF72")
            256 -> Color.parseColor("#EDCC61")
            512 -> Color.parseColor("#EDC850")
            1024 -> Color.parseColor("#EDC53F")
            2048 -> Color.parseColor("#EDC22E")
            else -> Color.parseColor("#3C3A32") // números >2048
        }
    }
}