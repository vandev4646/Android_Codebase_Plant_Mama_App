package com.android.example.plantmamaapp_v3.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.example.plantmamaapp_v3.R

@Composable
fun PlantMamaApp(
    viewModel: MainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController = rememberNavController()
) {

    val homeUiState by viewModel.homeUiState.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val routesWithBottomBar = listOf(
        PlantMamaHomeDesintation.route,
        AllRemindersDesintation.route,
        AllPhotosDesintation.route
    )
    var showAddPlantDialog by rememberSaveable { mutableStateOf(false) }
    var showInfoDialog by rememberSaveable { mutableStateOf(false)}
    val isCameraActive = currentRoute?.startsWith(CameraStartDestination.route) == true

    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.menu_screen),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
            topBar = {
                if(currentRoute == PlantMamaHomeDesintation.route){
                    PlantTopAppBar(
                        navController = navController,
                        canNavigateBack = false,
                        //canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() },
                        onAddPlantClick = { showAddPlantDialog = true},
                        onInfoClick = { showInfoDialog = true }
                    )
                }
            },
            bottomBar = {
                if(currentRoute in routesWithBottomBar){
                    PlantBottomBar(
                        navController = navController,
                        onAddPlantClick = { showAddPlantDialog = true},
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            },
             ){ contentPadding ->

                NavHost(
                    navController = navController,
                    startDestination = PlantMamaHomeDesintation.route,
                    modifier = Modifier.padding(contentPadding)
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
                            navController = navController
                        )
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
                                //viewModel.cameraForProfile = true
                                viewModel.newProfilePicSelected = true
                                navController.navigate("${CameraStartDestination.route}/true")
                            },
                            viewModel2 = viewModel
                        )

                    }

                    composable(
                        route = CameraStartDestination.routeWithArgs,
                        arguments = listOf(navArgument(CameraStartDestination.isProfileArg) { type = NavType.BoolType })
                    ) { backStackEntry ->
                        val isProfileCamera = backStackEntry.arguments?.getBoolean(CameraStartDestination.isProfileArg) ?: false
                        StartMainCamera(
                            navController = navController,
                            viewModelMainScreen = viewModel,
                            cameraForProfile = isProfileCamera
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

                    composable(
                        route = InspectPhotoScreenDestination.routeWithArgs,
                        arguments = listOf(navArgument(InspectPhotoScreenDestination.itemIdArg) {
                            type = NavType.IntType
                        })
                    ) {
                        InspectPhotoScreen(viewModel = viewModel, navController = navController)
                    }

                    composable(AllRemindersDesintation.route) {
                        AllReminders(navController = navController)
                    }

                    composable(AllPhotosDesintation.route) {
                        AllPhotos(navController = navController)
                    }
                }
            }

        if (showAddPlantDialog && !isCameraActive) {
            AddPlant(
                onDismissRequest = { showAddPlantDialog = false },
                onConfirmation = { showAddPlantDialog = false },
                {
                    navController.navigate(("${CameraStartDestination.route}/true"))
                },
                viewModel2 = viewModel
            )

        }


        if(showInfoDialog){
            InfoDialog({showInfoDialog = false})
        }
}}

