package com.android.example.plantmamaapp_v3.data

import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    suspend fun insertNote(note: Note): Long
    suspend fun insertNotePhotoCrossRef(crossRef: NotePhotoCrossRef)
    fun getNotesForPlant(plantId: Int): Flow<List<NoteWithPhotos>>
    fun getFullPlantHistory(plantId: Int): Flow<PlantWithNotesAndPhotos>
    suspend fun deleteNote(note: Note)
    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getNoteWithPhotoStream(id: Int): Flow<NoteWithPhotos?>

    suspend fun getNoteWithPhoto(id: Int): NoteWithPhotos
}