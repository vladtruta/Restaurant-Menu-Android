package com.vladtruta.restaurantmenu.data.model.responses

import com.google.gson.annotations.SerializedName
import com.vladtruta.restaurantmenu.data.model.local.Customer

data class CustomerResponse(
    @SerializedName("fullName")
    val fullName: String? = null,
    @SerializedName("tableName")
    val tableName: String? = null,
    @SerializedName("id")
    val id: Int? = null
) {
    fun toCustomer(): Customer? {
        fullName ?: return null
        tableName ?: return null
        id ?: return null

        return Customer(fullName, tableName, id)
    }
}