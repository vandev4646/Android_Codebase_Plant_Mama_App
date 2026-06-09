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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.android.example.plantmamaapp_v3.R
import com.android.example.plantmamaapp_v3.data.Plant
import com.android.example.plantmamaapp_v3.ui.navigation.NavigationDestination
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.Date

object PlantProfileDestination : NavigationDestination {
    override val route = "plant_profile"
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun PlantProfleMain(
    viewModel: MainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    photoDisplayViewModel: PhotoDisplayViewModel = viewModel(factory = AppViewModelProvider.Factory),
    profileViewModel: PlantProfileViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    var showAddReminderDialog by rememberSaveable { mutableStateOf(false) }
    var showEditPlantDialog by rememberSaveable { mutableStateOf(false) }


    //val currentPlant = viewModel.currentPlant
    viewModel.newProfilePicSelected = false

    val profileUiState by profileViewModel.profileUiState.collectAsStateWithLifecycle()
    val currentPlant = profileUiState.plant

    val photoListUiState by photoDisplayViewModel.photoListUiState.collectAsState()
    val photoList = photoListUiState.photoList

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
                ProfileTopBar(plant = currentPlant,
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
                                PlantDetailsDisplay(plant = currentPlant)
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
                        PhotoDisplay(photoList, "Oops! No photos are added yet for this plant. Tap the camera icon above to add a photo", navController = navController)
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
                        currentPlant = currentPlant
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
fun PlantDetailsDisplay(plant: Plant) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val textHeight = with(density) {
        textMeasurer.measure(
            text = plant.type,
            maxLines = 3,
            style = TextStyle(),
        ).size.height.toDp()}

    val formattedAge = remember(plant.datePurchased){
        calculatePlantAgeText(plant.datePurchased)
    }

    Column(modifier = Modifier.padding(6.dp)) {

        Row(modifier = Modifier) {
            Text(text = "Name", modifier = Modifier.size(96.dp, 32.dp))
            Text(text = plant.name, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = true)

        }
        Row(modifier = Modifier) {
            Text(text = "Age", modifier = Modifier.size(96.dp, 32.dp))
            Text(text = formattedAge, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = true)

        }
        Row(modifier = Modifier) {
            Text(text = "Type", modifier = Modifier.size(96.dp, 32.dp))
            Text(text = plant.type, fontWeight = FontWeight.Bold,  softWrap = true,  modifier = Modifier.height(textHeight+5.dp)
                .verticalScroll(rememberScrollState())
            )

        }
        Row(modifier = Modifier) {
            Text(text = "Notes", modifier = Modifier.size(96.dp, 32.dp))
            Text(text = plant.description, fontWeight = FontWeight.Bold, softWrap = true, modifier = Modifier
                .verticalScroll(rememberScrollState())
            )

        }

    }
}

fun calculatePlantAgeText(purchasedDate: Date): String{
    //get the local data from the purchase date
    val purchasedLocalDate = purchasedDate.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    val today = LocalDate.now()

    //time between date calcuation
    val period = Period.between(purchasedLocalDate, today)
    val totalMonths = (period.years * 12) + period.months

    return when{
        totalMonths <= 0 -> "Less than a month"
        totalMonths <= 12 -> "$totalMonths months"
        else -> {
            val years = period.years
            val remainingMonths = period.months

            if(remainingMonths > 0) {
                "$years years and $remainingMonths months"
            } else {
                "$years years"
            }
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
        datePurchased = Date(System.currentTimeMillis()),
        description = "Needs high humidity and indirect light.",
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
            ProfileTopBar(
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
                        PlantDetailsDisplay(plant = mockPlant)
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

