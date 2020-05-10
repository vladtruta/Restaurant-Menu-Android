package com.vladtruta.restaurantmenu.data.model.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "menuCourses")
data class MenuCourse(
    val category: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val portionSize: String,
    val price: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable