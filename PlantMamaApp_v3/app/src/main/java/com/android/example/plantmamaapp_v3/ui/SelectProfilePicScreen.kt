package com.android.example.plantmamaapp_v3.ui

import com.google.accompanist.permissions.rememberPermissionState
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
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
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.android.example.plantmamaapp_v3.MainActivity
import com.android.example.plantmamaapp_v3.R
import com.android.example.plantmamaapp_v3.data.WaterRepository
import com.android.example.plantmamaapp_v3.ui.navigation.NavigationDestination
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.text.SimpleDateFormat
import java.util.Locale


//class CameraMainScreen () : ComponentActivity(){
object SelectProfilePicDestination: NavigationDestination {
    override val route = "select_profile_pic_start"
}

    @Composable
    fun SelectProfilePicScreen(
        viewModelPlantMamaMainScreen: PlantMamaMainScreenViewModel,
        //onPhotoSelected:() -> Unit,
        navController: NavController,
    ){


        val scaffoldState = rememberBottomSheetScaffoldState()
        val context = LocalContext.current

        val controller = remember {
            LifecycleCameraController(context).apply {
                setEnabledUseCases(
                    CameraController.IMAGE_CAPTURE
                )
            }
        }



        val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = {
                    uri -> viewModelPlantMamaMainScreen.currentUri = uri!!
                navController.popBackStack()

            }
        )


        //CheckCameraPermission()

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
                        IconButton(onClick = {navController.navigateUp()},modifier = Modifier
                            .offset(16.dp, 8.dp) ) {
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
                                singlePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Photo,
                                contentDescription = "Open gallery"
                            )
                        }
                        IconButton(
                            onClick = {
                                takePhoto(
                                    controller = controller,
                                    //plantName = viewModelPlantMamaMainScreen.currentPlant.name,
                                    context = context,
                                    viewModelPlantMamaMainScreen = viewModelPlantMamaMainScreen,
                                    navController = navController,
                                )
                                navController.popBackStack()

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
        //plantName: String,
        context: Context,
        viewModelPlantMamaMainScreen: PlantMamaMainScreenViewModel,
        navController: NavController,
    ) //: Uri
    {

        // Create time stamped name and MediaStore entry.
        val TAG = "PlantMamaApp"
        val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/PlantMama/profilePic/${name}")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        //var uri: Uri  = Uri.EMPTY


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
                    navController.popBackStack()
                  // uri = outputFileResults.savedUri!!

                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                }

            }

        )
        //return uri
    }






