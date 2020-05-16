package com.vladtruta.restaurantmenu.data.model.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "customers")
data class Customer(
    val fullName: String,
    val tableName: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable