package com.android.example.plantmamaapp_v3.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.example.plantmamaapp_v3.data.NoteDocument
import com.android.example.plantmamaapp_v3.data.PhotoDocument
import com.android.example.plantmamaapp_v3.data.PlantDatabase
import com.android.example.plantmamaapp_v3.data.PlantDocument
import com.android.example.plantmamaapp_v3.data.ReminderDocument
import com.android.example.plantmamaapp_v3.data.SyncState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UploadSyncWorker (
    appContent: Context,
    workerParams: WorkerParameters
): CoroutineWorker(appContent, workerParams){

    private val db = PlantDatabase.getDatabase(appContent)
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override suspend fun doWork(): Result {
        if (userId == null) return Result.failure()
        val userDocRef = firestore.collection("users").document(userId)

        return try {
            //plant sync
            val unsyncedPlants = db.plantDao().getUnsyncedPlants()
            for (plant in unsyncedPlants){
               val plantDto = PlantDocument(
                   roomId = plant.id,
                   name = plant.name,
                   profilePic = plant.profilePic,
                   datePurchased = plant.datePurchased.time,
                   type = plant.type,
                   description = plant.description,
                   lastUpdated = plant.lastUpdated
               )

               userDocRef.collection("plants").document(plant.id.toString())
                   .set(plantDto).await()
                db.plantDao().updateSyncState(plant.id, SyncState.SYNCED.name)
            }

            //reminder sync
            val unsyncedReminder = db.reminderDao().getUnsyncedReminders()
            for (reminder in unsyncedReminder){
                val reminderDto = ReminderDocument(
                    roomId = reminder.id,
                    wmIdentifier = reminder.wmIdentifier,
                    title = reminder.title,
                    date = reminder.date,
                    time = reminder.time,
                    lastUpdated = reminder.lastUpdated
                )

                userDocRef.collection("plants").document(reminder.plantID.toString())
                .collection("reminders").document(reminder.id.toString())
                    .set(reminderDto).await()
                db.reminderDao().updateSyncState(reminder.id, SyncState.SYNCED.name)
            }

            //photo sync
            val unsyncedPhoto = db.photoDao().getUnsyncedPhotos()
            for (photo in unsyncedPhoto){
                val photoDto = PhotoDocument(
                    roomId = photo.id,
                    uri = photo.uri,
                    lastUpdated = photo.lastUpdated
                )

                userDocRef.collection("plants").document(photo.plantId.toString())
                .collection("photos").document(photo.id.toString())
                    .set(photoDto).await()
                db.photoDao().updateSyncState(photo.id, SyncState.SYNCED.name)
            }

            //note sync
            val unsyncedNote = db.noteDao().getUnsyncedNotes()
            for (noteWithPhotos in unsyncedNote){
                val noteDto = NoteDocument(
                    roomId = noteWithPhotos.note.noteId,
                    title = noteWithPhotos.note.title,
                    date = noteWithPhotos.note.date.time,
                    photoRoomIds = noteWithPhotos.photos.map { it.id },
                    lastUpdated = noteWithPhotos.note.lastUpdated
                )

                userDocRef.collection("plants").document(noteWithPhotos.note.plantId.toString())
                .collection("notes").document(noteWithPhotos.note.noteId.toString())
                    .set(noteDto).await()
                db.noteDao().updateSyncState(noteWithPhotos.note.noteId, SyncState.SYNCED.name)
            }

            Result.success()
        } catch (e: Exception){
            Log.e("SyncWorkerError", "Upload failed due to exception: ", e)
            Result.retry()
        }
    }
}
