package com.vladtruta.restaurantmenu.utils

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig

class RestaurantApp: Application(), CameraXConfig.Provider {
    companion object {
        lateinit var instance: RestaurantApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }
}