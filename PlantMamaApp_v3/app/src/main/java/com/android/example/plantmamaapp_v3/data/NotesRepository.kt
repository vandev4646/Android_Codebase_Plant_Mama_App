package com.android.example.plantmamaapp_v3.data

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    suspend fun updateNote(note: Note)

    suspend fun deleteNotePhotoCrossRefsForNote(noteId: Int)

    suspend fun getUnsyncedNotes(): List<NoteWithPhotos>

    suspend fun updateSyncState(id: Int, state: String)

    suspend fun upsertNote(note: Note)

    suspend fun insertCrossRef(crossRef: NotePhotoCrossRef)

    suspend fun getLastUpdatedTime(id: Int): Long?
}