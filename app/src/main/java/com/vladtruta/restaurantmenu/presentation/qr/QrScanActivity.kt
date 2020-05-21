package com.vladtruta.restaurantmenu.presentation.qr

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
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
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.databinding.ActivityQrScanBinding
import com.vladtruta.restaurantmenu.utils.UIUtils
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class QrScanActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    companion object {
        private const val TAG = "QrScanActivity"

        private const val TOAST_HANDLER_DELAY = 300L

        private const val REQ_CODE_CAMERA_PERMISSION = 283
        private val cameraPermissions = arrayOf(Manifest.permission.CAMERA)
    }

    private val viewModel by viewModels<QrScanViewModel>()
    private lateinit var binding: ActivityQrScanBinding

    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null

    private val toastSuccessHandler = Handler()
    private val toastErrorHandler = Handler()

    private lateinit var cameraExecutor: Executor

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
        setContentView(binding.root)

        setSupportActionBar(binding.qrScanMtb)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        cameraExecutor = Executors.newSingleThreadExecutor()

        initObservers()
        tryStartCamera()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        toastSuccessHandler.removeCallbacksAndMessages(null)
        toastErrorHandler.removeCallbacksAndMessages(null)

        super.onDestroy()
    }

    private fun initObservers() {
        viewModel.errorMessage.observe(this, Observer {
            toastErrorHandler.removeCallbacksAndMessages(null)
            toastErrorHandler.postDelayed({
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }, TOAST_HANDLER_DELAY)

            finish()
        })

        viewModel.customerAddedSuccessMessage.observe(this, Observer {
            toastSuccessHandler.removeCallbacksAndMessages(null)
            toastSuccessHandler.postDelayed({
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }, TOAST_HANDLER_DELAY)

            finish()
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
                    it.setAnalyzer(cameraExecutor, viewModel.qrCodeAnalyzer)
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
                    preview?.setSurfaceProvider(binding.cameraTxv.createSurfaceProvider(camera?.cameraInfo))
                } catch (error: Exception) {
                    Log.e(TAG, "Use case binding failed", error)
                }
            }, ContextCompat.getMainExecutor(this))
        } else {
            requestCameraPermissions()
        }
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