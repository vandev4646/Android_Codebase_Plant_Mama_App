package com.android.example.plantmamaapp_v3.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.concurrent.TimeUnit

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val wmIdentifier: String,
    val plantID: Int,
    val title: String,
    val date: String,
    val time: String
)

data class ReminderWM(
    val duration: Long,
    val unit: TimeUnit,
    val plantName: String,
    val reminderTitle: String,
    var reminderIdentifier: String,
    val recurrence: Recurrence
)



