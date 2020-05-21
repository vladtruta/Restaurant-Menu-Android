package com.vladtruta.restaurantmenu.data.work

import androidx.work.*
import com.vladtruta.restaurantmenu.data.model.local.CartItem
import com.vladtruta.restaurantmenu.utils.GsonHelper
import com.vladtruta.restaurantmenu.utils.RestaurantApp

object WorkUtils {
    const val TAG_SEND_KITCHEN_ORDER = "TAG_SEND_KITCHEN_ORDER"

    fun enqueueSendKitchenOrder(cartItems: List<CartItem>, tableName: String) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val work = OneTimeWorkRequestBuilder<SendKitchenRequestWork>()
            .setConstraints(constraints)
            .setInputData(
                workDataOf(
                    SendKitchenRequestWork.ARG_CART_ITEMS to GsonHelper.instance.toJson(cartItems),
                    SendKitchenRequestWork.ARG_TABLE_NAME to tableName
                )
            )
            .addTag(TAG_SEND_KITCHEN_ORDER)
            .build()

        WorkManager.getInstance(RestaurantApp.instance).enqueue(work)
    }
}