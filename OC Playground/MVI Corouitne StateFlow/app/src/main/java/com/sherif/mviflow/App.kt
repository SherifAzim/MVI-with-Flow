package com.sherif.mviflow

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}