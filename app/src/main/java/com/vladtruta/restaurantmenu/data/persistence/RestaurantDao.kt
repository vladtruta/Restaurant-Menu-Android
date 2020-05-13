package com.vladtruta.restaurantmenu.data.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vladtruta.restaurantmenu.data.model.local.CartItem
import com.vladtruta.restaurantmenu.data.model.local.Category
import com.vladtruta.restaurantmenu.data.model.local.MenuCourse

@Dao
interface RestaurantDao {
    @Insert
    suspend fun insertMenuCourses(vararg menuCourses: MenuCourse)

    @Query("SELECT * FROM menuCourses")
    fun getAllMenuCourses(): LiveData<List<MenuCourse>>

    @Query("SELECT * FROM menuCourses WHERE category = :category")
    suspend fun getMenuCoursesByCategory(category: String): List<MenuCourse>

    @Query("DELETE FROM menuCourses")
    suspend fun clearMenuCourses()

    @Insert
    suspend fun insertCategories(vararg categories: Category)

    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("DELETE FROM categories")
    suspend fun clearCategories()

    @Insert
    suspend fun addItemToCart(cartItem: CartItem): Long

    @Query("SELECT * FROM cartItems")
    fun getAllCartItems(): LiveData<List<CartItem>>

    @Query("SELECT * FROM cartItems WHERE id = :id")
    suspend fun getCartItemById(id: Int): CartItem?

    @Query("SELECT id FROM cartItems WHERE menuCourseid = :id")
    suspend fun getCartItemIdByMenuCourseId(id: Int): Int?

    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    @Query("DELETE FROM cartItems WHERE id = :id")
    suspend fun deleteItemFromCartById(id: Int)

    @Query("DELETE FROM cartItems")
    suspend fun clearCart()
}