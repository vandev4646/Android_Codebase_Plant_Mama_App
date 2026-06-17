package com.android.example.plantmamaapp_v3.ui


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.plantmamaapp_v3.data.Note
import com.android.example.plantmamaapp_v3.data.NotePhotoCrossRef
import com.android.example.plantmamaapp_v3.data.NotesRepository
import com.android.example.plantmamaapp_v3.data.Photo
import com.android.example.plantmamaapp_v3.data.PhotosRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

class NoteEditViewModel (
    savedStateHandle: SavedStateHandle,
    private val notesRepository: NotesRepository,
    private val photosRepository: PhotosRepository
): ViewModel(){

    val noteId: Int = checkNotNull(savedStateHandle[NoteEditDestination.noteIdArg])
    var noteUiState by mutableStateOf(NoteEditUiState())
        private set

    var plantGalleryPhotos by mutableStateOf<List<Photo>>(emptyList())
        private set

    init {
        viewModelScope.launch {
            val originalNoteWithPhotos = notesRepository.getNoteWithPhotoStream(noteId)
                .filterNotNull()
                .first()

            val plantId = originalNoteWithPhotos.note.plantId

            noteUiState = NoteEditUiState(
                noteDetails = NoteEditDetails(
                    title = originalNoteWithPhotos.note.title,
                    date = originalNoteWithPhotos.note.date.time,
                    plantId = originalNoteWithPhotos.note.plantId
                ),
                selectedPhotos = originalNoteWithPhotos.photos,
                isEntryValid = originalNoteWithPhotos.note.title.isNotBlank()
            )

            plantGalleryPhotos = photosRepository.getAllPhotoNonStreamByPlantId(plantId)
        }
    }

    fun updateUiState(details: NoteEditDetails){
        noteUiState = NoteEditUiState(
            noteDetails = details,
            selectedPhotos = noteUiState.selectedPhotos,
            isEntryValid = details.title.isNotBlank()
        )
    }

    fun togglePhotoSelection(photo: Photo){
        val currentSelected = noteUiState.selectedPhotos.toMutableList()
        if(currentSelected.contains(photo)){
            currentSelected.remove(photo)
        } else{
            currentSelected.add(photo)
        }
        noteUiState = noteUiState.copy(selectedPhotos = currentSelected)
    }

    fun updateNote(onComplete: () -> Unit){
        if(!noteUiState.isEntryValid) return
        viewModelScope.launch {
            val updatedNote = Note(
                noteId = noteId,
                plantId = noteUiState.noteDetails.plantId,
                title = noteUiState.noteDetails.title,
                date = Date(noteUiState.noteDetails.date)
            )

            notesRepository.updateNote(updatedNote)

            notesRepository.deleteNotePhotoCrossRefsForNote(noteId)

            noteUiState.selectedPhotos.forEach { photo ->
                val crossRef = NotePhotoCrossRef(
                    noteId = noteId,
                    photoId = photo.id
                )
                notesRepository.insertNotePhotoCrossRef(crossRef)
            }
            onComplete()
        }
    }

    fun deleteNote(onComplete: () -> Unit){

        viewModelScope.launch {
            val updatedNote = Note(
                noteId = noteId,
                plantId = noteUiState.noteDetails.plantId,
                title = noteUiState.noteDetails.title,
                date = Date(noteUiState.noteDetails.date)
            )
            notesRepository.deleteNote(updatedNote)
        }

        onComplete()
        onComplete()

    }

}

data class NoteEditUiState(
    val noteDetails: NoteEditDetails = NoteEditDetails(),
    val selectedPhotos: List<Photo> = emptyList(),
    val isEntryValid: Boolean = false
)

data class NoteEditDetails(
    val title: String = "",
    val date: Long = System.currentTimeMillis(),
    val plantId: Int = 0
)