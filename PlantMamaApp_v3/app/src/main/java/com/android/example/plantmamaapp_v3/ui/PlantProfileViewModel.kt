package com.android.example.plantmamaapp_v3.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.plantmamaapp_v3.data.Plant
import com.android.example.plantmamaapp_v3.data.PlantsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ProfileUiState(val plant: Plant = Plant(
    id = 0,
    profilePic = "",
    name = "Loading...",
    age = 0,
    type = "Loading...",
    notes = "Loading..."
))

class PlantProfileViewModel (
    private val repository: PlantsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(){
    val plantId: Int = checkNotNull(savedStateHandle[PlantProfileDestination.itemIdArg])

    val profileUiState: StateFlow<ProfileUiState> = repository.getPlantStream(plantId)
        .map{
            ProfileUiState(plant = it ?: Plant(
                id = 0,
                profilePic = "",
                name = "Loading...",
                age = 0,
                type = "Loading...",
                notes = "Loading..."
            ))}
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileUiState()
        )
}