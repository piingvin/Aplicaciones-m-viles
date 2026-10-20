package com.example.appsemestral.di

import android.content.Context
import com.example.appsemestral.data.local.WimDatabase
import com.example.appsemestral.data.repository.CandidataRepository

class AppContainer(private val context: Context) {
    val database by lazy { WimDatabase.obtener(context) }
    val candidataRepository by lazy { CandidataRepository(database.candidataDao()) }
}