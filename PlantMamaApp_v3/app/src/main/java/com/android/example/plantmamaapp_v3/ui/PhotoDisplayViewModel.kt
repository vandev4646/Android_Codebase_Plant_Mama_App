package com.android.example.plantmamaapp_v3.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.plantmamaapp_v3.data.Photo
import com.android.example.plantmamaapp_v3.data.PhotosRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PhotoDisplayViewModel (savedStateHandle: SavedStateHandle, photosRepository: PhotosRepository): ViewModel(){

    val photosRepository = photosRepository

    private val plantID: Int = checkNotNull(savedStateHandle[PlantProfileDestination.itemIdArg])

    val photoListUiState: StateFlow<PhotoListUiState> =
        photosRepository.getAllPhotoStreamByPlantId(plantID).map { PhotoListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PhotoListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val photoLists = photosRepository.getAllPhotoStreamByPlantId(plantID)
}

/**
 * Ui State for HomeScreen
 */
data class PhotoListUiState(val photoList: List<Photo> = listOf())