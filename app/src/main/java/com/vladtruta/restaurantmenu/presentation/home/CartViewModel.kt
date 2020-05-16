package com.vladtruta.restaurantmenu.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.vladtruta.restaurantmenu.data.repository.RestaurantRepository
import kotlinx.coroutines.CoroutineExceptionHandler
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

    var selectedCartItemId = RecyclerView.NO_POSITION

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
            RestaurantRepository.insertOrUpdateOrderedItems(cartItems.value!!)
            RestaurantRepository.clearCart()
        }
    }

    fun payForOrder() {
        viewModelScope.launch {
            RestaurantRepository.clearOrderedItems()
        }
    }
}