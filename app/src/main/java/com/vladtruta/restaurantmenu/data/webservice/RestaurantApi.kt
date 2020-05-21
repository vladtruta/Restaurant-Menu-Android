package com.vladtruta.restaurantmenu.data.webservice

import com.vladtruta.restaurantmenu.data.model.local.Category
import com.vladtruta.restaurantmenu.data.model.local.MenuCourse
import com.vladtruta.restaurantmenu.data.model.requests.KitchenRequest
import com.vladtruta.restaurantmenu.data.model.responses.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RestaurantApi {
    @GET("categories")
    suspend fun getAllCategories(): BaseResponse<List<Category>>

    @GET("courses")
    suspend fun getAllMenuCourses(): BaseResponse<List<MenuCourse>>

    @POST("kitchen")
    suspend fun sendKitchenRequest(@Body request: KitchenRequest): BaseResponse<Any>
}