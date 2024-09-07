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

/**
* ViewModel to validate and insert items in the Room database.
*/
class PlantEntryViewModel(private val plantsRepository: PlantsRepository) : ViewModel() {

    /**
     * Holds current plant ui state
     */
    var plantUiState by mutableStateOf(PlantUiState())
        private set

    /**
     * Updates the [plantUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(plantDetails: PlantDetails) {
        plantUiState =
            PlantUiState(plantDetails = plantDetails, isEntryValid = validateInput(plantDetails))
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveItem() {
        if (validateInput()) {
            plantsRepository.insertPlant(plantUiState.plantDetails.toItem())
        }
    }

    private fun validateInput(uiState: PlantDetails = plantUiState.plantDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class PlantUiState(
    val plantDetails: PlantDetails = PlantDetails(),
    val isEntryValid: Boolean = false
)

data class PlantDetails(
    //@DrawableRes val imageResourceId: Int = R.drawable.plant_logo,
    val profilePic: String = "",
    val name: String = "",
    val age: String = "",
    val type: String = "",
    val description: String = "",
)

/**
 * Extension function to convert [PlantUiState] to [Plant]. If the value of [ItemDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun PlantDetails.toItem(): Plant = Plant(
    profilePic = profilePic,
    name = name,
    age = age.toIntOrNull()?:0,
    type = type,
    description = description
)


/**
 * Extension function to convert [Plant] to [PlantUiState]
 */
fun Plant.toPlantUiState(isEntryValid: Boolean = false): PlantUiState = PlantUiState(
    plantDetails = this.toPlantDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Plant] to [PlantDetails]
 */
fun Plant.toPlantDetails(): PlantDetails = PlantDetails(
    profilePic = profilePic,
    name = name,
    age = age.toString(),
    type = type,
    description = description
)