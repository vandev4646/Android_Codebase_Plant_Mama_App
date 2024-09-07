package com.android.example.plantmamaapp_v3.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.android.example.plantmamaapp_v3.R

@Composable
fun PhotoDisplay(
    viewModel: PlantMamaMainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    photoDisplayViewModel: PhotoDisplayViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onPhotoClick:() -> Unit)
{
    val photoListUiState by photoDisplayViewModel.photoListUiState.collectAsState()
    val photoList = photoListUiState.photoList
    val photoList2 = photoDisplayViewModel.photoLists

    if (photoList.isEmpty()) {
        Text(
            text = "No items so sad",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier,
        )
    }
    else {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(items = photoList, key = { it.id }) { item ->
            displayPhotos(item.uri, onPhotoClick = onPhotoClick)
        }
    }}
}


@Composable
fun displayPhotos (uri: String,
                   viewModel: PlantMamaMainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
                   onPhotoClick:() -> Unit
                   ){

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
                onPhotoClick()
            },
            //.clip(CircleShape),
        contentScale = ContentScale.FillWidth,
    )
}