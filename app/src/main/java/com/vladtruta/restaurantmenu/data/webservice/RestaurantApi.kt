package com.vladtruta.restaurantmenu.data.webservice

import com.vladtruta.restaurantmenu.data.model.local.Category
import com.vladtruta.restaurantmenu.data.model.local.MenuCourse
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantApi {
    @GET("categories")
    suspend fun getAllCategories(): List<Category>

    @GET("courses")
    suspend fun getAllMenuCourses(): List<MenuCourse>
}