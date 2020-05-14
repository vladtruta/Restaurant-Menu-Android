package com.vladtruta.restaurantmenu.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladtruta.restaurantmenu.data.model.local.MenuCourse
import com.vladtruta.restaurantmenu.data.repository.RestaurantRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class CourseDetailsViewModel : ViewModel() {
    companion object {
        private const val DEFAULT_ROW_ID = -1
    }

    private val ignoredExceptionHandler = CoroutineExceptionHandler { _, _ -> }

    private val messageExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _errorMessage.value = throwable.message
    }

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private var rowId = DEFAULT_ROW_ID
    private var itemAlreadyExists = false

    fun checkIfItemAlreadyExists(id: Int) {
        viewModelScope.launch(ignoredExceptionHandler) {
            rowId = RestaurantRepository.getCartItemIdByMenuCourseId(id)
            itemAlreadyExists = true
        }
    }

    fun addToOrUpdateCart(menuCourse: MenuCourse, quantity: Int) {
        viewModelScope.launch(messageExceptionHandler) {
            if (rowId == DEFAULT_ROW_ID) {
                rowId = RestaurantRepository.addItemToCart(menuCourse, quantity).toInt()
                itemAlreadyExists = true
            } else {
                RestaurantRepository.addQuantityToAlreadyExistingInCart(rowId, quantity)
            }
        }
    }

    fun undoCart(quantity: Int) {
        viewModelScope.launch(messageExceptionHandler) {
            if (itemAlreadyExists) {
                RestaurantRepository.addQuantityToAlreadyExistingInCart(rowId, -quantity)
            } else {
                RestaurantRepository.deleteItemFromCart(rowId)
                itemAlreadyExists = false
                rowId = DEFAULT_ROW_ID
            }
        }
    }
}