package com.android.example.plantmamaapp_v3.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.android.example.plantmamaapp_v3.PlantMamaApplication

object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val waterRepository =
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PlantMamaApplication).container.waterRepository
            PlantMamaMainScreenViewModel(
                waterRepository = waterRepository,
                plantsRepository = plantMamaApplication().container.plantsRepository
            )

        }

        // Initializer for PlantEntryViewModel
        initializer {
            PlantEntryViewModel(plantMamaApplication().container.plantsRepository)
        }

        // Initializer for ReminderEntryViewModel
        initializer {
            ReminderEntryViewModel(plantMamaApplication().container.reminderRepository)
        }

        initializer {
            ReminderListViewModel(this.createSavedStateHandle(), plantMamaApplication().container.reminderRepository)
        }

        initializer {
            PhotoViewModel(this.createSavedStateHandle(),plantMamaApplication().container.photosRepository)
        }

        initializer {
            PhotoDisplayViewModel(this.createSavedStateHandle(),plantMamaApplication().container.photosRepository)
        }
    }
}




/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.plantMamaApplication(): PlantMamaApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PlantMamaApplication)