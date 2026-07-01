package com.android.example.plantmamaapp_v3.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.example.plantmamaapp_v3.data.PlantDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NoteDeleteWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {
    private val db = PlantDatabase.getDatabase(appContext)
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override suspend fun doWork(): Result {
        if (userId == null) return Result.failure()
        val noteId = inputData.getInt("NOTE_ID", -1)
        if (noteId == -1) return Result.failure()

        return try {
            // We need to fetch the note before deleting to find its plantId path mapping
            val note = db.noteDao().getNoteWithPhoto(noteId) ?: return Result.success()

            firestore.collection("users").document(userId)
                .collection("plants").document(note.note.plantId.toString())
                .collection("notes").document(noteId.toString())
                .delete().await()

            db.noteDao().deleteNotePhotoCrossRefsForNote(noteId)
            db.noteDao().deleteNote(note.note)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
