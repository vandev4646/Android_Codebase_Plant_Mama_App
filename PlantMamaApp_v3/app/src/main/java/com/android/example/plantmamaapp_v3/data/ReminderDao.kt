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
interface ReminderDao {
    @Query("SELECT * from reminders WHERE plantID = :plantId")
    fun getAllItems(plantId: Int): Flow<List<Reminder>>

    @Query("SELECT * from reminders WHERE plantID = :plantId")
    suspend fun getAllItemsNonFlow(plantId: Int): List<Reminder>

    @Query("SELECT * from reminders WHERE id = :id")
    fun getItem(id: Int): Flow<Reminder>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(reminder: Reminder)

    @Update
    suspend fun update(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)

    @Query("DELETE from reminders WHERE wmIdentifier = :id")
    fun deleteByReminderWM(id: String)
}