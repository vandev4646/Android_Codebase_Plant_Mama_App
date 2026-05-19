package com.android.example.plantmamaapp_v3.ui


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



@Composable
fun PlantBottomBar(modifier: Modifier = Modifier.fillMaxSize()){
    Surface(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 4.dp, top = 32.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        //color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 4.dp
    ){

    }
}