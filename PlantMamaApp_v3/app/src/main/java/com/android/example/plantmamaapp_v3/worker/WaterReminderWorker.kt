package com.android.example.plantmamaapp_v3.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.example.plantmamaapp_v3.R


class WaterReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        val plantName = inputData.getString(nameKey)
        val reminderTitle = inputData.getString(reminderTitle)

        makePlantReminderNotification(
            reminderTitle?:"",
            plantName+" Reminder!"?:"",
            applicationContext
        )

        return Result.success()
    }

    companion object {
        const val nameKey = "NAME"
        const val reminderTitle = "TITLE"
    }
}