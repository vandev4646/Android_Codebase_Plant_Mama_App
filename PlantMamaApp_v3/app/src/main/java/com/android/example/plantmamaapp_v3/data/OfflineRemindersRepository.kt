package com.android.example.plantmamaapp_v3.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.android.example.plantmamaapp_v3.worker.PlantDeleteWorker
import com.android.example.plantmamaapp_v3.worker.ReminderDeleteWorker
import com.android.example.plantmamaapp_v3.worker.ReminderSyncWorker
import kotlinx.coroutines.flow.Flow

class OfflineRemindersRepository(
    private val reminderDao: ReminderDao,
    context: Context
) : ReminderRepository {
    val appContext = context.applicationContext

    override fun getAllRemindersStream(): Flow<List<Reminder>> = reminderDao.getAllItems()
    override fun getAllRemindersStream(plantId: Int): Flow<List<Reminder>> =
        reminderDao.getAllItems(plantId)

    override suspend fun getAllRemindersNonStream(plantId: Int): List<Reminder> =
        reminderDao.getAllItemsNonFlow(plantId)

    override fun getReminderStream(id: Int): Flow<Reminder?> = reminderDao.getItem(id)

    override suspend fun insertReminder(reminder: Reminder){
        val generatedId = reminderDao.insert(reminder).toInt()
        triggerReminderSync(generatedId)
    }

    override suspend fun deleteReminder(reminder: Reminder){
        val tombstone = reminder.copy(
            syncState = SyncState.TO_DELETE,
            lastUpdated = System.currentTimeMillis()
        )
        reminderDao.update(tombstone)
        triggerReminderDeleteSync(tombstone.id)
    }

    override suspend fun updateReminder(reminder: Reminder){
        val localReminder = reminder.copy(
            syncState = SyncState.NOT_SYNCED,
            lastUpdated = System.currentTimeMillis()
        )
        reminderDao.update(localReminder)
        triggerReminderSync(reminder.id)
    }
    override suspend fun getUnsyncedReminders(): List<Reminder> = reminderDao.getUnsyncedReminders()

    override suspend fun updateSyncState(id: Int, state: String) = reminderDao.updateSyncState(id, state)

    override suspend fun upsertReminder(reminder: Reminder) = reminderDao.upsertReminder(reminder)

    override suspend fun getLastUpdatedTime(id: Int): Long? = reminderDao.getLastUpdatedTime(id)

    override suspend fun getItemNonFlow(id: Int): Reminder = reminderDao.getItemNonFlow(id)

    private fun triggerReminderSync(reminderId: Int){
        val inputData = Data.Builder()
            .putInt("REMINDER_ID", reminderId)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<ReminderSyncWorker>()
            .setInputData(inputData)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(appContext).enqueueUniqueWork(
            "sync_reminder_${reminderId}",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }

    private fun triggerReminderDeleteSync(reminderId: Int){
        val inputData = Data.Builder()
            .putInt("REMINDER_ID", reminderId)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<ReminderDeleteWorker>()
            .setInputData(inputData)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(appContext).enqueueUniqueWork(
            "delete_reminder_$reminderId",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )

    }
}