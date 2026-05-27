package com.android.example.plantmamaapp_v3.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.plantmamaapp_v3.data.Reminder
import com.android.example.plantmamaapp_v3.data.ReminderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all items in the Room database.
 */
class AllReminderViewModel(
    savedStateHandle: SavedStateHandle,
    reminderRepository: ReminderRepository
) : ViewModel() {
    val reminderRepository = reminderRepository

    /**
     * Holds reminder list ui state. The list of items are retrieved from [ReminderRepository] and mapped to
     * [ReminderListUiState]
     */
    val allReminderUiState: StateFlow<AllReminderUiState> =
        reminderRepository.getAllRemindersStream().map { AllReminderUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AllReminderUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class AllReminderUiState(val reminderList: List<Reminder> = listOf())