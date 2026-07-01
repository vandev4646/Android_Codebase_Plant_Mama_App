package com.android.example.plantmamaapp_v3.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.android.example.plantmamaapp_v3.worker.PhotoDeleteWorker
import com.android.example.plantmamaapp_v3.worker.PhotoSyncWorker
import com.android.example.plantmamaapp_v3.worker.PlantDeleteWorker
import kotlinx.coroutines.flow.Flow

class OfflinePhotoRepository(
    private val photoDao: PhotoDao,
    context: Context
) : PhotosRepository {
    val appContext = context.applicationContext
    override fun getAllPhotosStream(): Flow<List<Photo>> = photoDao.getAllItems()

    override fun getAllPhotoStreamByPlantId(plantId: Int): Flow<List<Photo>> =
        photoDao.getAllItemsbyPlantId(plantId)

    override suspend fun getAllPhotoNonStreamByPlantId(plantId: Int): List<Photo> =
        photoDao.getAllPlantsNonFlow(plantId)

    override fun getPhotoStream(id: Int): Flow<Photo?> = photoDao.getItem(id)

    override suspend fun insertPhoto(photo: Photo){
        val generatedId = photoDao.insert(photo).toInt()
        triggerPhotoSync(generatedId)
    }

    override suspend fun deletePhoto(photo: Photo){
        val tombstone = photo.copy(
            syncState = SyncState.TO_DELETE,
            lastUpdated = System.currentTimeMillis()
        )

        photoDao.update(tombstone)
        triggerPhotoDeleteSync(tombstone.id)
    }

    override suspend fun updatePhoto(photo: Photo){
        val localPhoto = photo.copy(
            syncState = SyncState.NOT_SYNCED,
            lastUpdated = System.currentTimeMillis()
        )
        photoDao.update(localPhoto)
        triggerPhotoSync(photo.id)
    }

    override suspend fun getUnsyncedPhotos(): List<Photo> = photoDao.getUnsyncedPhotos()

    override suspend fun updateSyncState(id: Int, state: String) = photoDao.updateSyncState(id, state)

    override suspend fun upsertPhoto(photo: Photo) = photoDao.upsertPhoto(photo)

    override suspend fun getLastUpdatedTime(id: Int): Long? = photoDao.getLastUpdatedTime(id)

    override suspend fun getItemNonFlow(id: Int): Photo = photoDao.getItemNonFlow(id)

    private fun triggerPhotoSync(photoId: Int){
        val inputData = Data.Builder()
            .putInt("PHOTO_ID", photoId)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<PhotoSyncWorker>()
            .setInputData(inputData)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(appContext).enqueueUniqueWork(
            "sync_photo_${photoId}",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }

    private fun triggerPhotoDeleteSync(photoId: Int){
        val inputData = Data.Builder()
            .putInt("PHOTO_ID", photoId)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<PhotoDeleteWorker>()
            .setInputData(inputData)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(appContext).enqueueUniqueWork(
            "delete_photo_$photoId",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )

    }

}