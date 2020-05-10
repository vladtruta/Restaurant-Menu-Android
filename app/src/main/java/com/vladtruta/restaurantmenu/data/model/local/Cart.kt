package com.vladtruta.restaurantmenu.data.model.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cart(
    val items: List<CartItem>
): Parcelable