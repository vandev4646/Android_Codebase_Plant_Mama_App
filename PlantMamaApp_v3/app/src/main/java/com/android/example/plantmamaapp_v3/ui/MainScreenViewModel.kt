package com.android.example.plantmamaapp_v3.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.plantmamaapp_v3.data.Plant
import com.android.example.plantmamaapp_v3.data.PlantsRepository
import com.android.example.plantmamaapp_v3.data.ReminderWM
import com.android.example.plantmamaapp_v3.data.WaterRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PlantMamaMainScreenViewModel(
    private val waterRepository: WaterRepository,
    val plantsRepository: PlantsRepository
) : ViewModel() {

    lateinit var currentPlant: Plant
    var currentUri: Uri = Uri.EMPTY

    var deletePhotoPath = ""
    var deletePhotoId = 0

    //for selected images
    var selectedImagesUris: List<Uri> = mutableListOf()

    var cameraForProfile = false

    var newProfilePicSelected = false


    //Information from the Water Me app for the reminder schedule
    //WAS CAUSING CRASHES SO I COMMEMENTED IT OUT. WILL NEED TO MAKE SURE THIS ISN'T NEEDED IN THE FUTURE
    //internal val waterPlants = waterRepository.plants

    fun scheduleReminder(reminderWM: ReminderWM) {
        waterRepository.scheduleReminder(
            reminderWM.duration,
            reminderWM.unit,
            reminderWM.plantName,
            reminderWM.reminderTitle,
            reminderWM.reminderIdentifier,
            reminderWM.recurrence
        )
    }


    val homeUiState: StateFlow<HomeUiState> =
        plantsRepository.getAllPlantsStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )


    /**
     * Factory for [WaterViewModel] that takes [WaterRepository] as a dependency
     */
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(val itemList: List<Plant> = listOf())