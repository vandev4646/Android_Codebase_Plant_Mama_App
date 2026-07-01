package com.android.example.plantmamaapp_v3.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.example.plantmamaapp_v3.data.PhotoDocument
import com.android.example.plantmamaapp_v3.data.PlantDatabase
import com.android.example.plantmamaapp_v3.data.SyncState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PhotoSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val db = PlantDatabase.getDatabase(appContext)
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override suspend fun doWork(): Result {
        if (userId == null) return Result.failure()

        val photoId = inputData.getInt("PHOTO_ID", -1)
        if (photoId == -1) return Result.failure()

        return try {
            val photo = db.photoDao().getItemNonFlow(photoId) ?: return Result.success()

            val photoDto = PhotoDocument(
                roomId = photo.id,
                uri = photo.uri,
                lastUpdated = photo.lastUpdated
            )

            //upload to firestore
            firestore.collection("users").document(userId)
                .collection("plants").document(photo.plantId.toString())
                .collection("photos").document(photoId.toString())
                .set(photoDto).await()

            db.photoDao().updateSyncState(photoId, SyncState.SYNCED.name)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
