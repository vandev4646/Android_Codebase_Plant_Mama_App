package com.android.example.plantmamaapp_v3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.example.plantmamaapp_v3.R
import com.android.example.plantmamaapp_v3.data.Plant

@Composable
fun ProfileTopBar(
    plant: Plant,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 4.dp, top = 8.dp)
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