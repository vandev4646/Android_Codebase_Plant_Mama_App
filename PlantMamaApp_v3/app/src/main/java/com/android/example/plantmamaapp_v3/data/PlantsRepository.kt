package com.android.example.plantmamaapp_v3.data

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Plant] from a given data source.
 */
interface PlantsRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllPlantsStream(): Flow<List<Plant>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getPlantStream(id: Int): Flow<Plant?>

    suspend fun getPlant(id: Int): Plant

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getFirstPlant(id: Int): Plant?

    /**
     * Insert item in the data source
     */
    suspend fun insertPlant(plant: Plant)

    /**
     * Delete item from the data source
     */
    suspend fun deletePlant(plant: Plant)

    /**
     * Update item in the data source
     */
    suspend fun updatePlant(plant: Plant)


    suspend fun getUnsyncedPlants(): List<Plant>

    suspend fun updateSyncState(id: Int, state: String)

    suspend fun upsertPlant(plant: Plant)

    suspend fun getLastUpdatedTime(id: Int): Long?
}