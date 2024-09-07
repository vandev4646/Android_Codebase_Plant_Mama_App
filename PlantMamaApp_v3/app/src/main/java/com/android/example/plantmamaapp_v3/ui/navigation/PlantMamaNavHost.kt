package com.android.example.plantmamaapp_v3.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.android.example.plantmamaapp_v3.ui.AppViewModelProvider
import com.android.example.plantmamaapp_v3.ui.PlantGridScreen
import com.android.example.plantmamaapp_v3.ui.PlantMamaMainScreenViewModel
import com.android.example.plantmamaapp_v3.ui.PlantProfleMain
import com.android.example.plantmamaapp_v3.ui.PlantScreen
import com.android.example.plantmamaapp_v3.ui.SelectedPhotoScreen
import com.android.example.plantmamaapp_v3.ui.SelectedSinglePhotoScreen
import com.android.example.plantmamaapp_v3.ui.StartMainCamera
/*
@Composable
fun PlantMamaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
){
    NavHost(
        navController = navController,
        startDestination = PlantScreen.Start.name,
        modifier = modifier
        //.verticalScroll(rememberScrollState())
        //.padding(innerPadding)
    ) {
        composable(route = PlantScreen.Start.name){
            PlantGridScreen(
                //  plants = uiState.currentPlantList,
                imageOnClick = {navController.navigate(PlantScreen.AddPlant.name)},
                navController = navController,
            )
        }

        composable(route = PlantScreen.PlantProfile.name){
            PlantProfleMain(
                navController = navController
            )
        }

        composable(route = PlantScreen.StartCamera.name){

            StartMainCamera(
                navController= navController,
            )

        }

        composable(route = PlantScreen.ShowSelectedPhotos.name){

            SelectedPhotoScreen()

        }

        composable(route = PlantScreen.ShowSingleSelectedPhoto.name){

            SelectedSinglePhotoScreen()

        }
    }


}

 */

