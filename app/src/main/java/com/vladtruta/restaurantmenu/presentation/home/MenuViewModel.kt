package com.vladtruta.restaurantmenu.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladtruta.restaurantmenu.data.model.local.Category
import com.vladtruta.restaurantmenu.data.model.local.MenuCourse
import com.vladtruta.restaurantmenu.data.repository.RestaurantRepository
import kotlinx.coroutines.launch

class MenuViewModel: ViewModel() {
    val categories = RestaurantRepository.getAllCategories()

    private val _filteredMenuCourses = MutableLiveData<List<MenuCourse>>()
    val filteredMenuCourses: LiveData<List<MenuCourse>> = _filteredMenuCourses

    fun getMenuCoursesByCategory(category: Category) {
        viewModelScope.launch {
            val result = RestaurantRepository.getMenuCoursesByCategory(category.name)
            _filteredMenuCourses.value = result
        }
    }
}