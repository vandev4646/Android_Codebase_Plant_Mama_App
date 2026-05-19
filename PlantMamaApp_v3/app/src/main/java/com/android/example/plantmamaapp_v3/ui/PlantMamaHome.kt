package com.android.example.plantmamaapp_v3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
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

object PlantMamaHomeDesintation : NavigationDestination {
    override val route = "plant_mama_home"
}

@Composable
fun PlantGridScreen(
    plants: List<Plant>,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: PlantMamaMainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.menu_screen),
            contentDescription = null,
            modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        var showAddPlantDialog by rememberSaveable { mutableStateOf(false) }
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                PlantTopAppBar(
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            },

            floatingActionButton = {
                FloatingActionButton(onClick = {

                    showAddPlantDialog = true
                }) {
                    Icon(Icons.Filled.Add, "Floating action button.")
                }
            },

            content = { contentPadding ->

                if(plants.isEmpty()){
                    Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_large)))
                    Text(
                        text = "Welcome! You currently do not have any plants listed. Click + to add one. Then click on the newly created plant to add reminders and photos",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(contentPadding),
                    )
                }
                else{
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 150.dp, ),
                        modifier = modifier.padding(horizontal = 4.dp),
                        contentPadding = contentPadding,
                    ) {
                        items(items = plants) {
                            plantItem(
                                plant = it,
                                navController = navController,
                                viewModel = viewModel
                            )
                        }
                    }

                }

                if (showAddPlantDialog) {
                    viewModel.cameraForProfile = true
                    AddPlant(
                        onDismissRequest = { showAddPlantDialog = false },
                        onConfirmation = { showAddPlantDialog = false },
                        { navController.navigate(CameraStartDestination.route) },
                        viewModel2 = viewModel
                    )

                }
                if (!showAddPlantDialog) {
                    viewModel.cameraForProfile = false
                }

            }
        )


    }
}


@Composable
fun plantItem(
    plant: Plant,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: PlantMamaMainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .clickable {
                viewModel.currentPlant = plant
                navController.navigate("${PlantProfileDestination.route}/${viewModel.currentPlant.id}")
            },
        shape = RoundedCornerShape(12.dp), // Slightly rounder for modern look
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. The Background Image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(plant.profilePic.ifEmpty { R.drawable.plant_logo })
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Fills the whole card
            )

            // 2. The Gradient Overlay (Shadow at the bottom for readability)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp) // Space between the box and the card edge
                    .background(
                        // Semi-transparent background for the text box
                        color = Color.White.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .fillMaxWidth()
            ){
                Text(
                    text = plant.name,
                    modifier = Modifier
                        .align(Alignment.BottomCenter) // Positions text at bottom left
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


