package com.dam2.proyecto2048

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dam2.proyecto2048.databinding.GameBinding

class Game : AppCompatActivity() {
    lateinit var gameBinding: GameBinding;

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

    }

    private fun buttonBehavior() {
        val salirButton = gameBinding.salirButton

        salirButton.setOnClickListener {
            finish()
        }
    }
}