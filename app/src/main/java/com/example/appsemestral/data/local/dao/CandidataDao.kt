package com.example.appsemestral.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.appsemestral.model.Candidata
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidataDao {
    @Query("SELECT * FROM candidatas")
    fun listarCandidatas(): Flow<List<Candidata>>

    @Query("SELECT * FROM candidatas WHERE id = :id")
    suspend fun buscarPorId(id: Int): Candidata?

    @Insert
    suspend fun insertar(candidata: Candidata): Long

    @Update
    suspend fun actualizar(candidata: Candidata)

    @Delete
    suspend fun eliminar(candidata: Candidata)
}