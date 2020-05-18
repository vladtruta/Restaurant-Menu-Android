package com.vladtruta.restaurantmenu.data.model.requests

import com.google.gson.annotations.SerializedName
import com.vladtruta.restaurantmenu.data.model.local.CartItem

data class KitchenRequest(
    @SerializedName("cartItems")
    val cartItems: List<CartItem>,
    @SerializedName("tableName")
    val tableName: String
)