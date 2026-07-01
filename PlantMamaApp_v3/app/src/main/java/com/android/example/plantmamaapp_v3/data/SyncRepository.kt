package com.android.example.plantmamaapp_v3.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.android.example.plantmamaapp_v3.worker.UploadSyncWorker
import com.android.example.plantmamaapp_v3.worker.scheduleReminderWork
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date


class SyncRepository(private val context: Context) {
    private val db = PlantDatabase.getDatabase(context)
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun syncOnSignIn(userId: String) = withContext(Dispatchers.IO){
        val userDocRef = firestore.collection("users").document(userId)

        try {
            val plantSnapshots = userDocRef.collection("plants").get().await()

                for (plantDoc in plantSnapshots){
                    val cloudPlant = plantDoc.toObject(PlantDocument::class.java)?: continue
                    val localLastUpdated = db.plantDao().getLastUpdatedTime(cloudPlant.roomId)

                    if (localLastUpdated == null || cloudPlant.lastUpdated > localLastUpdated){
                        val plant = Plant(
                            id = cloudPlant.roomId,
                            profilePic = cloudPlant.profilePic,
                            name = cloudPlant.name,
                            datePurchased = Date(cloudPlant.datePurchased),
                            type = cloudPlant.type,
                            description = cloudPlant.description,
                            lastUpdated = cloudPlant.lastUpdated,
                            syncState = SyncState.SYNCED
                        )

                        db.plantDao().upsertPlant(plant)

                        restorePhotos(userDocRef, cloudPlant.roomId)
                        restoreNotes(userDocRef, cloudPlant.roomId)
                        restoreReminders(userDocRef, cloudPlant.roomId, cloudPlant.name)

                    }
                }



        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    suspend fun syncOnSignUp(){
        val syncRequest = OneTimeWorkRequestBuilder<UploadSyncWorker>()
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "upload_sync_job",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }

    private suspend fun restoreNotes(userDocRef: DocumentReference, plantId: Int){
        val noteSnapShots = userDocRef.collection("plants").document(plantId.toString())
            .collection("notes").get().await()

        for(noteDoc in noteSnapShots.documents){
            val cloudNote = noteDoc.toObject(NoteDocument::class.java)?: continue
            val localLastUpdated = db.noteDao().getLastUpdatedTime(cloudNote.roomId)

            if (localLastUpdated == null || cloudNote.lastUpdated > localLastUpdated){
                val note = Note(
                    noteId = cloudNote.roomId,
                    plantId = plantId,
                    title = cloudNote.title,
                    date = Date(cloudNote.date),
                    lastUpdated = cloudNote.lastUpdated,
                    syncState = SyncState.SYNCED
                )

                db.noteDao().upsertNote(note)

                // rebuild many to many local junction relationship table
                for(photoId in cloudNote.photoRoomIds){
                    val crossRef = NotePhotoCrossRef(noteId = cloudNote.roomId, photoId = photoId)
                    db.noteDao().insertCrossRef(crossRef)
                }
            }
        }
    }

    private suspend fun restoreReminders(userDocRef: DocumentReference, plantId: Int, plantName: String){
        val reminderSnapShots = userDocRef.collection("plants").document(plantId.toString())
            .collection("reminders").get().await()

        val workManager = WorkManager.getInstance(context)

        for(reminderDoc in reminderSnapShots.documents){
            val cloudReminder = reminderDoc.toObject(ReminderDocument::class.java)?: continue
            val localLastUpdated = db.reminderDao().getLastUpdatedTime(cloudReminder.roomId)

            if (localLastUpdated == null || cloudReminder.lastUpdated > localLastUpdated){
                val reminder = Reminder(
                    id = cloudReminder.roomId,
                    wmIdentifier = cloudReminder.wmIdentifier,
                    plantID = plantId,
                    title = cloudReminder.title,
                    date = cloudReminder.date,
                    time = cloudReminder.time,
                    lastUpdated = cloudReminder.lastUpdated,
                    syncState = SyncState.SYNCED
                )

                db.reminderDao().upsertReminder(reminder)

                //Check if this unique work identifier is already scheduled on this phone
                val workInfos = workManager.getWorkInfosForUniqueWork(reminder.wmIdentifier).await()
                val isAlreadyScheduled = workInfos.any { workInfo ->
                    workInfo.state == WorkInfo.State.ENQUEUED || workInfo.state == WorkInfo.State.RUNNING
                }

                //schedule reminder if missing
                if (!isAlreadyScheduled) {
                    scheduleReminderWork(context, reminder, plantName)
                }

            }
        }
    }

    private suspend fun restorePhotos(userDocRef: DocumentReference, plantId: Int){
        val photoSnapShots = userDocRef.collection("plants").document(plantId.toString())
            .collection("photos").get().await()

        for(photoDoc in photoSnapShots.documents){
            val cloudPhoto = photoDoc.toObject(PhotoDocument::class.java)?: continue
            val localLastUpdated = db.photoDao().getLastUpdatedTime(cloudPhoto.roomId)

            if (localLastUpdated == null || cloudPhoto.lastUpdated > localLastUpdated){
                val photo = Photo(
                    id = cloudPhoto.roomId,
                    plantId = plantId,
                    uri = cloudPhoto.uri,
                    lastUpdated = cloudPhoto.lastUpdated,
                    syncState = SyncState.SYNCED
                )

                db.photoDao().upsertPhoto(photo)
            }
        }
    }

}