package com.android.example.plantmamaapp_v3.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.example.plantmamaapp_v3.data.PlantDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PlantDeleteWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {
    private val db = PlantDatabase.getDatabase(appContext)
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override suspend fun doWork(): Result {
        if (userId == null) return Result.failure()
        val plantId = inputData.getInt("PLANT_ID", -1)
        if (plantId == -1) return Result.failure()
        val plant = db.plantDao().getPlantNonFlow(plantId)

        return try {
            // Remove the plant document tree from Firestore
            firestore.collection("users").document(userId)
                .collection("plants").document(plantId.toString())
                .delete().await()

            // delete plant from room db
            db.plantDao().delete(plant)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
