package com.example.startcametax

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.RequestCallback
import java.lang.Exception
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    var preview: Preview? = null
    var takePhoto: Button? = null
    var previewView: PreviewView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        previewView=findViewById(R.id.preview)
        PermissionX.init(this)
            .permissions(android.Manifest.permission.CAMERA)
            .request(RequestCallback { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    startCamera()
                }
            })
    }

    private fun startCamera() {
        var processCameraProvider = ProcessCameraProvider.getInstance(this)
        processCameraProvider.addListener(Runnable {
            var cameraProvider = processCameraProvider.get()
            preview = Preview.Builder().build()
            var cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()
            cameraProvider.unbindAll()
            try {
                var camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview
                )
                preview?.setSurfaceProvider(previewView?.createSurfaceProvider(camera.cameraInfo))
            } catch (e: Exception) {
                Log.i("cameraErr", e.toString())
            }

        }, ContextCompat.getMainExecutor(this))
    }

}