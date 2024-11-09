package com.android.example.plantmamaapp_v3.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Photo database
 */
@Dao
interface PhotoDao {
    @Query("SELECT * from photos")
    fun getAllItems(): Flow<List<Photo>>

    @Query("SELECT * from photos WHERE plantId = :plantId")
    fun getAllItemsbyPlantId(plantId: Int): Flow<List<Photo>>

    @Query("SELECT * from photos WHERE plantId = :plantId")
    suspend fun getAllPlantsNonFlow(plantId: Int): List<Photo>

    @Query("SELECT * from photos WHERE id = :id")
    fun getItem(id: Int): Flow<Photo>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(photo: Photo)

    @Update
    suspend fun update(photo: Photo)

    @Delete
    suspend fun delete(photo: Photo)
}