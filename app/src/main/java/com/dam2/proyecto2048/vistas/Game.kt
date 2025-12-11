package com.dam2.proyecto2048.vistas

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
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
import com.dam2.proyecto2048.clases.Preferences
import com.dam2.proyecto2048.clases.Puntos
import com.dam2.proyecto2048.clases.Tablero
import com.dam2.proyecto2048.databinding.GameBinding
import kotlin.math.abs
import java.time.LocalDate

class Game : AppCompatActivity() {

    lateinit var gameBinding: GameBinding
    private var currentPuntos: Puntos? = null

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

        val dificultad = intent.getIntExtra("dificultad", 0)
        Tablero.get().dificultad = dificultad

        Tablero.get().reset()
        Tablero.get().spawnNewCells()
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
                    abs(diffX) > abs(diffY) -> {
                        if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) tablero.move(Tablero.Direction.RIGHT)
                            else tablero.move(Tablero.Direction.LEFT)
                            createBoard()
                            true
                        } else false
                    }

                    abs(diffY) > abs(diffX) -> {
                        if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY > 0) tablero.move(Tablero.Direction.DOWN)
                            else tablero.move(Tablero.Direction.UP)
                            createBoard()
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
        gameBinding.salirButton.setOnClickListener {
            finish()
        }
    }

    private fun createBoard() {
        val tableroView = gameBinding.tablero
        val tablero = Tablero.get()

        gameBinding.puntos.text = tablero.score.toString()

        tableroView.removeAllViews()
        tableroView.columnCount = 4
        tableroView.rowCount = 4

        for (i in 0 until 4) {
            for (j in 0 until 4) {
                val value = tablero.board[i][j]
                val textView = TextView(this).apply {
                    text = if (value == 0) "" else value.toString()
                    gravity = Gravity.CENTER
                    height = 160
                    width = 160
                    setBackgroundColor(getColorForValue(value))
                    layoutParams = GridLayout.LayoutParams().apply {
                        columnSpec = GridLayout.spec(j, 1f)
                        rowSpec = GridLayout.spec(i, 1f)
                    }
                }
                tableroView.addView(textView)
            }
        }

        val prefs = Preferences(this)

        if (currentPuntos == null) {
            currentPuntos = Puntos(tablero.score, LocalDate.now(), tablero.dificultad)
            prefs.saveOrUpdateCurrentScore(currentPuntos!!)
        } else {
            currentPuntos!!.puntos = tablero.score
            prefs.saveOrUpdateCurrentScore(currentPuntos!!)
        }

        // Mensajes de victoria/derrota
        if (tablero.hasWon()) {
            showMessage("¡Has ganado!")
            currentPuntos!!.puntos = tablero.score
            prefs.saveOrUpdateCurrentScore(currentPuntos!!)
            tablero.reset()
            currentPuntos = null
            createBoard()
        } else if (tablero.isGameOver()) {
            showMessage("¡Has perdido!")
            currentPuntos!!.puntos = tablero.score
            prefs.saveOrUpdateCurrentScore(currentPuntos!!)
            tablero.reset()
            currentPuntos = null
            createBoard()
        }
    }


    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun getColorForValue(value: Int): Int {
        return when (value) {
            0 -> Color.parseColor("#CDC1B4")
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
            else -> Color.parseColor("#3C3A32")
        }
    }
}
