package com.vladtruta.restaurantmenu.presentation.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladtruta.restaurantmenu.data.repository.RestaurantRepository
import kotlinx.coroutines.*
import java.lang.Exception

class SplashViewModel : ViewModel() {

    val refreshSuccessful = MutableLiveData<Boolean>()
    val refreshErrorMessage = MutableLiveData<String>()

    fun refresh() {
        refreshSuccessful.value = false
        refreshErrorMessage.value = ""
        viewModelScope.launch {
            try {
                RestaurantRepository.clearDatabase()
                loadDataFromNetwork()
                refreshSuccessful.value = true
            } catch (error: Exception) {
                refreshErrorMessage.value = error.message
            }
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