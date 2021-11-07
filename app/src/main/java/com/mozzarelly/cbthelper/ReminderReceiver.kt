package com.mozzarelly.cbthelper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.preference.PreferenceManager
import androidx.core.app.NotificationCompat

class ReminderReceiver : BroadcastReceiver(), NotificationSender {
    override fun onReceive(context: Context, intent: Intent) {
        context.sendReadReminderNotification(intent)
    }
}

interface NotificationSender {
    val reminderMessage: String
        get() = "Time to record an entry."

    fun Context.sendReadReminderNotification(intent: Intent?) {
        if (hasAlreadyViewedToday(this)) return

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(
                    "Reminder",
                    "Reminder",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Daily reminder"
                })
        }

        val notification = NotificationCompat.Builder(this, "DailyReader").apply {
            setSmallIcon(android.R.drawable.ic_menu_edit)
            setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.mipmap.ic_launcher2
                )
            )
            setChannelId("Reminder")
            setContentTitle(intent?.getStringExtra("contentTitle") ?: "Daily Reader")
            setContentText(intent?.getStringExtra("contentText") ?: reminderMessage)
            setOngoing(false)
            setPriority(NotificationCompat.PRIORITY_DEFAULT)
            setAutoCancel(true)
            setLights(-0x48e3e4, 1000, 2000)
            setContentIntent(intentToGoToApp(this@sendReadReminderNotification))
        }.build()

        manager.notify(12345, notification)
    }

    private fun hasAlreadyViewedToday(context: Context) = run {
        val lastViewDay = PreferenceManager.getDefaultSharedPreferences(context)
            .getInt("LastView", 0)
        now().dayOfYear() == lastViewDay
    }
}