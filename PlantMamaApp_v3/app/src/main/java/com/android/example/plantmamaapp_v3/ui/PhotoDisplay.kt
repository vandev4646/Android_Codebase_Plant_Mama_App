package com.android.example.plantmamaapp_v3.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.example.plantmamaapp_v3.R

@Composable
fun PhotoDisplay(
    photoDisplayViewModel: PhotoDisplayViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController
) {
    val photoListUiState by photoDisplayViewModel.photoListUiState.collectAsState()
    val photoList = photoListUiState.photoList

    if (photoList.isEmpty()) {
        Text(
            text = "No items so sad",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier,
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(items = photoList, key = { it.id }) { item ->
                displayPhotos(item.uri, navController = navController, photoId = item.id)
            }
        }
    }
}


@Composable
fun displayPhotos(
    uri: String,
    photoId: Int,
    viewModel: PlantMamaMainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri)
            .placeholder(R.drawable.plant_logo)
            .build(),
        contentDescription = "",
        modifier = Modifier
            .padding(2.dp)
            .fillMaxSize()
            .clickable {
                viewModel.deletePhotoPath = uri
                navController.navigate("${InspectPhotoScreenDestination.route}/${photoId}")
            },
        contentScale = ContentScale.FillWidth,
    )
}