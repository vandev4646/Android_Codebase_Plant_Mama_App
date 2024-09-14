package com.android.example.plantmamaapp_v3.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.plantmamaapp_v3.data.PlantsRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


/**
 * ViewModel to retrieve and update an item from the [ItemsRepository]'s data source.
 */
class PlantEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val plantRepository: PlantsRepository
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var plantUiState by mutableStateOf(PlantUiState())
        private set

    private val plantId: Int = checkNotNull(savedStateHandle[PlantEditDestination.itemIdArg])

    init {
        viewModelScope.launch {
            val plant = plantRepository.getPlantStream(plantId)
                .filterNotNull()
                .first()
            plantUiState = plant.toPlantUiState(true)
            println("Loaded plant: $plant")
            /*
            plantUiState = plantRepository.getPlantStream(plantId)
                .filterNotNull()
                .first()
                .toPlantUiState(true)

             */
        }
    }

    /**
     * Update the item in the [ItemsRepository]'s data source
     */
    suspend fun updatePlant() {
        if (validateInput(plantUiState.plantDetails)) {
            plantRepository.updatePlant(plantUiState.plantDetails.toItem())
        }
    }

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(plantDetails: PlantDetails) {
        plantUiState =
            PlantUiState(plantDetails = plantDetails, isEntryValid = validateInput(plantDetails))
    }

    private fun validateInput(uiState: PlantDetails = plantUiState.plantDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}
