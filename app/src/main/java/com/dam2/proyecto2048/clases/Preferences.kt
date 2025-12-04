package com.dam2.proyecto2048.clases

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

class Preferences(var context: Context) {
    var prefs : SharedPreferences = context.getSharedPreferences("AppConfig", MODE_PRIVATE)

    fun clearPoints(){
        prefs.edit().putString("puntos", """[]""").commit()
    }

    fun savePoint(puntos: Puntos){
        var puntosList = mutableListOf<Puntos>()
        puntosList.addAll(getPointList())

        puntosList.add(puntos)

        val jsonArray = JSONArray()

        for (puntos in puntosList){
            val jsonObject = JSONObject()

            jsonObject.put("puntos", puntos.puntos)
            jsonObject.put("fecha", puntos.fecha)
            jsonObject.put("dificultad", puntos.dificultad)

            jsonArray.put(jsonObject)
        }

        prefs.edit().putString("puntos", jsonArray.toString()).commit()
    }

    fun getPointList() : List<Puntos>{
        val puntos = mutableListOf<Puntos>()
        val jsonArray = JSONArray(prefs.getString("puntos", """[]""")!!)

        for (i in 0 until jsonArray.length()){
            val jobject = jsonArray.getJSONObject(i)
            val punto = Puntos(jobject.getString("puntos").toInt(), LocalDate.parse(jobject.getString("fecha")), jobject.getString("dificultad").toInt())
            puntos.add(punto)
        }

        return puntos.toList()
    }
}