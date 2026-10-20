package com.example.appsemestral.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appsemestral.model.Candidata
import com.example.appsemestral.data.local.dao.CandidataDao

@Database(entities = [Candidata::class], version = 1, exportSchema = false)
abstract class WimDatabase : RoomDatabase() {
    abstract fun candidataDao(): CandidataDao

    companion object {
        @Volatile
        private var INSTANCE: WimDatabase? = null

        fun obtener(context: Context): WimDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    WimDatabase::class.java,
                    "wim100.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}