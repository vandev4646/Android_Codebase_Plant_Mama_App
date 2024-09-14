package com.android.example.plantmamaapp_v3.data

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
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
        reminderIdentifier: String
    ) {
        val data = Data.Builder()
        data.putString(WaterReminderWorker.nameKey, plantName)

        val workRequestBuilder = OneTimeWorkRequestBuilder<WaterReminderWorker>()
            .setInitialDelay(duration, unit)
            .setInputData(data.build())
            .build()

        workManager.enqueueUniqueWork(
            reminderIdentifier,
            ExistingWorkPolicy.REPLACE,
            workRequestBuilder
        )
    }
}