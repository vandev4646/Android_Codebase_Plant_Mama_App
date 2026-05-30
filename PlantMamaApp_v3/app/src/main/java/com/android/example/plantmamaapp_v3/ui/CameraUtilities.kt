package com.android.example.plantmamaapp_v3.ui

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale


fun takePhoto(
    controller: LifecycleCameraController,
    context: Context,
    viewModelPlantMamaMainScreen: MainScreenViewModel,
    navigationAfter: () -> Unit,
) {

    // Create time stamped name and MediaStore entry.
    val TAG = "PlantMamaApp"
    val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
        .format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/PlantMama")
        }
    }

    // Create output options object which contains file + metadata
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .build()


    controller.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                viewModelPlantMamaMainScreen.selectedImagesUris.plusElement(outputFileResults.savedUri)
                viewModelPlantMamaMainScreen.currentUri = outputFileResults.savedUri!!
                navigationAfter()
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
            }

        }

    )
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckCameraPermission() {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val requestLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted
            } else {
                // Handle permission denial
            }
        }

    LaunchedEffect(cameraPermissionState) {
        if (cameraPermissionState.status.isGranted && cameraPermissionState.status.shouldShowRationale) {
            // Show rationale if needed
        } else {
            requestLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}


fun saveImageToNewFolder(context: Context, uri: Uri): Uri {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val newFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Plant_Mama")
    if (!newFile.exists()) {
        newFile.mkdirs()
    }
    val fileName = "${System.currentTimeMillis()}.jpg"
    val outputFile = File(newFile, fileName)
    val outputStream = FileOutputStream(outputFile)

    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
    return Uri.fromFile(outputFile)
}
