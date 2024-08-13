package com.android.example.plantmamaapp_v3

import android.app.Application
import com.android.example.plantmamaapp_v3.data.AppContainer
import com.android.example.plantmamaapp_v3.data.DefaultAppContainer

class PlantMamaApplication: Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}