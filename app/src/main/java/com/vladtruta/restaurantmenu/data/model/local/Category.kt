package com.vladtruta.restaurantmenu.data.model.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "categories")
data class Category(
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable {
    companion object {
        val STARTERS = Category("Starters", 1)
        val MAIN_COURSES = Category("Main Courses", 2)
        val BEVERAGES = Category("Beverages", 3)
        val DESSERTS = Category("Desserts", 4)
    }
}
