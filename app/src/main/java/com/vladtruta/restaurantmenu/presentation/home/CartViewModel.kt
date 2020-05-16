package com.vladtruta.restaurantmenu.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.vladtruta.restaurantmenu.data.model.local.Customer
import com.vladtruta.restaurantmenu.data.model.local.OrderedItem
import com.vladtruta.restaurantmenu.data.repository.RestaurantRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val messageExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _errorMessage.postValue(throwable.message)
    }

    val cartItems = RestaurantRepository.getAllCartItems()
    val orderedItems = RestaurantRepository.getAllOrderedItems()
    val customers = RestaurantRepository.getAllCustomers()
    val cartItemsTotalPrice = RestaurantRepository.getCartItemsTotalPrice()
    val orderedItemsTotalPrice = RestaurantRepository.getOrderedItemsTotalPrice()

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _splitPayEnabled = MutableLiveData<Boolean>()
    val splitPayEnabled: LiveData<Boolean> = _splitPayEnabled

    var selectedCartItemId = RecyclerView.NO_POSITION

    var selectedCustomerId = 0

    fun updateQuantityInCart(id: Int, quantity: Int) {
        viewModelScope.launch(messageExceptionHandler) {
            RestaurantRepository.updateQuantityInCart(id, quantity)
        }
    }

    fun deleteItemFromCart(id: Int) {
        viewModelScope.launch {
            RestaurantRepository.deleteItemFromCart(id)
        }
    }

    fun insertOrderedItems() {
        viewModelScope.launch {
            RestaurantRepository.insertOrAddToOrderedItems(cartItems.value!!)
            RestaurantRepository.clearCart()
            if (_splitPayEnabled.value == true) {
                updateOrderedItemsLayout()
            }
        }
    }

    fun payForOrder() {
        viewModelScope.launch {
            RestaurantRepository.clearOrderedItems()
        }
    }

    fun setSplitPayEnabled(enabled: Boolean) {
        _splitPayEnabled.value = enabled
        updateOrderedItemsLayout()
    }

    private fun updateOrderedItemsLayout() {
        viewModelScope.launch(Dispatchers.Default) {
            val orderedItems = orderedItems.value ?: return@launch
            RestaurantRepository.clearOrderedItems()

            if (_splitPayEnabled.value == true) {
                orderedItems.forEach { orderedItem ->
                    repeat(orderedItem.cartItem.quantity) {
                        val singleItem = OrderedItem(orderedItem.cartItem.apply { this.quantity = 1 })
                        RestaurantRepository.insertOrderedItem(singleItem)
                    }
                }
            } else {
                val cartItems = orderedItems.map { it.cartItem }
                RestaurantRepository.insertOrAddToOrderedItems(cartItems)
            }
        }
    }

    fun updateCustomerOfOrderedItem(id: Int, customer: Customer?) {
        viewModelScope.launch(messageExceptionHandler) {
            RestaurantRepository.updateCustomerOfOrderedItem(id, customer)
        }
    }
}