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
) : Parcelable
