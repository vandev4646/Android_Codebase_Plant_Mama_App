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
    @Query("SELECT * from reminders WHERE syncState != 'TO_DELETE'")
    fun getAllItems(): Flow<List<Reminder>>
    @Query("SELECT * from reminders WHERE plantID = :plantId AND syncState != 'TO_DELETE'")
    fun getAllItems(plantId: Int): Flow<List<Reminder>>

    @Query("SELECT * from reminders WHERE plantID = :plantId AND syncState != 'TO_DELETE'")
    suspend fun getAllItemsNonFlow(plantId: Int): List<Reminder>

    @Query("SELECT * from reminders WHERE id = :id")
    fun getItem(id: Int): Flow<Reminder>

    @Query("SELECT * from reminders WHERE id = :id")
    suspend fun getItemNonFlow(id: Int): Reminder

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(reminder: Reminder): Long

    @Update
    suspend fun update(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)

    @Query("DELETE from reminders WHERE wmIdentifier = :id")
    fun deleteByReminderWM(id: String)

    //functions to support firestore sync
    @Query("SELECT * FROM reminders WHERE syncState = 'NOT_SYNCED'")
    suspend fun getUnsyncedReminders(): List<Reminder>

    @Query("UPDATE reminders SET syncState = :state WHERE id = :id")
    suspend fun updateSyncState(id: Int, state: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertReminder(reminder: Reminder)

    @Query("SELECT lastUpdated FROM reminders WHERE id = :id")
    suspend fun getLastUpdatedTime(id: Int): Long?
}