package com.vladtruta.restaurantmenu.data.model.local

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "menu")
data class MenuCourse(
    @Embedded(prefix = "category_")
    val category: Category,
    val name: String,
    val description: String,
    val photoUrl: String,
    val portionSize: String,
    val price: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable