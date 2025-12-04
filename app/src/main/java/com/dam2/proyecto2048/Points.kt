package com.dam2.proyecto2048

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        buttonBehavior()

    }

    private fun buttonBehavior() {
        val salirButton = pointsBinding.salirButton

        salirButton.setOnClickListener {
            finish()
        }
    }
}