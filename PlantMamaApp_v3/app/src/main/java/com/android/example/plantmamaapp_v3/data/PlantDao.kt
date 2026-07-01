package com.android.example.plantmamaapp_v3.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Plant database
 */
@Dao
interface PlantDao {
    @Query("SELECT * from plants WHERE syncState != 'TO_DELETE' ORDER BY name ASC")
    fun getAllItems(): Flow<List<Plant>>

    @Query("SELECT * from plants WHERE id = :id")
    fun getItem(id: Int): Flow<Plant>

    @Query("SELECT * from plants WHERE id = :id")
    fun getPlantNonFlow(id: Int): Plant

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(plant: Plant): Long

    @Update
    suspend fun update(plant: Plant)

    @Delete
    suspend fun delete(plant: Plant)

    //functions to support firestore sync
    @Query("SELECT * FROM plants WHERE syncState = 'NOT_SYNCED'")
    suspend fun getUnsyncedPlants(): List<Plant>

    @Query("UPDATE plants SET syncState = :state WHERE id = :id")
    suspend fun updateSyncState(id: Int, state: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPlant(plant: Plant)

    @Query("SELECT lastUpdated FROM plants WHERE id = :id")
    suspend fun getLastUpdatedTime(id: Int): Long?
}