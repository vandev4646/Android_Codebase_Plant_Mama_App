package com.android.example.plantmamaapp_v3.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.android.example.plantmamaapp_v3.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object InspectPhotoScreenDestination : NavigationDestination {
    override val route = "inspect_photo_screen"
}

@Composable
fun InspectPhotoScreen(viewModel: PlantMamaMainScreenViewModel,
                       navController: NavController,
                       )
{
    val uri = viewModel.deletePhotoPath
    Surface(modifier = Modifier.fillMaxSize()) {

        ImageDisplay2(uri = uri)


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {

                navController.popBackStack()

            }) {
                Text(text = "Keep Photo")
            }
            Button(onClick = {
                navController.popBackStack()

            }) {
                Text(text = "Delete Photo")
            }
        }

    }
}

@Composable
fun ImageDisplay2(uri: String){
    Image(
        painter = rememberImagePainter(
            data  = Uri.parse(uri)  // or ht
        )
        ,
        contentDescription = "123",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillWidth
    )

}