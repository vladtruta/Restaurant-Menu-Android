package com.vladtruta.restaurantmenu.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    val toolbarText = MutableLiveData<String>()
}