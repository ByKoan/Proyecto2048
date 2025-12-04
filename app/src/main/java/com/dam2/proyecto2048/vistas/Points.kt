package com.dam2.proyecto2048.vistas

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dam2.proyecto2048.R
import com.dam2.proyecto2048.clases.Preferences
import com.dam2.proyecto2048.databinding.PointsBinding

class Points : AppCompatActivity() {
    lateinit var pointsBinding: PointsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pointsBinding = PointsBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(pointsBinding.main)
        ViewCompat.setOnApplyWindowInsetsListener(pointsBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var prefs = Preferences(this)

        Log.i("POINTS", "${prefs.getPointList()}")

        buttonBehavior()
        loadPointTable()
    }

    private fun buttonBehavior() {
        val salirButton = pointsBinding.salirButton
        val borrarPuntuaciones = pointsBinding.borrarPuntuaciones

        borrarPuntuaciones.setOnClickListener {
            val prefs = Preferences(this)
            prefs.clearPoints()
            loadPointTable()
        }

        salirButton.setOnClickListener {
            finish()
        }
    }

    private fun loadPointTable(){
        var tablaPuntos = pointsBinding.tablaPuntos
        tablaPuntos.removeAllViews()

        var prefs = Preferences(this)
        var puntosLista = prefs.getPointList()

        Log.i("POINTS", "${prefs.getPointList()}")

        var cabecera = TableRow(tablaPuntos.context)

        cabecera.addView(genTextView(tablaPuntos.context, "Puntos", 24f))
        cabecera.addView(genTextView(tablaPuntos.context, "Fecha", 24f))
        cabecera.addView(genTextView(tablaPuntos.context, "Dificultad", 24f))


        tablaPuntos.addView(cabecera)

        for (i in 0 until puntosLista.size){
            val row = TableRow(tablaPuntos.context)

            row.addView(genTextView(tablaPuntos.context, puntosLista[i].puntos.toString(), 14f))
            row.addView(genTextView(tablaPuntos.context, puntosLista[i].fecha.toString(), 14f))
            row.addView(genTextView(tablaPuntos.context, puntosLista[i].dificultad.toString(), 14f))

            tablaPuntos.addView(row)
        }
    }

    fun genTextView(context : Context, texto : String, tamaño : Float) : TextView{
        val params = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT, 1f
        )

        return TextView(context).apply {
            text = texto
            textSize = tamaño
            setBackgroundResource(R.drawable.cell_border)
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            layoutParams = params
        }
    }
}