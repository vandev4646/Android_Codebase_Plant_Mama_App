package com.android.example.plantmamaapp_v3.worker


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.android.example.plantmamaapp_v3.CHANNEL_ID
import com.android.example.plantmamaapp_v3.MainActivity
import com.android.example.plantmamaapp_v3.NOTIFICATION_ID
import com.android.example.plantmamaapp_v3.NOTIFICATION_TITLE
import com.android.example.plantmamaapp_v3.R
import com.android.example.plantmamaapp_v3.REQUEST_CODE
import com.android.example.plantmamaapp_v3.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
import com.android.example.plantmamaapp_v3.VERBOSE_NOTIFICATION_CHANNEL_NAME
import com.android.example.plantmamaapp_v3.data.Reminder
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit


fun makePlantReminderNotification(
    message: String,
    title: String,
    context: Context
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(
            CHANNEL_ID,
            VERBOSE_NOTIFICATION_CHANNEL_NAME,
            importance
        )
        channel.description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }

    val pendingIntent: PendingIntent = createPendingIntent(context)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    // Generate a unique notification ID
    val notificationId = (System.currentTimeMillis() % 10000).toInt()
    NotificationManagerCompat.from(context).notify(notificationId, builder.build())
}

fun createPendingIntent(appContext: Context): PendingIntent {
    val intent = Intent(appContext, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    // Flag to detect unsafe launches of intents for Android 12 and higher
    // to improve platform security
    var flags = PendingIntent.FLAG_UPDATE_CURRENT
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        flags = flags or PendingIntent.FLAG_IMMUTABLE
    }

    return PendingIntent.getActivity(
        appContext,
        REQUEST_CODE,
        intent,
        flags
    )
}

//reschedule reminders based on information from firebase
fun scheduleReminderWork(context: Context, reminder: Reminder, plantName: String) {
    // Calculate the initial delay based on your saved string date and time
    val dateTimeString = "${reminder.date} ${reminder.time}"
    val sdf =
        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) // Adjust format to match your app
    val reminderTimeInMillis = sdf.parse(dateTimeString)?.time ?: return
    val delay = reminderTimeInMillis - System.currentTimeMillis()

    // Skip scheduling if the date/time is already in the past
    if (delay <= 0) return

    val data = Data.Builder()
        .putString(WaterReminderWorker.nameKey, plantName)
        .putString(WaterReminderWorker.reminderTitle, reminder.title)
        .build()

    //Build the work request
    val reminderWorkRequest = OneTimeWorkRequestBuilder<WaterReminderWorker>()
        .setInputData(data)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .build()

    //Enqueue as UNIQUE work using saved wmIdentifier
    WorkManager.getInstance(context).enqueueUniqueWork(
        reminder.wmIdentifier,
        ExistingWorkPolicy.KEEP,
        reminderWorkRequest
    )
}
