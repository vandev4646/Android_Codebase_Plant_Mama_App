package com.android.example.plantmamaapp_v3.data

import kotlinx.coroutines.flow.Flow

class OfflineRemindersRepository(private val reminderDao: ReminderDao) : ReminderRepository {
    override fun getAllRemindersStream(plantId: Int): Flow<List<Reminder>> = reminderDao.getAllItems(plantId)

    override fun getReminderStream(id: Int): Flow<Reminder?> = reminderDao.getItem(id)

    override suspend fun insertReminder(reminder: Reminder) = reminderDao.insert(reminder)

    override suspend fun deleteReminder(reminder: Reminder) = reminderDao.delete(reminder)

    override suspend fun updateReminder(reminder: Reminder) = reminderDao.update(reminder)
}