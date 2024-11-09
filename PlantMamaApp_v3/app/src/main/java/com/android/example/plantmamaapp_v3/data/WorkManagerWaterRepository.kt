package com.android.example.plantmamaapp_v3.data

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
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
        reminderIdentifier: String
    ) {
        val data = Data.Builder()
        data.putString(WaterReminderWorker.nameKey, plantName)
        data.putString(WaterReminderWorker.reminderTitle, reminderTitle)

        val workRequestBuilder = OneTimeWorkRequestBuilder<WaterReminderWorker>()
            .setInitialDelay(duration, unit)
            .setInputData(data.build())
            .build()

        val deleteRecordWork = OneTimeWorkRequestBuilder<DeleteRecordWorker>()
            .setInputData(workDataOf("reminder_id" to reminderIdentifier))
            .build()

        workManager.beginUniqueWork(
            reminderIdentifier,
            ExistingWorkPolicy.REPLACE,
            workRequestBuilder
        )
            .then(deleteRecordWork)
            .enqueue()


    }

    override fun deleteReminder(reminderIdentifier: String){

        workManager.cancelAllWorkByTag(reminderIdentifier)
        workManager.cancelUniqueWork(reminderIdentifier)

    }
}