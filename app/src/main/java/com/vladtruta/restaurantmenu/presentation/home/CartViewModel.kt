package com.vladtruta.restaurantmenu.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.data.model.local.CartItem
import com.vladtruta.restaurantmenu.data.model.local.Customer
import com.vladtruta.restaurantmenu.data.model.local.OrderedItem
import com.vladtruta.restaurantmenu.data.model.local.PaymentSummary
import com.vladtruta.restaurantmenu.data.repository.RestaurantRepository
import com.vladtruta.restaurantmenu.utils.RestaurantApp
import com.vladtruta.restaurantmenu.utils.SessionUtils
import com.vladtruta.restaurantmenu.utils.UIUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private val _paymentSummary = MutableLiveData<ArrayList<PaymentSummary>>()
    val paymentSummary: LiveData<ArrayList<PaymentSummary>> = _paymentSummary

    private val _payButtonEnabled = MutableLiveData<Boolean>()
    val payButtonEnabled: LiveData<Boolean> = _payButtonEnabled

    private val _orderValid = MutableLiveData<Boolean>()
    val orderValid: LiveData<Boolean> = _orderValid

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
            RestaurantRepository.insertOrIncrementOrderedItems(cartItems.value!!)
            RestaurantRepository.clearCart()
            if (_splitPayEnabled.value == true) {
                updateOrderedItemsLayout()
            }
        }
    }

    fun payForOrder() {
        viewModelScope.launch {
            RestaurantApp.instance.startActivity(SessionUtils.getResetIntent())
        }
    }

    fun setSplitPayEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _splitPayEnabled.value = enabled
            updateOrderedItemsLayout()
        }
    }

    private suspend fun updateOrderedItemsLayout() {
        withContext(Dispatchers.Default) {
            val orderedItems = RestaurantRepository.getAllOrderedItemsSuspend()
            RestaurantRepository.clearOrderedItems()

            if (_splitPayEnabled.value == true) {
                orderedItems.forEach { orderedItem ->
                    repeat(orderedItem.cartItem.quantity) {
                        val singleItem =
                            OrderedItem(
                                orderedItem.cartItem.apply { this.quantity = 1 },
                                orderedItem.payingCustomer
                            )
                        RestaurantRepository.insertOrderedItem(singleItem)
                    }
                }
            } else {
                val cartItems = orderedItems.map { it.cartItem }
                RestaurantRepository.insertOrIncrementOrderedItems(cartItems)
            }
        }
    }


    fun updateCustomerOfOrderedItem(id: Int, customer: Customer?) {
        viewModelScope.launch(messageExceptionHandler) {
            RestaurantRepository.updateCustomerOfOrderedItem(id, customer)
        }
    }

    fun checkOrderValid() {
        val isSplitPayEnabled = splitPayEnabled.value ?: false
        if (!isSplitPayEnabled) {
            _orderValid.value = true
            return
        }
        _orderValid.value = false
        _payButtonEnabled.value = false
        viewModelScope.launch(Dispatchers.Default + messageExceptionHandler) {
            val customers = customers.value ?: emptyList()
            val orderedItems = orderedItems.value ?: emptyList()
            if (customers.isEmpty()) {
                _payButtonEnabled.postValue(true)
                throw Exception(UIUtils.getString(R.string.error_customers_empty))
            }
            if (orderedItems.any { it.payingCustomer == null }) {
                _payButtonEnabled.postValue(true)
                throw Exception(UIUtils.getString(R.string.error_some_products_not_assign))
            }
            _payButtonEnabled.postValue(true)
            _orderValid.postValue(true)
        }
    }

    fun updatePaymentSummary() {
        _payButtonEnabled.value = false
        viewModelScope.launch(Dispatchers.Default + messageExceptionHandler) {
            val paymentSummaryList = ArrayList<PaymentSummary>()
            val orderedItems = orderedItems.value ?: emptyList()
            val customers = customers.value ?: emptyList()
            val isSplitPayEnabled = splitPayEnabled.value ?: false
            if (isSplitPayEnabled) {
                customers.forEach { customer ->
                    val orderedItemsOfCustomer = orderedItems
                        .filter { it.payingCustomer == customer }
                        .map { it.cartItem }
                        .groupBy { it.menuCourse.id }
                        .values
                        .map {
                            it.reduce { acc, cartItem ->
                                CartItem(
                                    cartItem.menuCourse,
                                    acc.quantity + cartItem.quantity,
                                    cartItem.id
                                )
                            }
                        }
                    val totalPrice =
                        orderedItemsOfCustomer.sumBy { it.quantity * it.menuCourse.price }
                    val summary = PaymentSummary(orderedItemsOfCustomer, customer, totalPrice)
                    paymentSummaryList.add(summary)
                }
            } else {
                val orderedCartItems = orderedItems.map { it.cartItem }
                val orderedItemsTotalPrice = orderedItemsTotalPrice.value ?: 0
                val summary = PaymentSummary(orderedCartItems, null, orderedItemsTotalPrice)
                paymentSummaryList.add(summary)
            }
            _payButtonEnabled.postValue(true)
            _paymentSummary.postValue(paymentSummaryList)
        }
    }
}