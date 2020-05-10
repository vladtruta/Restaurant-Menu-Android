package com.vladtruta.restaurantmenu.data.webservice

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private lateinit var INSTANCE: RestaurantApi

fun getNetwork(): RestaurantApi {
    if (!::INSTANCE.isInitialized) {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(SkipNetworkInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        INSTANCE = retrofit.create(RestaurantApi::class.java)
    }

    return INSTANCE
}