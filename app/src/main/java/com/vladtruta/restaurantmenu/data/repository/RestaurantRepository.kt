package com.vladtruta.restaurantmenu.data.repository

import androidx.lifecycle.LiveData
import com.vladtruta.restaurantmenu.data.model.local.*
import com.vladtruta.restaurantmenu.data.model.requests.KitchenRequest
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
                val response = restaurantNetwork.getAllCategories()
                if (response.success) {
                    restaurantNetwork.getAllCategories().data.toTypedArray()
                } else {
                    throw Exception()
                }
            } catch (error: Exception) {
                throw Exception("Unable to refresh categories", error)
            }
        }
        restaurantDao.insertCategories(*categories)
    }

    suspend fun refreshMenuCourses() {
        val menuCourses = withContext(Dispatchers.Default) {
            try {
                val response = restaurantNetwork.getAllMenuCourses()
                if (response.success) {
                    restaurantNetwork.getAllMenuCourses().data.toTypedArray()
                } else {
                    throw Exception()
                }
            } catch (error: Exception) {
                throw Exception("Unable to refresh menu courses", error)
            }
        }
        restaurantDao.insertMenuCourses(*menuCourses)
    }

    suspend fun sendKitchenRequest(cartItems: List<CartItem>, tableName: String) {
        try {
            val kitchenRequest = KitchenRequest(cartItems, tableName)
            restaurantNetwork.sendKitchenRequest(kitchenRequest)
        } catch (error: Exception) {
            throw Exception("Unable to send kitchen request", error)
        }
    }
    //endregion

    //region Database
    suspend fun clearDatabase() {
        restaurantDao.clearCart()
        restaurantDao.clearMenuCourses()
        restaurantDao.clearCategories()
        restaurantDao.clearOrderedItems()
        restaurantDao.clearCustomers()
    }

    fun getAllCategories(): LiveData<List<Category>> {
        return restaurantDao.getAllCategories()
    }

    suspend fun getMenuCoursesByCategory(category: Category): List<MenuCourse> {
        return restaurantDao.getMenuCoursesByCategoryId(category.id)
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

    suspend fun incrementQuantityInCart(id: Int, quantity: Int) {
        restaurantDao.incrementQuantityInCartById(id, quantity)
    }

    suspend fun updateQuantityInCart(id: Int, quantity: Int) {
        restaurantDao.updateQuantityInCartById(id, quantity)
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

    suspend fun getAllOrderedItemsSuspend(): List<OrderedItem> {
        return restaurantDao.getAllOrderedItemsSuspend()
    }

    fun getOrderedItemsTotalPrice(): LiveData<Int> {
        return restaurantDao.getOrderedItemsTotalPrice()
    }

    suspend fun insertOrderedItem(orderedItem: OrderedItem) {
        restaurantDao.insertOrderedItem(orderedItem)
    }

    suspend fun insertOrIncrementOrderedItems(cartItems: List<CartItem>) {
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

    suspend fun updateCustomerOfOrderedItem(id: Int, customer: Customer?) {
        val orderedItem = restaurantDao.getOrderedItemById(id)
            ?: throw Exception("Could not find order with id $id")
        restaurantDao.updateOrderedItem(orderedItem.apply { this.payingCustomer = customer })
    }

    suspend fun clearOrderedItems() {
        restaurantDao.clearOrderedItems()
    }

    suspend fun insertCustomer(customer: Customer) {
        restaurantDao.insertCustomer(customer)
    }

    fun getAllCustomers(): LiveData<List<Customer>> {
        return restaurantDao.getAllCustomers()
    }

    suspend fun clearCustomers() {
        return restaurantDao.clearCustomers()
    }

    ///endregion
}