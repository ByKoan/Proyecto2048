package com.dam2.proyecto2048.clases

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

class Preferences(var context: Context) {
    var prefs: SharedPreferences = context.getSharedPreferences("AppConfig", MODE_PRIVATE)

    fun clearPoints() {
        prefs.edit().putString("puntos", """[]""").commit()
    }

    fun savePoint(puntos: Puntos) {
        val puntosList = mutableListOf<Puntos>()
        puntosList.addAll(getPointList())
        puntosList.add(puntos)

        val jsonArray = JSONArray()
        for (p in puntosList) {
            val jsonObject = JSONObject()
            jsonObject.put("puntos", p.puntos)
            jsonObject.put("fecha", p.fecha)
            jsonObject.put("dificultad", p.dificultad)
            jsonArray.put(jsonObject)
        }

        prefs.edit().putString("puntos", jsonArray.toString()).commit()
    }

    fun saveOrUpdateCurrentScore(puntos: Puntos) {
        val puntosList = mutableListOf<Puntos>()
        puntosList.addAll(getPointList())

        // Buscamos si ya existe puntuación de esta partida (dificultad actual y fecha de hoy)
        val today = puntos.fecha
        val index = puntosList.indexOfFirst { it.fecha == today && it.dificultad == puntos.dificultad }

        Log.i("SAVE", "PUNTOS ${puntos.fecha}, ${puntos.dificultad}")

        if (index != -1) {
            puntosList[index].puntos = puntos.puntos // actualizamos
        } else {
            puntosList.add(puntos) // si no existe, creamos
        }

        val jsonArray = JSONArray()
        for (p in puntosList) {
            val jsonObject = JSONObject()
            jsonObject.put("puntos", p.puntos)
            jsonObject.put("fecha", p.fecha)
            jsonObject.put("dificultad", p.dificultad)
            jsonArray.put(jsonObject)
        }

        prefs.edit().putString("puntos", jsonArray.toString()).commit()
    }

    fun getPointList(): List<Puntos> {
        val puntos = mutableListOf<Puntos>()
        val jsonArray = JSONArray(prefs.getString("puntos", """[]""")!!)

        for (i in 0 until jsonArray.length()) {
            val jobject = jsonArray.getJSONObject(i)
            val punto = Puntos(
                jobject.getString("puntos").toInt(),
                LocalDate.parse(jobject.getString("fecha")),
                jobject.getString("dificultad").toInt()
            )
            puntos.add(punto)
        }
        return puntos.toList()
    }
}
