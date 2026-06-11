package com.example.messfitai

import android.app.Application
import android.content.Context

class MessFitApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private var instance: MessFitApplication? = null

        fun getContext(): Context {
            return instance?.applicationContext 
                ?: throw IllegalStateException("Application not initialized yet")
        }
    }
}
