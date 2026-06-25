package com.android.example.plantmamaapp_v3.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.example.plantmamaapp_v3.ui.navigation.NavigationDestination
import com.android.example.plantmamaapp_v3.ui.photo.SelectedPhotoScreenDestination
import com.android.example.plantmamaapp_v3.ui.photo.SelectedSinglePhotoScreenDestination


object CameraStartDestination : NavigationDestination {
    override val route = "camera_start"
    const val isProfileArg = "isProfile"
    val routeWithArgs = "$route/{$isProfileArg}"
}

@Composable
fun StartMainCamera(
    viewModelMainScreen: MainScreenViewModel,
    navController: NavController,
    cameraForProfile: Boolean
) {

    val context = LocalContext.current

    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }


    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris: List<Uri> ->
            val mutableUris = selectedImageUris.toMutableList()

            uris.forEach{ uri ->
                mutableUris.add(saveImageToNewFolder(context, uri))
                //selectedImageUris.plusElement(saveImageToNewFolder(context, uri))
            }
            selectedImageUris = mutableUris
            viewModelMainScreen.selectedImagesUris = selectedImageUris
            navController.navigate(SelectedPhotoScreenDestination.route)

        }
    )

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                viewModelMainScreen.currentUri = saveImageToNewFolder(context, uri)
            }
            navController.popBackStack()
        }
    )

    //var cameraForProfile = viewModelMainScreen.cameraForProfile

    CheckCameraPermission()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black
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
                        contentDescription = "Back"
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
                                context = context,
                                viewModelPlantMamaMainScreen = viewModelMainScreen,
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
                                context = context,
                                viewModelPlantMamaMainScreen = viewModelMainScreen,
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




