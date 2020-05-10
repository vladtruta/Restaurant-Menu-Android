package com.vladtruta.restaurantmenu.data.model.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KitchenDishQuantity(
    val dishName: String? = null,
    val quantity: Int? = null
) : Parcelable