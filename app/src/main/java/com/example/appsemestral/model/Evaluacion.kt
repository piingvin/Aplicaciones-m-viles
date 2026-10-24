package com.example.appsemestral.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "evaluaciones")
data class Evaluacion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val candidataId: Int,
    val candidataNombre: String,
    val puntajeExperiencia: Int, // 1 a 5
    val puntajeFormacion: Int,   // 1 a 5
    val puntajeEntrevista: Int,  // 1 a 5
    val totalPonderado: Double,
    val evaluadora: String = "Evaluadora WIM",
    val fecha: String
)
