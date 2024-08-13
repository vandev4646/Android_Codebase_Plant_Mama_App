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
    @Query("SELECT * from plants ORDER BY name ASC")
    fun getAllItems(): Flow<List<Plant>>

    @Query("SELECT * from plants WHERE name = :id")
    fun getItem(id: Int): Flow<Plant>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(plant: Plant)

    @Update
    suspend fun update(plant: Plant)

    @Delete
    suspend fun delete(plant: Plant)
}