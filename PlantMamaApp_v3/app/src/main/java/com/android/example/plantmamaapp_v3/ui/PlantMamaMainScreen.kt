package com.android.example.plantmamaapp_v3.ui


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun PlantMamaApp(
    viewModel: PlantMamaMainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController = rememberNavController()
) {

    val homeUiState by viewModel.homeUiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = PlantMamaHomeDesintation.route,
        modifier = Modifier
            .fillMaxSize()
    ) {
        composable(route = PlantMamaHomeDesintation.route) {
            PlantGridScreen(
                plants = homeUiState.itemList,
                navController = navController,
                viewModel = viewModel,
            )
        }

        composable(
            route = PlantProfileDestination.routeWithArgs,
            arguments = listOf(navArgument(PlantProfileDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            PlantProfleMain(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(route = CameraStartDestination.route) {

            StartMainCamera(
                navController = navController,
                viewModelPlantMamaMainScreen = viewModel
            )

        }

        composable(route = SelectedPhotoScreenDestination.route) {

            SelectedPhotoScreen(
                viewModel = viewModel,
                onCancel = { navController.navigate("${PlantProfileDestination.route}/${viewModel.currentPlant.id}") },
                plantId = viewModel.currentPlant.id,
                plantName = viewModel.currentPlant.name
            )

        }

        composable(route = SelectedSinglePhotoScreenDestination.route) {

            SelectedSinglePhotoScreen(
                uri = viewModel.currentUri,
                onCancel = { navController.navigate("${PlantProfileDestination.route}/${viewModel.currentPlant.id}") },
                plantId = viewModel.currentPlant.id,
                plantName = viewModel.currentPlant.name
            )

        }

        composable(route = SelectProfilePicDestination.route) {

            SelectProfilePicScreen(
                viewModelPlantMamaMainScreen = viewModel,
                navController = navController
            )

        }

        composable(route = InspectPhotoScreenDestination.routeWithArgs,arguments = listOf(navArgument(InspectPhotoScreenDestination.itemIdArg) {
            type = NavType.IntType
        })) {

            InspectPhotoScreen(viewModel = viewModel, navController = navController)

        }

        composable(
            route = PlantEditDestination.routeWithArgs,
            arguments = listOf(navArgument(PlantEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            PlantEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateHome = {
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.navigate(PlantMamaHomeDesintation.route)
                },
                onProfilePicClick = {
                    viewModel.cameraForProfile = true
                    viewModel.newProfilePicSelected = true
                    navController.navigate(CameraStartDestination.route)
                },
                viewModel2 = viewModel
            )
        }
    }
}

