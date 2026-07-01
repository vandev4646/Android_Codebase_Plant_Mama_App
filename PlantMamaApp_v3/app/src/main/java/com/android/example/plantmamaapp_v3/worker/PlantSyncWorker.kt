package com.android.example.plantmamaapp_v3.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.example.plantmamaapp_v3.data.PlantDatabase
import com.android.example.plantmamaapp_v3.data.PlantDocument
import com.android.example.plantmamaapp_v3.data.SyncState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PlantSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val db = PlantDatabase.getDatabase(appContext)
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override suspend fun doWork(): Result {
        if (userId == null) return Result.failure()

        //Extract the specific ID passed from the repository
        val plantId = inputData.getInt("PLANT_ID", -1)
        if (plantId == -1) return Result.failure()

        return try {
            //Fetch ONLY this specific plant from Room
            val plant = db.plantDao().getPlantNonFlow(plantId) ?: return Result.success()

            val plantDto = PlantDocument(
                roomId = plant.id,
                name = plant.name,
                profilePic = plant.profilePic,
                datePurchased = plant.datePurchased.time,
                type = plant.type,
                description = plant.description,
                lastUpdated = plant.lastUpdated
            )

            // upload to firestore
            firestore.collection("users").document(userId)
                .collection("plants").document(plantId.toString())
                .set(plantDto).await()

            //update room status after sucessful sync
            db.plantDao().updateSyncState(plantId, SyncState.SYNCED.name)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
