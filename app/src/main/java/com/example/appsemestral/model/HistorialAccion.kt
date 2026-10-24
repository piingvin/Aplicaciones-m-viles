package com.example.appsemestral.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historial_acciones")
data class HistorialAccion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val accion: String,
    val descripcion: String,
    val fecha: String
)
