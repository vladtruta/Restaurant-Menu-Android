package com.vladtruta.restaurantmenu.presentation.qr

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.gson.JsonSyntaxException
import com.vladtruta.restaurantmenu.data.model.local.Customer
import com.vladtruta.restaurantmenu.data.repository.RestaurantRepository
import com.vladtruta.restaurantmenu.utils.GsonHelper
import com.vladtruta.restaurantmenu.utils.SessionUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QrScanViewModel : ViewModel() {

    private val _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> = _statusMessage

    private val errorMessageExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _statusMessage.value = throwable.message
    }

    fun insertCustomers(customers: List<FirebaseVisionBarcode>) {
        viewModelScope.launch(errorMessageExceptionHandler) {
            val customersConverted = withContext(Dispatchers.Default) {
                val customersConverted = mutableListOf<Customer>()
                customers.forEach {
                    try {
                        val converted =
                            GsonHelper.instance.fromJson(it.rawValue, Customer::class.java)
                        if (converted.tableName != SessionUtils.getTableName()) {
                            throw Exception("The customer does not belong to this table")
                        }
                        customersConverted.add(converted)
                        _statusMessage.postValue("Added customer ${converted.fullName} successfully!")
                    } catch (error: JsonSyntaxException) {
                        throw Exception("This QR code does not represent a valid customer", error)
                    }
                }
                return@withContext customersConverted.toTypedArray()
            }

            RestaurantRepository.insertCustomers(*customersConverted)
        }
    }
}