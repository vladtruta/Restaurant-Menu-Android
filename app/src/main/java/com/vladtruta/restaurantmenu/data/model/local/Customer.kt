package com.vladtruta.restaurantmenu.data.model.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Customer(
    val fullName: String,
    val tableNumber: Int
) : Parcelable