/*
 * Copyright (C) 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vladtruta.restaurantmenu.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vladtruta.restaurantmenu.data.model.local.CartItem
import com.vladtruta.restaurantmenu.data.repository.RestaurantRepository
import com.vladtruta.restaurantmenu.utils.GsonHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SendKitchenRequestWork(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    companion object {
        private const val RETRY_LIMIT = 3

        const val ARG_CART_ITEMS = "ARG_CART_ITEMS"
        const val ARG_TABLE_NAME = "KEY_IN_LOGS_ARRAY"
    }

    override suspend fun doWork(): Result {
        val cartItems = withContext(Dispatchers.Default) {
            val cartItemsSerialized = inputData.getString(ARG_CART_ITEMS) ?: return@withContext null
            GsonHelper.instance.fromJson(cartItemsSerialized, Array<CartItem>::class.java).toList()
        } ?: return Result.failure()

        val tableName = inputData.getString(ARG_TABLE_NAME) ?: return Result.failure()

        return withContext(Dispatchers.IO) {
            try {
                RestaurantRepository.sendKitchenRequest(cartItems, tableName)
                Result.success()
            } catch (error: Exception) {
                if (runAttemptCount <= RETRY_LIMIT) {
                    Result.retry()
                } else {
                    Result.failure()
                }
            }
        }
    }
}