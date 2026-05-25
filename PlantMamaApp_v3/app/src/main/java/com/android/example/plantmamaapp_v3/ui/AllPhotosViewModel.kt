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

class AllPhotosViewModel(
    savedStateHandle: SavedStateHandle,
    photosRepository: PhotosRepository
) : ViewModel() {

    val allPhotosUiState: StateFlow<AllPhotosUiState> =
        photosRepository.getAllPhotosStream().map { AllPhotosUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AllPhotosUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class AllPhotosUiState(val photoList: List<Photo> = listOf())