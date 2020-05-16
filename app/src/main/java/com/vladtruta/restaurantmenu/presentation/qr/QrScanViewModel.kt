package com.vladtruta.restaurantmenu.presentation.qr

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.gson.JsonSyntaxException
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.data.model.local.Customer
import com.vladtruta.restaurantmenu.data.repository.RestaurantRepository
import com.vladtruta.restaurantmenu.utils.GsonHelper
import com.vladtruta.restaurantmenu.utils.SessionUtils
import com.vladtruta.restaurantmenu.utils.UIUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QrScanViewModel : ViewModel() {

    private val errorMessageExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _errorMessage.postValue(throwable.message)
    }

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _customerAddedSuccessMessage = MutableLiveData<String>()
    val customerAddedSuccessMessage: LiveData<String> = _customerAddedSuccessMessage

    val qrCodeAnalyzer = object : ImageAnalysis.Analyzer {
        override fun analyze(imageProxy: ImageProxy) {
            @androidx.camera.core.ExperimentalGetImage
            val mediaImage = imageProxy.image ?: return

            val rotation = degreesToFirebaseRotation(imageProxy.imageInfo.rotationDegrees)
            val image = FirebaseVisionImage.fromMediaImage(mediaImage, rotation)

            val options = FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
                .build()
            val detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)

            detector.detectInImage(image)
                .addOnSuccessListener {
                    processScannedBarcodes(it)
                }
                .addOnFailureListener {
                    _errorMessage.value = UIUtils.getString(R.string.error_message)
                }

            imageProxy.close()
        }

        private fun degreesToFirebaseRotation(degrees: Int): Int = when (degrees) {
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
        }
    }

    private fun processScannedBarcodes(barcodes: List<FirebaseVisionBarcode>) {
        viewModelScope.launch(Dispatchers.Default + errorMessageExceptionHandler) {
            barcodes.lastOrNull()?.let {
                try {
                    val converted = GsonHelper.instance.fromJson(
                        it.rawValue,
                        Customer::class.java
                    )
                    if (!converted.isValid()) {
                        throw Exception("This QR code does not represent a valid customer")
                    }
                    if (converted.tableName != SessionUtils.getTableName()) {
                        throw Exception("The customer does not belong to this table")
                    }

                    RestaurantRepository.insertCustomer(converted)
                    _customerAddedSuccessMessage.postValue(UIUtils.getString(R.string.add_customer_success, converted.fullName))
                } catch (error: JsonSyntaxException) {
                    throw Exception("This QR code does not represent a valid customer", error)
                }
            }
        }
    }
}