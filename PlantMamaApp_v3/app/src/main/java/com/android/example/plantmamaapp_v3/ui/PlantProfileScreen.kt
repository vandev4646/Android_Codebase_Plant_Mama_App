package com.android.example.plantmamaapp_v3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.example.plantmamaapp_v3.R
import com.android.example.plantmamaapp_v3.data.Plant
import com.android.example.plantmamaapp_v3.ui.navigation.NavigationDestination
import androidx.compose.ui.tooling.preview.Preview

object PlantProfileDestination : NavigationDestination {
    override val route = "plant_profile"
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun PlantProfleMain(
    viewModel: PlantMamaMainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    var showAddReminderDialog by rememberSaveable { mutableStateOf(false) }
    var showEditPlantDialog by rememberSaveable { mutableStateOf(false) }


    val currentPlant = viewModel.currentPlant
    viewModel.newProfilePicSelected = false

    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.menu_screen),
            contentDescription = null,
            modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier
                .fillMaxSize()
                .padding(),
            topBar = {
                profileTopBar(plant = currentPlant,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = {
                        navController.navigate(PlantMamaHomeDesintation.route)
                    })
            },

            content = { padding ->

                Column {
                    Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_medium)))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.4f)
                            .padding(8.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .padding(padding)
                                .fillMaxWidth(0.9f)
                                .fillMaxHeight()
                        )
                        {

                            Card(modifier = Modifier.fillMaxSize(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )) {
                                plantDetailsDisplay(plant = currentPlant)
                            }

                        }

                        Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_small)))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(padding)
                                .fillMaxHeight()
                        )
                        {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Column {
                                    IconButton(onClick = {
                                        navController.navigate(CameraStartDestination.route)
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                                            contentDescription = "camera"
                                        )
                                    }
                                    IconButton(onClick = {
                                        showAddReminderDialog = true
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_timer_24),
                                            contentDescription = "Alarm"
                                        )
                                    }
                                    IconButton(onClick = {
                                        navController.navigate("${PlantEditDestination.route}/${currentPlant.id}")
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_edit_24),
                                            contentDescription = "edit"
                                        )
                                    }
                                }

                            }


                        }

                    }

                    var selectedIndex by remember { mutableStateOf(0) }
                    SegmentedButtons {

                        SegmentedButtonItem(
                            selected = selectedIndex == 0,
                            onClick = { selectedIndex = 0 },
                            label = { Text(text = "Photos") },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                                    contentDescription = "camera"
                                )
                            }
                        )
                        SegmentedButtonItem(
                            selected = selectedIndex == 1,
                            onClick = { selectedIndex = 1 },
                            label = { Text(text = "Reminders") },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_timer_24),
                                    contentDescription = "Alarm"
                                )
                            }
                        )
                    }
                    Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_medium)))
                    if (selectedIndex == 0) {
                        PhotoDisplay(navController = navController)
                    }

                    if (selectedIndex == 1) {
                        ReminderListScreen()

                    }

                }

                if (showAddReminderDialog) {
                    Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_small)))
                    AddReminder(
                        onDismissRequest = { showAddReminderDialog = false },
                        onConfirmation = { showAddReminderDialog = false },
                        viewModel = viewModel
                    )
                }

                if (showEditPlantDialog) {
                    AddPlant(
                        onDismissRequest = { showEditPlantDialog = false },
                        onConfirmation = { showEditPlantDialog = false },
                        { navController.navigate(CameraStartDestination.route) },
                        viewModel2 = viewModel
                    )
                }

            }
        )


    }

}

@Composable
fun profileTopBar(
    plant: Plant,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
           .padding(start = 8.dp, end = 8.dp, bottom = 4.dp, top = 32.dp)
           .fillMaxWidth(),
       shape = RoundedCornerShape(8.dp),
        //color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 4.dp
    ){
        CenterAlignedTopAppBar(
            title = { Text(plant.name + "'s Profile") },
            colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer),
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            },
            actions = {
                    Card(
                        modifier = Modifier
                            .padding( 8.dp)
                            .size(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ){
                        if (!plant.profilePic.equals("")) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(plant.profilePic)
                                    .placeholder(R.drawable.plant_logo)
                                    .build(),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                        } else {
                            val painter = painterResource(R.drawable.plant_logo)

                            val description = plant.name
                            Image(
                                painter = painter,
                                contentDescription = description,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                        }
                    }

            }
        )
        }
    }

@Composable
fun plantDetailsDisplay(plant: Plant) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val textHeight = with(density) {
        textMeasurer.measure(
            text = plant.type,
            maxLines = 3,
            style = TextStyle(),
        ).size.height.toDp()}

    Column(modifier = Modifier.padding(6.dp)) {

        Row(modifier = Modifier) {
            Text(text = "Name", modifier = Modifier.size(96.dp, 32.dp))
            Text(text = plant.name, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = true)

        }
        Row(modifier = Modifier) {
            Text(text = "Age", modifier = Modifier.size(96.dp, 32.dp))
            Text(text = plant.age.toString(), fontWeight = FontWeight.Bold, maxLines = 1, softWrap = true)

        }
        Row(modifier = Modifier) {
            Text(text = "Type", modifier = Modifier.size(96.dp, 32.dp))
            Text(text = plant.type, fontWeight = FontWeight.Bold,  softWrap = true,  modifier = Modifier.height(textHeight+5.dp)
                .verticalScroll(rememberScrollState())
            )

        }
        Row(modifier = Modifier) {
            Text(text = "Notes", modifier = Modifier.size(96.dp, 32.dp))
            Text(text = plant.notes, fontWeight = FontWeight.Bold, softWrap = true, modifier = Modifier
                .verticalScroll(rememberScrollState())
            )

        }

    }
}



@Preview(showBackground = true, name = "Plant Profile Full View")
@Composable
fun PlantProfilePreview() {
    val mockPlant = Plant(
        id = 1,
        name = "Ferny",
        type = "Boston Fern",
        age = 2,
        notes = "Needs high humidity and indirect light.",
        profilePic = "" // Empty to trigger the local painterResource
    )

    MaterialTheme {
        // We simulate the Scaffold content here
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Mocking the TopBar
            profileTopBar(
                plant = mockPlant,
                canNavigateBack = true,
                navigateUp = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(8.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth(0.85f)) {
                    Card(modifier = Modifier.fillMaxSize()) {
                        plantDetailsDisplay(plant = mockPlant)
                    }
                }

                Spacer(modifier = Modifier.size(8.dp))

                // Action Buttons Column
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column {
                        IconButton(onClick = {}) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Camera")
                        }
                        IconButton(onClick = {}) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Timer")
                        }
                    }
                }
            }
        }
    }
}

