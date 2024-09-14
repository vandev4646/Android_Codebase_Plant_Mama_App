package com.android.example.plantmamaapp_v3.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.plantmamaapp_v3.data.Photo
import com.android.example.plantmamaapp_v3.data.PhotosRepository
import com.android.example.plantmamaapp_v3.data.Reminder
import com.android.example.plantmamaapp_v3.data.ReminderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DeleteReminderViewModel(
    savedStateHandle: SavedStateHandle,
    reminderRepository: ReminderRepository
) : ViewModel() {

    val reminderRepository = reminderRepository

    private val photoId: Int = checkNotNull(savedStateHandle[InspectPhotoScreenDestination.itemIdArg])

    //for the Photo flow

    /**
     * Holds reminder list ui state. The list of items are retrieved from [ReminderRepository] and mapped to
     * [ReminderListUiState]
     */
    val reminderDeleteUiState: StateFlow<ReminderDeleteViewUiState> =
        reminderRepository.getReminderStream(photoId).map { reminder ->
            ReminderDeleteViewUiState(reminder ?: Reminder(-1, "", 1, "", "", ""))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ReminderDeleteViewUiState()
        )

    /*
    val photoViewUiState: StateFlow<PhotoViewUiState> =
        photosRepository.getPhotoStream(photoId).map { PhotoViewUiState(it!!) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PhotoViewUiState()
            )

     */

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun deleteReminder(reminder: Reminder){
        viewModelScope.launch{
            reminderRepository.deleteReminder(reminder)
        }

    }
}

data class ReminderDeleteViewUiState(val reminder: Reminder = Reminder(-1, "", 1, "", "", ""))