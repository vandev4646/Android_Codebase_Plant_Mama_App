package com.android.example.plantmamaapp_v3.ui

import com.android.example.plantmamaapp_v3.data.Plant
import com.android.example.plantmamaapp_v3.data.plants

class PlantMamaMainScreenUiState {
    val currentPlant: Plant = plants[0]
    val currentPlantList:List<Plant> = plants
}