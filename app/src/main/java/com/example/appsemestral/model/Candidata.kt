package com.example.appsemestral.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "candidatas")
data class Candidata(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val telefono: String,
    val area: String,
    val anosExperiencia: Int,
    val resumen: String,
    val enlacePerfil: String,
    val uriCv: String,
    val estado: String = "Postulada",
    val registradaPor: String
)