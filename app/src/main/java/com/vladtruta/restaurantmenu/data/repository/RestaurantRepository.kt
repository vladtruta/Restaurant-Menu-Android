package com.vladtruta.restaurantmenu.data.repository

import androidx.lifecycle.LiveData
import com.vladtruta.restaurantmenu.data.model.local.*
import com.vladtruta.restaurantmenu.data.persistence.getDatabase
import com.vladtruta.restaurantmenu.data.webservice.getNetwork
import com.vladtruta.restaurantmenu.utils.RestaurantApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RestaurantRepository {

    private val restaurantNetwork = getNetwork()
    private val restaurantDatabase = getDatabase(RestaurantApp.instance)
    private val restaurantDao = restaurantDatabase.restaurantDao

    //region API
    suspend fun refreshCategories() {
        val categories = withContext(Dispatchers.Default) {
            try {
                restaurantNetwork.getAllCategories().toTypedArray()
            } catch (error: Exception) {
                throw Exception("Unable to refresh categories", error)
            }
        }
        restaurantDao.insertCategories(*categories)
    }

    suspend fun refreshMenuCourses() {
        val menuCourses = withContext(Dispatchers.Default) {
            try {
                restaurantNetwork.getAllMenuCourses().toTypedArray()
            } catch (error: Exception) {
                throw Exception("Unable to refresh menu courses", error)
            }
        }
        restaurantDao.insertMenuCourses(*menuCourses)
    }
    //endregion

    //region Database
    suspend fun clearDatabase() {
        restaurantDao.clearCart()
        restaurantDao.clearMenuCourses()
        restaurantDao.clearCategories()
        restaurantDao.clearCart()
        restaurantDao.clearOrderedItems()
        restaurantDao.clearCustomers()
    }

    fun getAllCategories(): LiveData<List<Category>> {
        return restaurantDao.getAllCategories()
    }

    suspend fun getMenuCoursesByCategory(category: String): List<MenuCourse> {
        return restaurantDao.getMenuCoursesByCategory(category)
    }

    fun getAllCartItems(): LiveData<List<CartItem>> {
        return restaurantDao.getAllCartItems()
    }

    fun getCartItemsTotalPrice(): LiveData<Int> {
        return restaurantDao.getCartItemsTotalPrice()
    }

    suspend fun addItemToCart(menuCourse: MenuCourse, quantity: Int): Long {
        val item = CartItem(menuCourse, quantity)
        return restaurantDao.addItemToCart(item)
    }

    suspend fun addQuantityToAlreadyExistingInCart(id: Int, quantity: Int) {
        val cartItem = getCartItemById(id)
        restaurantDao.updateCartItem(cartItem.apply { this.quantity += quantity })
    }

    suspend fun updateQuantityInCart(id: Int, quantity: Int) {
        val cartItem = getCartItemById(id)
        restaurantDao.updateCartItem(cartItem.apply { this.quantity = quantity })
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

    suspend fun clearCart() {
        restaurantDao.clearCart()
    }

    fun getAllOrderedItems(): LiveData<List<OrderedItem>> {
        return restaurantDao.getAllOrderedItems()
    }

    fun getOrderedItemsTotalPrice(): LiveData<Int> {
        return restaurantDao.getOrderedItemsTotalPrice()
    }

    suspend fun insertOrUpdateOrderedItems(cartItems: List<CartItem>) {
        withContext(Dispatchers.Default) {
            cartItems.forEach { cartItem ->
                val orderedItem = restaurantDao.getOrderedItemByMenuCourseId(cartItem.menuCourse.id)
                orderedItem?.let {
                    restaurantDao.updateOrderedItem(it.apply { this.cartItem.quantity += cartItem.quantity })
                } ?: run {
                    restaurantDao.insertOrderedItem(OrderedItem(cartItem))
                }
            }
        }
    }

    suspend fun clearOrderedItems() {
        restaurantDao.clearOrderedItems()
    }

    suspend fun insertCustomers(vararg customers: Customer) {
        restaurantDao.insertCustomer(*customers)
    }

    fun getAllCustomers(): LiveData<List<Customer>> {
        return restaurantDao.getAllCustomers()
    }

    ///endregion
}