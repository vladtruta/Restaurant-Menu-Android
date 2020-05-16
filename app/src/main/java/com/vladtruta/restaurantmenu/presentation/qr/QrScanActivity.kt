package com.vladtruta.restaurantmenu.presentation.qr

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.databinding.ActivityQrScanBinding
import com.vladtruta.restaurantmenu.utils.UIUtils
import kotlinx.android.synthetic.main.activity_qr_scan.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.Executors


class QrScanActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks,
    QrCodeAnalyzer.QrCodesDetectedListener {
    companion object {
        private const val TAG = "QrScanActivity"

        private const val REQ_CODE_CAMERA_PERMISSION = 283
        private val cameraPermissions = arrayOf(Manifest.permission.CAMERA)
    }

    private lateinit var binding: ActivityQrScanBinding
    private val viewModel by viewModels<QrScanViewModel>()

    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null

    private val cameraExecutor = Executors.newSingleThreadExecutor()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (hasCameraPermissions()) {
                tryStartCamera()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrScanBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_qr_scan)

        initObservers()
        tryStartCamera()
    }

    private fun initObservers() {
        viewModel.statusMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    @AfterPermissionGranted(REQ_CODE_CAMERA_PERMISSION)
    private fun tryStartCamera() {
        if (hasCameraPermissions()) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

            cameraProviderFuture.addListener(Runnable {
                val cameraProvider = cameraProviderFuture.get()

                preview = Preview.Builder().build()
                imageAnalyzer = ImageAnalysis.Builder().build().also {
                    val analyzer = QrCodeAnalyzer(this)
                    it.setAnalyzer(cameraExecutor, analyzer)
                }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                try {
                    cameraProvider.unbindAll()
                    camera = cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageAnalyzer
                    )
                    preview?.setSurfaceProvider(camera_txv.createSurfaceProvider(camera?.cameraInfo))
                } catch (error: Exception) {
                    Log.e(TAG, "Use case binding failed", error)
                }
            }, ContextCompat.getMainExecutor(this))
        } else {
            requestCameraPermissions()
        }
    }

    override fun onQrCodesDetectSuccess(qrCodes: List<FirebaseVisionBarcode>) {
        viewModel.insertCustomers(qrCodes)
    }

    override fun onQrCodesDetectFailure(error: String?) {
        if (error.isNullOrEmpty()) {
            return
        }
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    private fun hasCameraPermissions(): Boolean {
        return EasyPermissions.hasPermissions(this, *cameraPermissions)
    }

    private fun requestCameraPermissions() {
        EasyPermissions.requestPermissions(
            this,
            UIUtils.getString(R.string.camera_permission_rationale),
            REQ_CODE_CAMERA_PERMISSION,
            *cameraPermissions
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        // Some permissions have been denied
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        // Some permissions have been granted
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}