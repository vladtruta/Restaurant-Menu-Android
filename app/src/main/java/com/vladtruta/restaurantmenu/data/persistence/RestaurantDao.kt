package com.vladtruta.restaurantmenu.data.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vladtruta.restaurantmenu.data.model.local.*

@Dao
interface RestaurantDao {
    @Insert
    suspend fun insertMenuCourses(vararg menuCourses: MenuCourse)

    @Query("SELECT * FROM menu")
    fun getAllMenuCourses(): LiveData<List<MenuCourse>>

    @Query("SELECT * FROM menu WHERE category_id = :id")
    suspend fun getMenuCoursesByCategoryId(id: Int): List<MenuCourse>

    @Query("DELETE FROM menu")
    suspend fun clearMenuCourses()

    @Insert
    suspend fun insertCategories(vararg categories: Category)

    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("DELETE FROM categories")
    suspend fun clearCategories()

    @Insert
    suspend fun addItemToCart(cartItem: CartItem): Long

    @Query("SELECT * FROM cart")
    fun getAllCartItems(): LiveData<List<CartItem>>

    @Query("SELECT SUM(quantity * menuCourse_price) FROM cart")
    fun getCartItemsTotalPrice(): LiveData<Int>

    @Query("SELECT * FROM cart WHERE id = :id")
    suspend fun getCartItemById(id: Int): CartItem?

    @Query("SELECT id FROM cart WHERE menuCourse_id = :id")
    suspend fun getCartItemIdByMenuCourseId(id: Int): Int?

    @Query("UPDATE cart SET quantity = quantity + :quantity WHERE id = :id")
    suspend fun incrementQuantityInCartById(id: Int, quantity: Int)

    @Query("UPDATE cart SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantityInCartById(id: Int, quantity: Int)

    @Query("DELETE FROM cart WHERE id = :id")
    suspend fun deleteItemFromCartById(id: Int)

    @Query("DELETE FROM cart")
    suspend fun clearCart()

    @Insert
    suspend fun insertOrderedItem(orderedItem: OrderedItem)

    @Query("SELECT * FROM orders")
    fun getAllOrderedItems(): LiveData<List<OrderedItem>>

    @Query("SELECT * FROM orders")
    suspend fun getAllOrderedItemsSuspend(): List<OrderedItem>

    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun getOrderedItemById(id: Int): OrderedItem?

    @Query("SELECT SUM(cartItem_quantity * cartItem_menuCourse_price) FROM orders")
    fun getOrderedItemsTotalPrice(): LiveData<Int>

    @Query("SELECT * FROM orders WHERE cartItem_menuCourse_id = :id")
    suspend fun getOrderedItemByMenuCourseId(id: Int): OrderedItem?

    @Update
    suspend fun updateOrderedItem(orderedItem: OrderedItem)

    @Query("DELETE FROM orders")
    suspend fun clearOrderedItems()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer)

    @Query("SELECT * FROM customers")
    fun getAllCustomers(): LiveData<List<Customer>>

    @Query("DELETE FROM customers")
    suspend fun clearCustomers()
}