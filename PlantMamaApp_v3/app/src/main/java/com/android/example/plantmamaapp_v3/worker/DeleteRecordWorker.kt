package com.android.example.plantmamaapp_v3.worker

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.room.Room
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.example.plantmamaapp_v3.PlantMamaApplication
import com.android.example.plantmamaapp_v3.data.DefaultAppContainer
import com.android.example.plantmamaapp_v3.data.PlantDatabase
import com.android.example.plantmamaapp_v3.ui.plantMamaApplication

class DeleteRecordWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    val context = context
    override fun doWork(): Result {
        val reminderId = inputData.getString("reminder_id")
        if (reminderId != null) {
            val reminderDao = PlantDatabase.getDatabase(context).reminderDao()
            reminderDao.deleteByReminderWM(reminderId)
            return Result.success()
        }
        return Result.failure()
    }
}


