package com.android.example.plantmamaapp_v3.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.android.example.plantmamaapp_v3.PlantMamaApplication
import com.android.example.plantmamaapp_v3.data.Plant
import com.android.example.plantmamaapp_v3.data.Reminder
import com.android.example.plantmamaapp_v3.data.ReminderWM
import com.android.example.plantmamaapp_v3.data.WaterRepository
import com.android.example.plantmamaapp_v3.data.plants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlantMamaMainScreenViewModel(private val waterRepository: WaterRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PlantMamaMainScreenUiState())
    val uiState: StateFlow<PlantMamaMainScreenUiState> = _uiState.asStateFlow()
    lateinit var currentPlant: Plant
    private var plantsList: MutableList<Plant> = mutableListOf()
    lateinit var currentUri: Uri

    //for selected images
     var selectedImagesUris: List<Uri> = mutableListOf()

    fun refresh(){
        _uiState.value = PlantMamaMainScreenUiState()
        currentPlant = plants[0]
        plantsList = plants.toMutableList()
    }

    init {
        refresh()
    }

    //Information from the Water Me app for the reminder schedule
    //WAS CAUSING CRASHES SO I COMMEMENTED IT OUT. WILL NEED TO MAKE SURE THIS ISN'T NEEDED IN THE FUTURE
    //internal val waterPlants = waterRepository.plants

    fun scheduleReminder(reminderWM: ReminderWM) {
        waterRepository.scheduleReminder(reminderWM.duration, reminderWM.unit, reminderWM.plantName, reminderWM.reminderIdentifier)
    }



    /**
     * Factory for [WaterViewModel] that takes [WaterRepository] as a dependency
     */
    companion object {

    }
}