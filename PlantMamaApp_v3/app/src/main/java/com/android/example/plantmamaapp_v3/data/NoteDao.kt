package com.android.example.plantmamaapp_v3.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note): Long //returns the newly created note Id

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotePhotoCrossRef(crossRef: NotePhotoCrossRef)

    @Transaction
    @Query("SELECT * FROM notes WHERE plantId = :plantId ORDER BY date DESC")
    fun getNotesForPlant(plantId: Int): Flow<List<NoteWithPhotos>>

    @Transaction
    @Query("SELECT * FROM plants WHERE id = :plantId AND syncState != 'TO_DELETE'")
    fun getFullPlantHistory(plantId: Int): Flow<PlantWithNotesAndPhotos>

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * from notes WHERE noteId = :id")
    fun getNoteWithPhotoStream(id: Int): Flow<NoteWithPhotos?>

    @Query("SELECT * from notes WHERE noteId = :id")
    suspend fun getNoteWithPhoto(id: Int): NoteWithPhotos

    @Update
    suspend fun updateNote(note: Note)

    @Query("DELETE FROM note_photo_cross_ref WHERE noteId = :noteId")
    suspend fun deleteNotePhotoCrossRefsForNote(noteId: Int)

    //functions to support firestore sync
    @Transaction
    @Query("SELECT * FROM notes WHERE syncState = 'NOT_SYNCED'")
    suspend fun getUnsyncedNotes(): List<NoteWithPhotos>

    @Query("UPDATE notes SET syncState = :state WHERE noteId = :id")
    suspend fun updateSyncState(id: Int, state: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(crossRef: NotePhotoCrossRef)

    @Query("SELECT lastUpdated FROM notes WHERE noteId = :id")
    suspend fun getLastUpdatedTime(id: Int): Long?

}