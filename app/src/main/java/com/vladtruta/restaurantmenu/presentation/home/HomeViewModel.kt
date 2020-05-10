package com.vladtruta.restaurantmenu.presentation.home

import androidx.lifecycle.*
import com.vladtruta.restaurantmenu.data.model.local.Category
import com.vladtruta.restaurantmenu.data.model.local.MenuCourse
import com.vladtruta.restaurantmenu.data.repository.RestaurantRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val categories = RestaurantRepository.getAllCategories()
    val filteredMenuCourses = MutableLiveData<List<MenuCourse>>()

    fun getMenuCoursesByCategory(category: Category) {
        viewModelScope.launch {
            val result = RestaurantRepository.getMenuCoursesByCategory(category.name)
            filteredMenuCourses.value = result
        }
    }
}