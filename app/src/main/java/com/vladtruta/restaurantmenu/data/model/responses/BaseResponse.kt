package com.vladtruta.restaurantmenu.data.model.responses

import com.google.gson.annotations.SerializedName

data class BaseResponse<out T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: T
)