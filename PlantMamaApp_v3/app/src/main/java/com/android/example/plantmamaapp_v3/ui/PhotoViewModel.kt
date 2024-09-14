package com.android.example.plantmamaapp_v3.ui

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

class PhotoViewModel(
    savedStateHandle: SavedStateHandle,
    private val photosRepository: PhotosRepository
) : ViewModel() {

    //private val photoId: Int = checkNotNull(savedStateHandle[InspectPhotoScreenDestination.itemIdArg])

    var photoUiState by mutableStateOf(PhotoUiState())
        private set

    data class PhotoUiState(
        val photoDetails: PhotoDetails = PhotoDetails()
    )

    data class PhotoDetails(
        val plantId: Int = 0,
        val uri: String = "",
    )

    fun PhotoDetails.toItem(): Photo = Photo(
        plantId = plantId,
        uri = uri
    )

    fun updateUiState(photoDetails: PhotoDetails) {
        photoUiState =
            PhotoUiState(photoDetails = photoDetails)

    }

    suspend fun savePhoto(plantId: Int, uri: String) {
        var newUri = uri
        if (uri.contains("picker")) {
            newUri = "content://media/external/images/media/" + extractNumbers(uri)
        }
        val photo = Photo(plantId = plantId, uri = newUri)
        photosRepository.insertPhoto(photo)
    }



    fun generateNewUri(uri: Uri): String {
        var newUri = uri.toString()
        if (newUri.contains("picker")) {
            newUri = "content://media/external/images/media/" + extractNumbers(uri.toString())
        }
        return newUri
    }

    fun extractNumbers(input: String): String {
        val regex = "\\d+".toRegex()
        val matchResults = regex.findAll(input).toList()
        return matchResults.lastOrNull()?.value ?: ""
    }




}




