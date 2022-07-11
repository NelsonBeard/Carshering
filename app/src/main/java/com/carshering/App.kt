package com.carshering

import android.app.Application
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(mainActivity, presenter, DAOImpls, store))
        }
    }
}