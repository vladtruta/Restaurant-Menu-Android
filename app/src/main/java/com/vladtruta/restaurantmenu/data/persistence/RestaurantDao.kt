package com.vladtruta.restaurantmenu.data.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vladtruta.restaurantmenu.data.model.local.CartItem
import com.vladtruta.restaurantmenu.data.model.local.Category
import com.vladtruta.restaurantmenu.data.model.local.MenuCourse

@Dao
interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuCourses(vararg menuCourses: MenuCourse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(vararg categories: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItems(vararg cartItems: CartItem)

    @Query("SELECT * FROM menuCourses")
    fun getAllMenuCourses(): LiveData<List<MenuCourse>>

    @Query("SELECT * FROM menuCourses WHERE category = :category")
    suspend fun getMenuCoursesByCategory(category: String): List<MenuCourse>

    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM cartItems")
    fun getAllCartItems(): LiveData<List<CartItem>>

    @Query("DELETE FROM categories")
    suspend fun clearCategories()

    @Query("DELETE FROM menuCourses")
    suspend fun clearMenuCourses()

    @Query("DELETE FROM cartItems")
    suspend fun clearCartItems()
}