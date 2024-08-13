package com.android.example.plantmamaapp_v3.ui


import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.android.example.plantmamaapp_v3.PlantMamaApplication
import com.android.example.plantmamaapp_v3.R



/**
 * enum values that represent the screens in the app
 */
enum class PlantScreen(@StringRes val title: Int) {
    Start(title = R.string.nav_main),
    PlantProfile(title = R.string.nav_plant_profile),
    AddPlant(title = R.string.nav_add_plant),
    AddReminder(title = R.string.nav_add_reminder),
    StartCamera(title = R.string.nav_start_camera),
    ShowSelectedPhotos(title = R.string.nav_show_selected_photos),
    ShowSingleSelectedPhoto(title = R.string.nav_show_single_selected_photos)
}



@Composable
fun PlantMamaApp(
    viewModel: PlantMamaMainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController = rememberNavController()
){
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = PlantScreen.valueOf(
        backStackEntry?.destination?.route ?: PlantScreen.Start.name
    )
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        /*
        topBar = {
            PlantTopAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }

         */

    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = PlantScreen.Start.name,
            modifier = Modifier
                .fillMaxSize()
                //.verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(route = PlantScreen.Start.name){
                PlantGridScreen(
                    plants = uiState.currentPlantList,
                    imageOnClick = {navController.navigate(PlantScreen.AddPlant.name)},
                    navController = navController,
                    viewModel = viewModel,
                    )
            }

            composable(route = PlantScreen.PlantProfile.name){
                PlantProfleMain(
                    viewModel = viewModel,
                    navController = navController
                )
            }

            composable(route = PlantScreen.StartCamera.name){

                StartMainCamera(
                    navController= navController,
                    viewModelPlantMamaMainScreen = viewModel
                )

            }

            composable(route = PlantScreen.ShowSelectedPhotos.name){

                SelectedPhotoScreen(viewModel = viewModel)

            }

            composable(route = PlantScreen.ShowSingleSelectedPhoto.name){

                SelectedSinglePhotoScreen(uri = viewModel.currentUri)

            }
        }
        /*
        Surface(color = MaterialTheme.colorScheme.background) {

            PlantGridScreen(plants = plantMamaMainScreenUiState.currentPlantList, contentPadding = it)

        }
        
         */
            
    }
}

