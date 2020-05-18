package com.vladtruta.restaurantmenu.data.model.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vladtruta.restaurantmenu.data.model.requests.KitchenRequest
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "kitchen")
data class KitchenOrder(
    val cartItems: List<CartItem>,
    val tableName: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable {
    fun toKitchenRequest(): KitchenRequest {
        return KitchenRequest(cartItems, tableName)
    }
}