package com.vladtruta.restaurantmenu.data.model.local

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "orders")
data class OrderedItem(
    @Embedded(prefix = "cartItem_")
    val cartItem: CartItem,
    @Embedded(prefix = "payingCustomer_")
    var payingCustomer: Customer? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable