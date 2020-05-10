package com.vladtruta.restaurantmenu.utils

import android.app.Application

class RestaurantApp: Application() {
    companion object {
        lateinit var instance: RestaurantApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}