package com.android.example.plantmamaapp_v3.ui.note


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.plantmamaapp_v3.data.Note
import com.android.example.plantmamaapp_v3.data.NotePhotoCrossRef
import com.android.example.plantmamaapp_v3.data.NotesRepository
import com.android.example.plantmamaapp_v3.data.Photo
import kotlinx.coroutines.launch
import java.util.Date

class NoteViewModel (private val notesRepository: NotesRepository): ViewModel(){
    var noteUiState by mutableStateOf(NoteUiState())
        private set

    fun updateUiState(details: NoteDetails){
        noteUiState = NoteUiState(
            noteDetails = details,
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

    fun saveNote(plantId: Int, onComplete: () -> Unit){
        if(!noteUiState.isEntryValid) return
        viewModelScope.launch {
            val newNote = Note(
                plantId = plantId,
                title = noteUiState.noteDetails.title,
                date = Date(noteUiState.noteDetails.date)
            )

            val generatedNoteId = notesRepository.insertNote(newNote).toInt()

            noteUiState.selectedPhotos.forEach { photo ->
                val crossRef = NotePhotoCrossRef(
                    noteId = generatedNoteId,
                    photoId = photo.id
                )
                notesRepository.insertNotePhotoCrossRef(crossRef)
            }
            onComplete()
        }
    }

}

data class NoteUiState(
    val noteDetails: NoteDetails = NoteDetails(),
    val selectedPhotos: List<Photo> = emptyList(),
    val isEntryValid: Boolean = false
)

data class NoteDetails(
    val title: String = "",
    val date: Long = System.currentTimeMillis()
)