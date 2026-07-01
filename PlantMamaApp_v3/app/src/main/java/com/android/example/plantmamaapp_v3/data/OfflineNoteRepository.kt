package com.android.example.plantmamaapp_v3.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.android.example.plantmamaapp_v3.worker.NoteDeleteWorker
import com.android.example.plantmamaapp_v3.worker.NoteSyncWorker
import com.android.example.plantmamaapp_v3.worker.PlantDeleteWorker
import kotlinx.coroutines.flow.Flow

class OfflineNoteRepository(
    private val noteDao: NoteDao,
    context: Context
): NotesRepository  {

    val appContext = context.applicationContext

    override suspend fun insertNote(note: Note): Long {
        val noteId = noteDao.insertNote(note)
        triggerNoteSync(noteId.toInt())
        return noteId
    }

    override suspend fun insertNotePhotoCrossRef(crossRef: NotePhotoCrossRef) {
        return noteDao.insertNotePhotoCrossRef(crossRef)
    }

    override fun getNotesForPlant(plantId: Int): Flow<List<NoteWithPhotos>> {
        return noteDao.getNotesForPlant(plantId)
    }

    override fun getFullPlantHistory(plantId: Int): Flow<PlantWithNotesAndPhotos> {
        return  noteDao.getFullPlantHistory(plantId)
    }

    override suspend fun deleteNote(note: Note) {
        val tombstone = note.copy(
            syncState = SyncState.TO_DELETE,
            lastUpdated = System.currentTimeMillis()
        )

        noteDao.updateNote(tombstone)
        triggerNoteDeleteSync(tombstone.noteId)
    }

    override fun getNoteWithPhotoStream(id: Int): Flow<NoteWithPhotos?> {
        return noteDao.getNoteWithPhotoStream(id)
    }

    override suspend fun getNoteWithPhoto(id: Int): NoteWithPhotos {
        return noteDao.getNoteWithPhoto(id)
    }

    override suspend fun updateNote(note: Note) {
        val localNote = note.copy(
            syncState = SyncState.NOT_SYNCED,
            lastUpdated = System.currentTimeMillis()
        )
        noteDao.updateNote(localNote)
        triggerNoteSync(note.noteId)
    }

    override suspend fun deleteNotePhotoCrossRefsForNote(noteId: Int) {
        return noteDao.deleteNotePhotoCrossRefsForNote(noteId)
    }

    override suspend fun getUnsyncedNotes(): List<NoteWithPhotos>  = noteDao.getUnsyncedNotes()

    override suspend fun updateSyncState(id: Int, state: String) = noteDao.updateSyncState(id, state)

    override suspend fun upsertNote(note: Note) = noteDao.upsertNote(note)

    override suspend fun insertCrossRef(crossRef: NotePhotoCrossRef) = noteDao.insertCrossRef(crossRef)

    override suspend fun getLastUpdatedTime(id: Int): Long? = noteDao.getLastUpdatedTime(id)

    private fun triggerNoteSync(noteId: Int){
        val inputData = Data.Builder()
            .putInt("NOTE_ID", noteId)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<NoteSyncWorker>()
            .setInputData(inputData)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(appContext).enqueueUniqueWork(
            "sync_note_$noteId",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }

    private fun triggerNoteDeleteSync(noteId: Int){
        val inputData = Data.Builder()
            .putInt("NOTE_ID", noteId)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<NoteDeleteWorker>()
            .setInputData(inputData)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(appContext).enqueueUniqueWork(
            "delete_note_$noteId",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )

    }
}