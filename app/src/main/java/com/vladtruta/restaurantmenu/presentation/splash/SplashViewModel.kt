package com.vladtruta.restaurantmenu.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladtruta.restaurantmenu.data.repository.RestaurantRepository
import kotlinx.coroutines.*

class SplashViewModel : ViewModel() {

    private val _refreshSuccessful = MutableLiveData<Boolean>()
    val refreshSuccessful: LiveData<Boolean> = _refreshSuccessful

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val messageExceptionHandler = CoroutineExceptionHandler { _, error ->
        _errorMessage.value = error.message
    }

    fun refresh() {
        _refreshSuccessful.value = false
        _errorMessage.value = ""
        viewModelScope.launch(messageExceptionHandler) {
            RestaurantRepository.clearDatabase()
            loadDataFromNetwork()
            _refreshSuccessful.value = true
        }
    }

    private suspend fun loadDataFromNetwork() {
        coroutineScope {
            val tasks = listOf(
                async { RestaurantRepository.refreshCategories() },
                async { RestaurantRepository.refreshMenuCourses() }
            )
            tasks.awaitAll()
        }
    }
}