package com.example.appsemestral.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appsemestral.model.HistorialAccion
import kotlinx.coroutines.flow.Flow

@Dao
interface HistorialDao {
    @Query("SELECT * FROM historial_acciones ORDER BY id DESC")
    fun listarHistorial(): Flow<List<HistorialAccion>>

    @Insert
    suspend fun insertar(historial: HistorialAccion): Long
}
