package com.android.example.plantmamaapp_v3.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Photo] from a given data source.
 */
interface PhotosRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllPhotosStream(): Flow<List<Photo>>

    /**
     * Retrieve all items based on the provided plant id
     */
    fun getAllPhotoStreamByPlantId(plantId: Int): Flow<List<Photo>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getPhotoStream(id: Int): Flow<Photo?>

    /**
     * Insert item in the data source
     */
    suspend fun insertPhoto(photo: Photo)

    /**
     * Delete item from the data source
     */
    suspend fun deletePhoto(photo: Photo)

    /**
     * Update item in the data source
     */
    suspend fun updatePhoto(photo: Photo)
}