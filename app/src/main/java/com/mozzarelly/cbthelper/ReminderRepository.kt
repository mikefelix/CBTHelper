package com.mozzarelly.cbthelper

import android.app.AlarmManager
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.widget.Toast

const val key = "ReminderTime"

class ReminderRepository(private val context: Context){

    companion object {
        private var instance: ReminderRepository? = null

        fun getInstance(context: Context): ReminderRepository {
            return instance ?: synchronized(this) {
                instance ?: ReminderRepository(context)
                    .also { instance = it }
            }
        }
    }

    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    var reminderTime: Time?
        get() = prefs.get<String>(key)?.toTime()
        set(time) {
            if (time == null) {
                prefs.remove(key)
            }
            else {
                prefs.set(key, time.toSetting())
                setAlarm(time)
                Toast.makeText(context, "Reminder set for $time.", Toast.LENGTH_SHORT).show()
            }
        }

    fun setAlarm(time: Time? = null){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        (time ?: prefs.get<String>(key)?.toTime())?.let {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC,
                it.toLong(),
                1000L * 60 * 60 * 24,
                intentToSendReminderNotification(context, "CBT Helper", "Reminder text")
            )
        }
    }
}
