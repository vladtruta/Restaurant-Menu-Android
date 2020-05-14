package com.vladtruta.restaurantmenu.data.model.local

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "cart")
data class CartItem(
    @Embedded(prefix = "menuCourse_")
    val menuCourse: MenuCourse,
    var quantity: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
): Parcelable