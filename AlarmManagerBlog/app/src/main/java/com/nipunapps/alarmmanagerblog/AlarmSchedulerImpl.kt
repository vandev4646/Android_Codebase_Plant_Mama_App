package com.nipunapps.alarmmanagerblog

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.time.ZoneId

//A class that implements the AlarmScheduler Interface
class AlarmSchedulerImpl(
    private val context: Context
) : AlarmScheduler {

    //declare the alarm manager class globally
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("ScheduleExactAlarm")
    override fun schedule(alarmItem: AlarmItem) {
        //intent for sending the alarm to the AlarmReceiver broadcast
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", alarmItem.message)
        }

        val alarmTime = alarmItem.alarmTime.atZone(ZoneId.systemDefault()).toEpochSecond()*1000L

        //AlarmType: RTC_WAKEUP should be set when we want to turn on the screen light when the alarm is triggered
        //TriggerTime: This parameter take trigger time in milliseconds.
        //Pending Intent: This parameter takes a pending intent for the alarm, which means what should happen next when the alarm is triggered.
        //alarmItem.hashcode(): The request code is unique for every alarm, later we can use this request code for cancelling the alarm.
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            PendingIntent.getBroadcast(
                context,
                alarmItem.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        Log.e("Alarm", "Alarm set at $alarmTime")
    }

    //For cacelling the alarm, we just need to call alarmManager.cancel method with same pending Intent request code
    override fun cancel(alarmItem: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmItem.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}