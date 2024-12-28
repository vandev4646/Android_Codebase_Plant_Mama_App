package com.android.example.plantmamaapp_v3.data


import java.util.concurrent.TimeUnit

interface WaterRepository {
    fun scheduleReminder(
        duration: Long,
        unit: TimeUnit,
        plantName: String,
        reminderTitle: String,
        reminderIdentifier: String,
        recurrence: Recurrence
    )

    fun deleteReminder(
        reminderIdentifier: String
    )

    val plants: List<Plant>
}