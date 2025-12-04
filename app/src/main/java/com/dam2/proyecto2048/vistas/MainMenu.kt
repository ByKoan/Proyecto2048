package com.dam2.proyecto2048.vistas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dam2.proyecto2048.clases.Preferences
import com.dam2.proyecto2048.clases.Puntos
import com.dam2.proyecto2048.databinding.MainMenuBinding
import java.time.LocalDate

class MainMenu : AppCompatActivity() {

    lateinit var mainMenuBinding : MainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainMenuBinding = MainMenuBinding.inflate(layoutInflater)


        enableEdgeToEdge()
        setContentView(mainMenuBinding.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainMenuBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonBehavior()
    }

    fun buttonBehavior(){
        val jugarButton = mainMenuBinding.jugarButton
        val verPuntosButton = mainMenuBinding.verPuntosButton
        val salirButton = mainMenuBinding.salirButton

        jugarButton.setOnClickListener {
            val intent = Intent(this, Game::class.java)
            startActivity(intent)
        }

        verPuntosButton.setOnClickListener {
            val intent = Intent(this, Points::class.java)
            startActivity(intent)
        }

        salirButton.setOnClickListener {
            finish()
        }
    }

}