package com.example.appsemestral.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appsemestral.data.local.dao.CandidataDao
import com.example.appsemestral.data.local.dao.EvaluacionDao
import com.example.appsemestral.data.local.dao.HistorialDao
import com.example.appsemestral.model.Candidata
import com.example.appsemestral.model.Evaluacion
import com.example.appsemestral.model.HistorialAccion

@Database(
    entities = [Candidata::class, Evaluacion::class, HistorialAccion::class],
    version = 2,
    exportSchema = false
)
abstract class WimDatabase : RoomDatabase() {
    abstract fun candidataDao(): CandidataDao
    abstract fun evaluacionDao(): EvaluacionDao
    abstract fun historialDao(): HistorialDao

    companion object {
        @Volatile
        private var INSTANCE: WimDatabase? = null

        fun obtener(context: Context): WimDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    WimDatabase::class.java,
                    "wim100.db"
                )
                .fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
            }
        }
    }
}
