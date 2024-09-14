package com.android.example.plantmamaapp_v3.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.plantmamaapp_v3.data.Photo
import com.android.example.plantmamaapp_v3.data.PhotosRepository
import com.android.example.plantmamaapp_v3.data.ReminderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InspectPhotoViewModel(
    savedStateHandle: SavedStateHandle,
    private val photosRepository: PhotosRepository
) : ViewModel() {

    private val photoId: Int = checkNotNull(savedStateHandle[InspectPhotoScreenDestination.itemIdArg])

    //for the Photo flow

    /**
     * Holds reminder list ui state. The list of items are retrieved from [ReminderRepository] and mapped to
     * [ReminderListUiState]
     */
    val photoViewUiState: StateFlow<PhotoViewUiState> =
        photosRepository.getPhotoStream(photoId).map { photo ->
            PhotoViewUiState(photo ?: Photo(-1, -1, ""))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = PhotoViewUiState()
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

    suspend fun deletePhoto(photo: Photo){
        viewModelScope.launch{
            photosRepository.deletePhoto(photo)
        }

    }
}

data class PhotoViewUiState(val photo: Photo = Photo(-1, -1, ""))