package com.android.example.plantmamaapp_v3.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.android.example.plantmamaapp_v3.PlantMamaApplication
import com.android.example.plantmamaapp_v3.ui.auth.AuthViewModel
import com.android.example.plantmamaapp_v3.ui.note.NoteEditViewModel
import com.android.example.plantmamaapp_v3.ui.note.NoteItemViewModel
import com.android.example.plantmamaapp_v3.ui.note.NoteViewModel
import com.android.example.plantmamaapp_v3.ui.photo.AllPhotosViewModel
import com.android.example.plantmamaapp_v3.ui.photo.InspectPhotoViewModel
import com.android.example.plantmamaapp_v3.ui.photo.PhotoDisplayViewModel
import com.android.example.plantmamaapp_v3.ui.photo.PhotoViewModel
import com.android.example.plantmamaapp_v3.ui.reminder.AllReminderViewModel
import com.android.example.plantmamaapp_v3.ui.reminder.DeleteReminderViewModel
import com.android.example.plantmamaapp_v3.ui.reminder.ReminderEntryViewModel
import com.android.example.plantmamaapp_v3.ui.reminder.ReminderListViewModel

object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            MainScreenViewModel(
                plantsRepository = plantMamaApplication().container.plantsRepository
            )
        }

        // Initializer for PlantEntryViewModel
        initializer {
            PlantEntryViewModel(plantMamaApplication().container.plantsRepository)
        }

        // Initializer for ReminderEntryViewModel
        initializer {
            val waterRepository =
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PlantMamaApplication).container.waterRepository
            ReminderEntryViewModel(
                plantMamaApplication().container.reminderRepository,
                waterRepository = waterRepository,
            )
        }

        initializer {
            ReminderListViewModel(
                this.createSavedStateHandle(),
                plantMamaApplication().container.reminderRepository
            )
        }

        initializer {
            AllReminderViewModel(
                this.createSavedStateHandle(),
                plantMamaApplication().container.reminderRepository
            )
        }

        initializer {
            PhotoViewModel(
                this.createSavedStateHandle(),
                plantMamaApplication().container.photosRepository
            )
        }

        initializer {
            PhotoDisplayViewModel(
                this.createSavedStateHandle(),
                plantMamaApplication().container.photosRepository
            )
        }

        initializer {
            AllPhotosViewModel(
                this.createSavedStateHandle(),
                plantMamaApplication().container.photosRepository
            )
        }

        initializer {
            InspectPhotoViewModel(
                this.createSavedStateHandle(),
                plantMamaApplication().container.photosRepository
            )
        }
        initializer {
            PlantProfileViewModel(
                plantMamaApplication().container.plantsRepository,
                this.createSavedStateHandle(),
                noteRepository = plantMamaApplication().container.notesRepository,
            )
        }

        initializer {
            PlantEditViewModel(
                this.createSavedStateHandle(),
                plantMamaApplication().container.plantsRepository,
                plantMamaApplication().container.reminderRepository,
                plantMamaApplication().container.photosRepository
            )
        }

        initializer {
            DeleteReminderViewModel(
                this.createSavedStateHandle(),
                plantMamaApplication().container.reminderRepository,
                plantMamaApplication().container.waterRepository
            )
        }

        initializer {
            NoteViewModel(
                plantMamaApplication().container.notesRepository
            )
        }

        initializer {
            NoteItemViewModel(
                this.createSavedStateHandle(),
                plantMamaApplication().container.notesRepository
            )
        }

        initializer {
            NoteEditViewModel(
                this.createSavedStateHandle(),
                plantMamaApplication().container.notesRepository,
                plantMamaApplication().container.photosRepository
            )
        }

        initializer {
            AuthViewModel()
        }
    }
}


/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.plantMamaApplication(): PlantMamaApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PlantMamaApplication)