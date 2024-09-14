package com.android.example.plantmamaapp_v3.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.android.example.plantmamaapp_v3.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object InspectPhotoScreenDestination : NavigationDestination {
    override val route = "inspect_photo_screen"
    const val itemIdArg = "itemId"
    val routeWithArgs = "${InspectPhotoScreenDestination.route}/{$itemIdArg}"
}

@Composable
fun InspectPhotoScreen(
    viewModel: PlantMamaMainScreenViewModel,
    navController: NavController,
    inspectPhotoViewModel: InspectPhotoViewModel =  viewModel(factory = AppViewModelProvider.Factory)
) {

    val coroutineScope = rememberCoroutineScope()

    val photoViewUiState by inspectPhotoViewModel.photoViewUiState.collectAsState()
    val photoItem = photoViewUiState.photo

    Surface(modifier = Modifier.fillMaxSize()) {

        ImageDisplay2(uri = photoItem.uri)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {
                navController.popBackStack()
            }) {
                Text(text = "Keep Photo")
            }
            Button(onClick = {
                if(photoItem != null){
                    coroutineScope.launch {
                        inspectPhotoViewModel.deletePhoto(photoItem)
                    }
                }

                navController.popBackStack()

            }) {
                Text(text = "Delete Photo")
            }
        }

    }
}

@Composable
fun ImageDisplay2(uri: String) {
    Image(
        painter = rememberAsyncImagePainter(
            model = Uri.parse(uri)  // or ht
        ),
        contentDescription = "123",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillWidth
    )

}