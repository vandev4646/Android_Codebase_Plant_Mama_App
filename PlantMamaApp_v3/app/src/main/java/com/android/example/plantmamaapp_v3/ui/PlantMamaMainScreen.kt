package com.android.example.plantmamaapp_v3.ui


import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
//import com.android.example.plantmamaapp_v3.data.plants


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

    //val currentPlantId = remember {viewModel.currentPlant.id}
    // Get current back stack entry
//    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
//    val currentScreen = PlantScreen.valueOf(
 //       backStackEntry?.destination?.route ?: PlantScreen.Start.name
 //   )
  //  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
   // Scaffold(
  //      modifier = Modifier
  //          .nestedScroll(scrollBehavior.nestedScrollConnection)
  //          .fillMaxSize(),
        /*
        topBar = {
            PlantTopAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }

         */

   // ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        val homeUiState by viewModel.homeUiState.collectAsState()



        NavHost(
            navController = navController,
            startDestination = PlantMamaHomeDesintation.route,
            modifier = Modifier
                .fillMaxSize()
                //.verticalScroll(rememberScrollState())
              //  .padding(innerPadding)
        ) {
            composable(route = PlantMamaHomeDesintation.route){
                PlantGridScreen(
                    plants = homeUiState.itemList,
                    imageOnClick = {navController.navigate(PlantProfileDestination.routeWithArgs)},
                    navController = navController,
                    viewModel = viewModel,
                    )
            }

            composable(route = PlantProfileDestination.routeWithArgs,
                arguments = listOf(navArgument(PlantProfileDestination.itemIdArg) {
                    type = NavType.IntType
                })
            ){
                PlantProfleMain(
                    viewModel = viewModel,
                    navController = navController
                )
            }

            composable(route = CameraStartDestination.route){

                StartMainCamera(
                    navController= navController,
                    viewModelPlantMamaMainScreen = viewModel
                )

            }

            composable(route = SelectedPhotoScreenDestination.route){

                SelectedPhotoScreen(
                    viewModel = viewModel,
                    onCancel = {navController.navigate("${PlantProfileDestination.route}/${viewModel.currentPlant.id}")},
                    plantId = viewModel.currentPlant.id
                )

            }

            composable(route = SelectedSinglePhotoScreenDestination.route){

                SelectedSinglePhotoScreen(
                    uri = viewModel.currentUri,
                    onCancel = {navController.navigate("${PlantProfileDestination.route}/${viewModel.currentPlant.id}")},
                    plantId = viewModel.currentPlant.id
                )

            }

            composable(route = SelectProfilePicDestination.route){

                SelectProfilePicScreen(
                    viewModelPlantMamaMainScreen = viewModel,
                    navController = navController
                )

            }

            composable(route = InspectPhotoScreenDestination.route){

                InspectPhotoScreen(viewModel = viewModel, navController = navController)

            }
        }
        /*
        Surface(color = MaterialTheme.colorScheme.background) {

            PlantGridScreen(plants = plantMamaMainScreenUiState.currentPlantList, contentPadding = it)

        }
        
         */
            
    //}
}

