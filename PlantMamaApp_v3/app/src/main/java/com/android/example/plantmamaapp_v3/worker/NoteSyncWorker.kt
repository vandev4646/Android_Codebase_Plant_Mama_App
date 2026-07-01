package com.android.example.plantmamaapp_v3.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.example.plantmamaapp_v3.data.NoteDocument
import com.android.example.plantmamaapp_v3.data.PlantDatabase
import com.android.example.plantmamaapp_v3.data.SyncState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NoteSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val db = PlantDatabase.getDatabase(appContext)
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override suspend fun doWork(): Result {
        if (userId == null) return Result.failure()

        val noteId = inputData.getInt("NOTE_ID", -1)
        if (noteId == -1) return Result.failure()

        return try {
            // Fetch the specific note relation structure
            val noteWithPhotos = db.noteDao().getNoteWithPhoto(noteId) ?: return Result.success()
            val note = noteWithPhotos.note

            val noteDto = NoteDocument(
                roomId = note.noteId,
                title = note.title,
                date = note.date.time,
                photoRoomIds = noteWithPhotos.photos.map { it.id },
                lastUpdated = note.lastUpdated
            )

            //upload to firestore
            firestore.collection("users").document(userId)
                .collection("plants").document(note.plantId.toString())
                .collection("notes").document(noteId.toString())
                .set(noteDto).await()

            db.noteDao().updateSyncState(noteId, SyncState.SYNCED.name)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
