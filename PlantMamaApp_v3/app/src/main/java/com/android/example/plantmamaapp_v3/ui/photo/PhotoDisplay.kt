package com.android.example.plantmamaapp_v3.ui.photo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.android.example.plantmamaapp_v3.data.Photo
import com.android.example.plantmamaapp_v3.ui.AppViewModelProvider
import com.android.example.plantmamaapp_v3.ui.MainScreenViewModel

@Composable
fun PhotoDisplay(
    photoList: List<Photo>,
    noPlantMessage: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {


    if (photoList.isEmpty()) {
        Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
            Text(
                text = noPlantMessage,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier,
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(items = photoList, key = { it.id }) { item ->
                DisplayPhotos(item.uri, navController = navController, photoId = item.id)
            }
        }
    }
}


@Composable
fun DisplayPhotos(
    uri: String,
    photoId: Int,
    viewModel: MainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .clickable {
                viewModel.deletePhotoPath = uri
                navController.navigate("${InspectPhotoScreenDestination.route}/${photoId}")
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .placeholder(R.drawable.plant_logo)
                .build(),
            contentDescription = "",
            modifier = Modifier
                .padding(2.dp)
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }

}