/*
This code was largely inspired thought the android basics class. I included the copy write for that section below for credit
 */

/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.android.example.plantmamaapp_v3.ui

import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.android.example.plantmamaapp_v3.R
import com.android.example.plantmamaapp_v3.data.Plant
import com.android.example.plantmamaapp_v3.data.PlantsRepository
import com.android.example.plantmamaapp_v3.data.Reminder
import com.android.example.plantmamaapp_v3.data.ReminderRepository
import java.util.concurrent.TimeUnit

/**
* ViewModel to validate and insert items in the Room database.
*/
class ReminderEntryViewModel(private val reminderRepository: ReminderRepository) : ViewModel() {

    /**
     * Holds current plant ui state
     */
    var reminderUiState by mutableStateOf(ReminderUiState())
        private set

    /**
     * Updates the [plantUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(reminderDetails: ReminderDetails) {
        reminderUiState =
            ReminderUiState(reminderDetails = reminderDetails, isEntryValid = validateInput(reminderDetails))
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveItem() {
        if (validateInput()) {
            reminderRepository.insertReminder(reminderUiState.reminderDetails.toItem())
        }
    }

    private fun validateInput(uiState: ReminderDetails = reminderUiState.reminderDetails): Boolean {
        return with(uiState) {
            title.isNotBlank()
            date.isNotBlank()
            time.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class ReminderUiState(
    val reminderDetails: ReminderDetails = ReminderDetails(),
    val isEntryValid: Boolean = false
)

data class ReminderDetails(
    val wmIdentifier: String = "",
    val plantID: String = "",
    val title: String = "",
    val date: String = "",
    val time: String = "",
)

/**
 * Extension function to convert [PlantUiState] to [Plant]. If the value of [ItemDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun ReminderDetails.toItem(): Reminder = Reminder(
    wmIdentifier = wmIdentifier,
    plantID = plantID.toIntOrNull()?:0,
    title = title,
    date = date,
    time = time,
)


/**
 * Extension function to convert [Plant] to [PlantUiState]
 */
fun Reminder.toReminderUiState(isEntryValid: Boolean = false): ReminderUiState = ReminderUiState(
    reminderDetails = this.toReminderDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Plant] to [PlantDetails]
 */
fun Reminder.toReminderDetails(): ReminderDetails = ReminderDetails(
    wmIdentifier = wmIdentifier,
    plantID = plantID.toString(),
    title = title,
    date = date,
    time = time
)