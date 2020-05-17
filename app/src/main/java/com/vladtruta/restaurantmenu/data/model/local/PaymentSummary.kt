package com.vladtruta.restaurantmenu.data.model.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentSummary(
    val cartItems: List<CartItem>,
    val payingCustomer: Customer? = null,
    val totalPrice: Int
): Parcelable