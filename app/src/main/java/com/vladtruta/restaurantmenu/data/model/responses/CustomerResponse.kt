package com.vladtruta.restaurantmenu.data.model.responses

import com.vladtruta.restaurantmenu.data.model.local.Customer

data class CustomerResponse(
    val fullName: String? = null,
    val tableName: String? = null,
    val id: Int? = null
) {
    fun toCustomer(): Customer? {
        fullName ?: return null
        tableName ?: return null
        id ?: return null

        return Customer(fullName, tableName, id)
    }
}