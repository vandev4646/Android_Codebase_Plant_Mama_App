package com.android.example.plantmamaapp_v3.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.plantmamaapp_v3.data.PhotosRepository
import com.android.example.plantmamaapp_v3.data.Plant
import com.android.example.plantmamaapp_v3.data.PlantsRepository
import com.android.example.plantmamaapp_v3.data.ReminderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * ViewModel to retrieve and update an item from the [ItemsRepository]'s data source.
 */
class PlantEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val plantRepository: PlantsRepository,
    private val reminderRepository: ReminderRepository,
    private val photosRepository: PhotosRepository,
) : ViewModel() {

    private val plantId: Int = checkNotNull(savedStateHandle[PlantEditDestination.itemIdArg])

    /**
     * Holds current item ui state
     */
    var plantUiState by mutableStateOf(PlantUiState(PlantDetails(), true))
        private set

    private val _profilePicChangeTrigger = mutableStateOf(false)

    init {
        viewModelScope.launch {
            val plant = plantRepository.getPlantStream(plantId)
                .filterNotNull()
                .first()
            plantUiState = plant.toPlantUiState(true)

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

    suspend fun deletePlant(){
        viewModelScope.launch {

            val reminderList =
                reminderRepository.getAllRemindersNonStream(plantId)

            reminderList.forEach{
                reminderRepository.deleteReminder(it)
            }

            val photoList =
                photosRepository.getAllPhotoNonStreamByPlantId(plantId)

            photoList.forEach{
                photosRepository.deletePhoto(it)
            }

            plantRepository.deletePlant(plantUiState.plantDetails.toItem())

        }

    }
}
