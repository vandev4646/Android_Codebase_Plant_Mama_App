package com.android.example.plantmamaapp_v3.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Plant] from a given data source.
 */
interface ReminderRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllRemindersStream(plantId: Int): Flow<List<Reminder>>

    suspend fun getAllRemindersNonStream(plantId: Int): List<Reminder>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getReminderStream(id: Int): Flow<Reminder?>

    /**
     * Insert item in the data source
     */
    suspend fun insertReminder(reminder: Reminder)

    /**
     * Delete item from the data source
     */
    suspend fun deleteReminder(reminder: Reminder)

    /**
     * Update item in the data source
     */
    suspend fun updateReminder(reminder: Reminder)
}