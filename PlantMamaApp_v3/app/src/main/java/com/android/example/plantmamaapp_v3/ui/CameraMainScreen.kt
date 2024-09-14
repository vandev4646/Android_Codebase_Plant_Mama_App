package com.android.example.plantmamaapp_v3.ui

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.android.example.plantmamaapp_v3.R
import com.android.example.plantmamaapp_v3.ui.navigation.NavigationDestination
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.text.SimpleDateFormat
import java.util.Locale


object CameraStartDestination : NavigationDestination {
    override val route = "camera_start"
}

@Composable
fun StartMainCamera(
    viewModelPlantMamaMainScreen: PlantMamaMainScreenViewModel,
    navController: NavController,
) {

    val scaffoldState = rememberBottomSheetScaffoldState()
    val context = LocalContext.current

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }


    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            viewModelPlantMamaMainScreen.selectedImagesUris = uris
            navController.navigate(SelectedPhotoScreenDestination.route)

        }
    )

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                viewModelPlantMamaMainScreen.currentUri = uri
            }
            navController.popBackStack()
        }
    )

    var cameraForProfile = remember { viewModelPlantMamaMainScreen.cameraForProfile }

    CheckCameraPermission()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            CameraPreview(
                controller = controller,
                modifier = Modifier
                    .fillMaxSize()
            )
            if (navController.previousBackStackEntry != null) {
                IconButton(
                    onClick = { navController.navigateUp() }, modifier = Modifier
                        .offset(16.dp, 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.nav_add_reminder)
                    )
                }
            }

            IconButton(
                onClick = {
                    controller.cameraSelector =
                        if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        } else CameraSelector.DEFAULT_BACK_CAMERA
                },
                modifier = Modifier
                    .offset(16.dp, 40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Switch camera"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(
                    onClick = {
                        if (!cameraForProfile) {
                            multiplePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        if (cameraForProfile) {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )

                        }

                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Photo,
                        contentDescription = "Open gallery"
                    )
                }
                IconButton(
                    onClick = {
                        if (!cameraForProfile) {
                            takePhoto(
                                controller = controller,
                                plantName = viewModelPlantMamaMainScreen.currentPlant.name,
                                context = context,
                                viewModelPlantMamaMainScreen = viewModelPlantMamaMainScreen,
                                navigationAfter = {
                                    navController.navigate(
                                        SelectedSinglePhotoScreenDestination.route
                                    )
                                }
                            )
                        }

                        if (cameraForProfile) {
                            takePhoto(
                                controller = controller,
                                plantName = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                                    .format(System.currentTimeMillis()),
                                context = context,
                                viewModelPlantMamaMainScreen = viewModelPlantMamaMainScreen,
                                navigationAfter = { navController.popBackStack() }
                            )
                        }

                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Take photo"
                    )
                }
            }
        }
    }


}


private fun takePhoto(
    controller: LifecycleCameraController,
    plantName: String,
    context: Context,
    viewModelPlantMamaMainScreen: PlantMamaMainScreenViewModel,
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
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/PlantMama/${plantName}")
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
                val msg = "Photo capture succeded: ${outputFileResults.savedUri}"
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                Log.d(TAG, msg)
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




