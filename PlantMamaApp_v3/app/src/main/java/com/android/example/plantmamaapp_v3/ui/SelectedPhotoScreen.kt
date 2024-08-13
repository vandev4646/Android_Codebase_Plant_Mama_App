package com.android.example.plantmamaapp_v3.ui

import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.android.example.plantmamaapp_v3.data.photos
import androidx.compose.foundation.Image as Image1


@Composable
fun SelectedPhotoScreen(viewModel: PlantMamaMainScreenViewModel){

    var selectedImagesUris = remember {
        viewModel.selectedImagesUris
    }

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

                    }) {
                        Text(text = "Keep Selected")
                    }
                    Button(onClick = {

                    }) {
                        Text(text = "Cancel")
                    }
                }

    }



}

@Composable
fun SelectedSinglePhotoScreen(uri: Uri){


    Surface(modifier = Modifier.fillMaxSize()) {

            ImageDisplay(uri = uri)



        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {

            }) {
                Text(text = "Keep Selected")
            }
            Button(onClick = {

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