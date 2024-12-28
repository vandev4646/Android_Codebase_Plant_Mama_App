package com.android.example.plantmamaapp_v3.data

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.android.example.plantmamaapp_v3.worker.DeleteRecordWorker
import com.android.example.plantmamaapp_v3.worker.WaterReminderWorker
import java.util.concurrent.TimeUnit

class WorkManagerWaterRepository(context: Context) : WaterRepository {
    private val workManager = WorkManager.getInstance(context)

    override val plants: List<Plant>
        get() = plants

    override fun scheduleReminder(
        duration: Long,
        unit: TimeUnit,
        plantName: String,
        reminderTitle: String,
        reminderIdentifier: String,
        recurrence: Recurrence
    ) {

        val data = Data.Builder()
            .putString(WaterReminderWorker.nameKey, plantName)
            .putString(WaterReminderWorker.reminderTitle, reminderTitle)
            .build()

        val workRequest = when (recurrence) {
            Recurrence.ONCE -> {
                OneTimeWorkRequestBuilder<WaterReminderWorker>()
                    .setInitialDelay(duration, unit)
                    .setInputData(data)
                    .build()
            }
            else -> {
                PeriodicWorkRequestBuilder<WaterReminderWorker>(
                    when (recurrence) {
                        Recurrence.DAILY -> 1
                        Recurrence.WEEKLY -> 7
                        Recurrence.MONTHLY -> 30 // approximate
                        Recurrence.YEARLY -> 365 // approximate
                        else -> 1 // default to daily if unknown
                    }, TimeUnit.DAYS
                )
                    .setInitialDelay(duration, unit)
                    .setInputData(data)
                    .build()
            }
        }

        if (recurrence == Recurrence.ONCE) {
            val deleteRecordWork = OneTimeWorkRequestBuilder<DeleteRecordWorker>()
                .setInputData(workDataOf("reminder_id" to reminderIdentifier))
                .build()

            workManager.beginUniqueWork(
                reminderIdentifier,
                ExistingWorkPolicy.REPLACE,
                (workRequest as OneTimeWorkRequest)
            )
                .then(deleteRecordWork)
                .enqueue()
        } else {
            workManager.enqueueUniquePeriodicWork(
                reminderIdentifier,
                ExistingPeriodicWorkPolicy.UPDATE,
                (workRequest as PeriodicWorkRequest)
            )
        }



    }

    override fun deleteReminder(reminderIdentifier: String) {

        workManager.cancelAllWorkByTag(reminderIdentifier)
        workManager.cancelUniqueWork(reminderIdentifier)

    }
}