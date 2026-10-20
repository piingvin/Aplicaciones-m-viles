package com.example.appsemestral

import android.app.Application
import com.example.appsemestral.di.AppContainer

class WimApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}