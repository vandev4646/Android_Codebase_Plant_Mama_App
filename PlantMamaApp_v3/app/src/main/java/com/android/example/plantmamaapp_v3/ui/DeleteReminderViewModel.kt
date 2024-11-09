package com.android.example.plantmamaapp_v3.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.plantmamaapp_v3.data.Photo
import com.android.example.plantmamaapp_v3.data.PhotosRepository
import com.android.example.plantmamaapp_v3.data.Reminder
import com.android.example.plantmamaapp_v3.data.ReminderRepository
import com.android.example.plantmamaapp_v3.data.WaterRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DeleteReminderViewModel(
    savedStateHandle: SavedStateHandle,
    reminderRepository: ReminderRepository,
    waterRepository: WaterRepository
) : ViewModel() {

    val reminderRepository = reminderRepository
    val waterRepository = waterRepository

    private val photoId: Int = checkNotNull(savedStateHandle[InspectPhotoScreenDestination.itemIdArg])

    //for the Photo flow

    /**
     * Holds reminder list ui state. The list of items are retrieved from [ReminderRepository] and mapped to
     * [ReminderListUiState]
     */



    suspend fun deleteReminder(reminder: Reminder){
        viewModelScope.launch{
            reminderRepository.deleteReminder(reminder)
        }
    }
}