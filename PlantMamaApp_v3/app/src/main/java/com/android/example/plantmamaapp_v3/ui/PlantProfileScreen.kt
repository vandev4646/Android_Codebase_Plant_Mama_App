package com.android.example.plantmamaapp_v3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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

object PlantProfileDestination : NavigationDestination {
    override val route = "plant_profile"
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun PlantProfleMain(
    viewModel: PlantMamaMainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController,
) {
    var showAddReminderDialog by rememberSaveable { mutableStateOf(false) }
    var showEditPlantDialog by rememberSaveable { mutableStateOf(false) }


    val currentPlant = viewModel.currentPlant
    viewModel.newProfilePicSelected = false

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_small)),
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

                        Card(modifier = Modifier.fillMaxSize()) {
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
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
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

@Composable
fun profileTopBar(
    plant: Plant,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(plant.name + "'s Profile") },
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
        },
        actions = {
            if (!plant.profilePic.equals("")) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(plant.profilePic)
                        .placeholder(R.drawable.plant_logo)
                        .build(),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(2.dp)
                        .size(dimensionResource(R.dimen.main_image_size)),
                    contentScale = ContentScale.Fit,
                )
            } else {
                val painter = painterResource(R.drawable.plant_logo)

                val description = plant.name
                Image(
                    painter = painter,
                    contentDescription = description,
                    modifier = Modifier.size(dimensionResource(R.dimen.main_image_size))
                )

            }

        },
    )
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
            //Spacer(modifier = Modifier.weight(1f))
            //Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_medium)))
            Text(text = plant.name, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = true)

        }
        Row(modifier = Modifier) {
            Text(text = "Age", modifier = Modifier.size(96.dp, 32.dp))
            //Spacer(modifier = Modifier.weight(1f))
           // Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_medium)))
            Text(text = plant.age.toString(), fontWeight = FontWeight.Bold, maxLines = 1, softWrap = true)

        }
        //Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_medium)))
        Row(modifier = Modifier) {
            Text(text = "Type", modifier = Modifier.size(96.dp, 32.dp))
            //Spacer(modifier = Modifier.weight(1f))
            //Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_medium)))
            Text(text = plant.type, fontWeight = FontWeight.Bold,  softWrap = true,  modifier = Modifier.height(textHeight+5.dp)
                .verticalScroll(rememberScrollState())
                //.horizontalScroll(rememberScrollState())
            )

        }
        Row(modifier = Modifier) {
            Text(text = "Notes", modifier = Modifier.size(96.dp, 32.dp))
            //Spacer(modifier = Modifier.weight(1f))
           // Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_medium)))
            Text(text = plant.notes, fontWeight = FontWeight.Bold, softWrap = true, modifier = Modifier
                .verticalScroll(rememberScrollState())
               // .horizontalScroll(rememberScrollState())
            )

        }

    }
}
