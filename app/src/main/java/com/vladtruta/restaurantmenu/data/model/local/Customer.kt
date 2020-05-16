package com.vladtruta.restaurantmenu.data.model.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "customers")
data class Customer(
    val fullName: String? = null,
    val tableName: String? = null,
    @PrimaryKey(autoGenerate = false)
    val id: Int? = 0
) : Parcelable {
    fun isValid(): Boolean {
        return id != null && fullName != null && tableName != null
    }
}