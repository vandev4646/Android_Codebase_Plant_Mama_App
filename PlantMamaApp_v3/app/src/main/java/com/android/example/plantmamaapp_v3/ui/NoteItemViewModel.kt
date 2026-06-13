package com.android.example.plantmamaapp_v3.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.plantmamaapp_v3.data.Note
import com.android.example.plantmamaapp_v3.data.NoteWithPhotos
import com.android.example.plantmamaapp_v3.data.NotesRepository
import com.android.example.plantmamaapp_v3.data.OfflineNoteRepository
import com.android.example.plantmamaapp_v3.data.Plant
import com.android.example.plantmamaapp_v3.data.PlantsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date

data class NoteItemUiState(val noteWithPhotos: NoteWithPhotos = NoteWithPhotos(
    note = Note(
        noteId = 0,
        plantId = 0,
        title = "",
        date = Date(System.currentTimeMillis())
    ),
    photos = emptyList()
))

class NoteItemViewModel (
    savedStateHandle: SavedStateHandle,
    noteRepository: NotesRepository
) : ViewModel(){
    val noteId: Int = checkNotNull(savedStateHandle[NoteItemDestination.noteIdArg])
    val noteItemUiState: StateFlow<NoteItemUiState> = noteRepository.getNoteWithPhotoStream(noteId)
        .map{ it ->
            NoteItemUiState(it ?:
            NoteWithPhotos(
                note = Note(
                    noteId = 0,
                    plantId = 0,
                    title = "",
                    date = Date(System.currentTimeMillis())
                ),
                photos = emptyList()
            )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NoteItemUiState()
        )
}