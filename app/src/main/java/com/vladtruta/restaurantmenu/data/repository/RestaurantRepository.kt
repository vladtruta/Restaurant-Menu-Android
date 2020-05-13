package com.vladtruta.restaurantmenu.data.repository

import androidx.lifecycle.LiveData
import com.vladtruta.restaurantmenu.data.model.local.CartItem
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
    suspend fun refreshCategories() {
        return try {
            val categories = restaurantNetwork.getAllCategories()
            restaurantDao.insertCategories(*categories.toTypedArray())
        } catch (error: Exception) {
            throw Exception("Unable to refresh categories", error)
        }
    }

    suspend fun refreshMenuCourses() {
        return try {
            val menuCourses = restaurantNetwork.getAllMenuCourses()
            restaurantDao.insertMenuCourses(*menuCourses.toTypedArray())
        } catch (error: Exception) {
            throw Exception("Unable to refresh menu courses", error)
        }
    }
    //endregion

    //region Database
    suspend fun clearDatabase() {
        restaurantDao.clearCart()
        restaurantDao.clearMenuCourses()
        restaurantDao.clearCategories()
    }

    fun getAllCategories(): LiveData<List<Category>> {
        return restaurantDao.getAllCategories()
    }

    suspend fun getMenuCoursesByCategory(category: String): List<MenuCourse> {
        return restaurantDao.getMenuCoursesByCategory(category)
    }

    suspend fun addItemToCart(menuCourse: MenuCourse, quantity: Int): Long {
        val item = CartItem(menuCourse, quantity)
        return restaurantDao.addItemToCart(item)
    }

    suspend fun updateQuantityInCart(id: Int, quantity: Int) {
        val cartItem = getCartItemById(id)
        restaurantDao.updateCartItem(cartItem.apply { this.quantity += quantity })
    }

    suspend fun getCartItemById(id: Int): CartItem {
        return restaurantDao.getCartItemById(id)
            ?: throw Exception("Could not find item with id $id")
    }

    suspend fun getCartItemIdByMenuCourseId(id: Int): Int {
        return restaurantDao.getCartItemIdByMenuCourseId(id)
            ?: throw Exception("Could not find cart item id with menuCourseId $id")
    }

    suspend fun deleteItemFromCart(id: Int) {
        restaurantDao.deleteItemFromCartById(id)
    }

    suspend fun clearCartItems() {
        restaurantDao.clearCart()
    }
    ///endregion
}