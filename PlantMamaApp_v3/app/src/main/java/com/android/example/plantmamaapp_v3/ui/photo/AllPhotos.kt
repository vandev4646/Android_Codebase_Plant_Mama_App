package com.android.example.plantmamaapp_v3.ui.photo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.android.example.plantmamaapp_v3.ui.AppViewModelProvider
import com.android.example.plantmamaapp_v3.ui.navigation.NavigationDestination

object AllPhotosDesintation : NavigationDestination {
    override val route = "all_photos"
}

@Composable
fun AllPhotos(modifier: Modifier = Modifier, navController: NavController){

    val allPhotosViewModel: AllPhotosViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val allPhotosUiState by allPhotosViewModel.allPhotosUiState.collectAsState()
    val photoList = allPhotosUiState.photoList
    val noPhotoMessage = "No Photos. To add a new photo, navigate to a plant profile. Then click the camera icon on the right sidebar to add photos for that plant"

    PhotoDisplay(
        photoList,
        navController = navController,
        modifier = modifier.fillMaxSize(),
        noPlantMessage = noPhotoMessage
    )

}