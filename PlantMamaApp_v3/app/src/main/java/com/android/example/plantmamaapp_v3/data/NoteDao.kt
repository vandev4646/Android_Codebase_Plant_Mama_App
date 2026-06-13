package com.android.example.plantmamaapp_v3.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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
    @Query("SELECT * FROM plants WHERE id = :plantId")
    fun getFullPlantHistory(plantId: Int): Flow<PlantWithNotesAndPhotos>

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * from notes WHERE noteId = :id")
    fun getNoteWithPhotoStream(id: Int): Flow<NoteWithPhotos?>

    @Query("SELECT * from notes WHERE noteId = :id")
    suspend fun getNoteWithPhoto(id: Int): NoteWithPhotos
}