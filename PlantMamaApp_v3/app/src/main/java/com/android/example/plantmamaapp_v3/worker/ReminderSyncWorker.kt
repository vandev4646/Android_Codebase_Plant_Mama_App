package com.android.example.plantmamaapp_v3.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.example.plantmamaapp_v3.data.PlantDatabase
import com.android.example.plantmamaapp_v3.data.ReminderDocument
import com.android.example.plantmamaapp_v3.data.SyncState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReminderSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val db = PlantDatabase.getDatabase(appContext)
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override suspend fun doWork(): Result {
        if (userId == null) return Result.failure()

        val reminderId = inputData.getInt("REMINDER_ID", -1)
        if (reminderId == -1) return Result.failure()

        return try {
            // Load the specific reminder row out of Room
            val reminder = db.reminderDao().getItemNonFlow(reminderId) ?: return Result.success()

            val reminderDto = ReminderDocument(
                roomId = reminder.id,
                wmIdentifier = reminder.wmIdentifier,
                title = reminder.title,
                date = reminder.date,
                time = reminder.time,
                lastUpdated = reminder.lastUpdated
            )

            //update firestore
            firestore.collection("users").document(userId)
                .collection("plants").document(reminder.plantID.toString())
                .collection("reminders").document(reminderId.toString())
                .set(reminderDto).await()

            db.reminderDao().updateSyncState(reminderId, SyncState.SYNCED.name)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
