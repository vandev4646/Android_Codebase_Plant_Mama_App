package com.android.example.plantmamaapp_v3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.example.plantmamaapp_v3.R
import com.android.example.plantmamaapp_v3.data.Plant
import com.android.example.plantmamaapp_v3.ui.navigation.NavigationDestination
import com.android.example.plantmamaapp_v3.ui.theme.PLantMamaTheme

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

    var showAddPlantDialog by rememberSaveable { mutableStateOf(false) }
    Scaffold(

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
                    columns = GridCells.Adaptive(minSize = 125.dp, ),
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

/**
 * Composable that displays a list item containing a dog icon and their information.
 *
 * @param plant contains the data that populates the list item
 * @param modifier modifiers to set to this composable
 */
@Composable
fun plantItem(
    plant: Plant,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: PlantMamaMainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    Card(
        modifier = modifier.padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = {}
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clickable(onClick = {
                    viewModel.currentPlant = plant
                    navController.navigate("${PlantProfileDestination.route}/${viewModel.currentPlant.id}")
                })
        ) {

            if (!plant.profilePic.equals("")) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(plant.profilePic)
                        .placeholder(R.drawable.plant_logo)
                        .build(),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize(0.9f)
                        .aspectRatio(1F),
                    contentScale = ContentScale.FillWidth,
                )
                Text(plant.name)
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(R.drawable.plant_logo)
                        .placeholder(R.drawable.plant_logo)
                        .build(),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize(0.9f)
                        .aspectRatio(1F),
                    contentScale = ContentScale.FillWidth,
                )
                Text(plant.name, maxLines = 1, overflow = TextOverflow.Ellipsis)

            }
        }
    }
}

/**
 * Composable that displays a Top Bar with an icon and text.
 *
 * @param modifier modifiers to set to this composable
 */
@Composable
fun PlantTopAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.main_image_size)),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(R.drawable.plant_logo),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_medium)))
                Text(
                    text = "Plant Mama",
                    style = MaterialTheme.typography.displaySmall

                )
            }
        },

        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.nav_add_reminder)
                    )
                }
            }
        }
    )

}


@Composable
fun ImageCard(
    painter: Painter,
    contentDescription: String,
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Box(modifier = Modifier.height(100.dp)) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White
                            ),
                            startY = 100f
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

    }

}


