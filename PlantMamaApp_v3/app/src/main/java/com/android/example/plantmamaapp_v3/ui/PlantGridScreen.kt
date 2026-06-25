package com.android.example.plantmamaapp_v3.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.example.plantmamaapp_v3.R
import com.android.example.plantmamaapp_v3.data.Plant
import com.android.example.plantmamaapp_v3.ui.navigation.NavigationDestination
import com.android.example.plantmamaapp_v3.ui.photo.PlantProfileDestination

object PlantMamaHomeDesintation : NavigationDestination {
    override val route = "plant_mama_home"
}

@Composable
fun PlantGridScreen(
    plants: List<Plant>,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {

                        if(plants.isEmpty()){
                            Column(horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                                Text(
                                    text = "Welcome! You currently do not have any plants listed. Click + to add one. Then click on the newly created plant to add reminders and photos",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(16.dp),
                                )
                            }
                            //Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_large)))

                        }
                        else{
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = 150.dp, ),
                                modifier = modifier.padding(horizontal = 4.dp).fillMaxSize(),
                            ) {
                                items(items = plants) {
                                    PlantItem(
                                        plant = it,
                                        navController = navController,
                                        viewModel = viewModel
                                    )
                                }
                            }
                        }


}


@Composable
fun PlantItem(
    plant: Plant,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .clickable {
                viewModel.currentPlant = plant
                navController.navigate("${PlantProfileDestination.route}/${viewModel.currentPlant.id}")
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(plant.profilePic.ifEmpty { R.drawable.plant_logo })
                    .build(),
                contentDescription = null,
                modifier = modifier,
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .fillMaxWidth()
            ){
                Text(
                    text = plant.name,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(1.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    //style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


