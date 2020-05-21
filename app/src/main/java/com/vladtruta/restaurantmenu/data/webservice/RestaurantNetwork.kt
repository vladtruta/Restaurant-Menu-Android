package com.vladtruta.restaurantmenu.data.webservice

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private lateinit var instance: RestaurantApi

fun getNetwork(): RestaurantApi {
    if (!::instance.isInitialized) {
        val okHttpClient = OkHttpClient.Builder()
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        instance = retrofit.create(RestaurantApi::class.java)
    }

    return instance
}