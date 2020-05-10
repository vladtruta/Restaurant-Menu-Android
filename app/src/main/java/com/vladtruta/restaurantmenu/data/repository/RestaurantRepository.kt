package com.vladtruta.restaurantmenu.data.repository

import androidx.lifecycle.LiveData
import com.vladtruta.restaurantmenu.data.model.local.Category
import com.vladtruta.restaurantmenu.data.model.local.MenuCourse
import com.vladtruta.restaurantmenu.data.webservice.getNetwork
import com.vladtruta.restaurantmenu.data.persistence.getDatabase
import com.vladtruta.restaurantmenu.utils.RestaurantApp
import java.lang.Exception

object RestaurantRepository {

    private val restaurantNetwork = getNetwork()
    private val restaurantDatabase = getDatabase(RestaurantApp.instance)
    private val restaurantDao = restaurantDatabase.restaurantDao

    //region API
    suspend fun refreshCategories(): Result<Unit> {
        return try {
            val categories = restaurantNetwork.getAllCategories()
            restaurantDao.insertCategories(*categories.toTypedArray())
            Result.success(Unit)
        } catch (error: Exception) {
            throw Exception("Unable to refresh categories", error)
        }
    }

    suspend fun refreshMenuCourses(): Result<Unit> {
        return try {
            val menuCourses = restaurantNetwork.getAllMenuCourses()
            restaurantDao.insertMenuCourses(*menuCourses.toTypedArray())
            Result.success(Unit)
        } catch (error: Exception) {
            throw Exception("Unable to refresh menu courses", error)
        }
    }
    //endregion

    //region Database
    suspend fun clearDatabase() {
        restaurantDao.clearCartItems()
        restaurantDao.clearMenuCourses()
        restaurantDao.clearCategories()
    }

    suspend fun insertMenuCourses(vararg menuCourses: MenuCourse) {
        restaurantDao.insertMenuCourses(*menuCourses)
    }

    suspend fun insertCategories(vararg categories: Category) {
        restaurantDao.insertCategories(*categories)
    }

    fun getAllMenuCourses(): LiveData<List<MenuCourse>> {
        return restaurantDao.getAllMenuCourses()
    }

    suspend fun getMenuCoursesByCategory(category: String): List<MenuCourse> {
        return restaurantDao.getMenuCoursesByCategory(category)
    }

    fun getAllCategories(): LiveData<List<Category>> {
        return restaurantDao.getAllCategories()
    }

    suspend fun clearCategories() {
        restaurantDao.clearCategories()
    }

    suspend fun clearMenuCourses() {
        restaurantDao.clearMenuCourses()
    }

    suspend fun clearCartItems() {
        restaurantDao.clearCartItems()
    }
    ///endregion
}