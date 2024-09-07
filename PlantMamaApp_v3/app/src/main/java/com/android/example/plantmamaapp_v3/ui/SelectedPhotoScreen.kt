package com.android.example.plantmamaapp_v3.ui

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.android.example.plantmamaapp_v3.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image as Image1

object SelectedPhotoScreenDestination : NavigationDestination {
    override val route = "selected_photo"
}

object SelectedSinglePhotoScreenDestination : NavigationDestination {
    override val route = "selected_single_photo"
}

@Composable
fun SelectedPhotoScreen(viewModel: PlantMamaMainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
                        photoViewModel: PhotoViewModel = viewModel(factory = AppViewModelProvider.Factory),
                        onCancel: () -> Unit = {},
                        plantId: Int
                        ){

    var selectedImagesUris = remember {
        viewModel.selectedImagesUris
    }

    val coroutineScope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(){

            items(selectedImagesUris) {  uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

        }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = {

                        selectedImagesUris.forEach{uri ->
                           coroutineScope.launch {
                               photoViewModel.savePhoto(plantId, uri.toString())
                           }
                        }

                        onCancel()

                    }) {
                        Text(text = "Keep Selected")
                    }
                    Button(onClick = {
                        onCancel()

                    }) {
                        Text(text = "Cancel")
                    }
                }

    }



}

@Composable
fun SelectedSinglePhotoScreen(uri: Uri,
                              viewModel: PlantMamaMainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
                              photoViewModel: PhotoViewModel = viewModel(factory = AppViewModelProvider.Factory),
                              onCancel: () -> Unit = {},
                              plantId: Int){


    val coroutineScope = rememberCoroutineScope()

      //  coroutineScope.launch {
      //      photoViewModel.savePhoto(viewModel.currentPlant.id, uri.toString())
      //  }


    Surface(modifier = Modifier.fillMaxSize()) {

            ImageDisplay(uri = uri)



        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {
                coroutineScope.launch {
                    photoViewModel.savePhoto(plantId, uri.toString())
                }
                onCancel()

            }) {
                Text(text = "Keep Selected")
            }
            Button(onClick = {
                onCancel()

            }) {
                Text(text = "Cancel")
            }
        }

    }



}

@Composable
fun ImageDisplay(uri: Uri){
    Image1(
        painter = rememberImagePainter(
            data  = Uri.parse(uri.toString())  // or ht
        )
        ,
        contentDescription = "123",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillWidth
    )

}