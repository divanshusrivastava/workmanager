package com.example.workmanager

import android.app.Application
import timber.log.Timber

class AppConfig : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}