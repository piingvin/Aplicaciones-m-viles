package com.example.appsemestral.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appsemestral.model.Evaluacion
import kotlinx.coroutines.flow.Flow

@Dao
interface EvaluacionDao {
    @Query("SELECT * FROM evaluaciones ORDER BY id DESC")
    fun listarEvaluaciones(): Flow<List<Evaluacion>>

    @Query("SELECT * FROM evaluaciones ORDER BY totalPonderado DESC")
    fun obtenerRanking(): Flow<List<Evaluacion>>

    @Query("SELECT * FROM evaluaciones WHERE candidataId = :candidataId LIMIT 1")
    suspend fun buscarPorCandidataId(candidataId: Int): Evaluacion?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(evaluacion: Evaluacion): Long

    @Delete
    suspend fun eliminar(evaluacion: Evaluacion)
}
