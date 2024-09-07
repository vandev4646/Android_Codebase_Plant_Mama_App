package com.android.example.plantmamaapp_v3.data

import kotlinx.coroutines.flow.Flow

class OfflinePhotoRepository (private val photoDao: PhotoDao) : PhotosRepository {
    override fun getAllPhotosStream(): Flow<List<Photo>> = photoDao.getAllItems()

    override fun getAllPhotoStreamByPlantId(plantId: Int): Flow<List<Photo>> = photoDao.getAllItemsbyPlantId(plantId)

    override fun getPhotoStream(id: Int): Flow<Photo?> = photoDao.getItem(id)

    override suspend fun insertPhoto(photo: Photo) = photoDao.insert(photo)

    override suspend fun deletePhoto(photo: Photo) = photoDao.delete(photo)

    override suspend fun updatePhoto(photo: Photo) = photoDao.update(photo)

}