package com.android.example.plantmamaapp_v3.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.example.plantmamaapp_v3.data.PlantDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReminderDeleteWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {
    private val db = PlantDatabase.getDatabase(appContext)
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override suspend fun doWork(): Result {
        if (userId == null) return Result.failure()
        val reminderId = inputData.getInt("REMINDER_ID", -1)
        if (reminderId == -1) return Result.failure()

        return try {
            val reminder = db.reminderDao().getItemNonFlow(reminderId) ?: return Result.success()

            firestore.collection("users").document(userId)
                .collection("plants").document(reminder.plantID.toString())
                .collection("reminders").document(reminderId.toString())
                .delete().await()

            db.reminderDao().delete(reminder)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
