package com.vladtruta.restaurantmenu.data.model.local

import android.os.Parcelable
import com.vladtruta.restaurantmenu.data.model.local.CartItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    val cartItems: List<CartItem>,
    val payingUserId: Int
) : Parcelable