package com.mozzarelly.cbthelper

import java.text.DateFormat
import java.util.*

data class Time(val hour: Int, val minute: Int){
    fun toLong() = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
    }.timeInMillis

    override fun toString(): String = DateFormat.getTimeInstance(DateFormat.SHORT)
        .format(Calendar.getInstance().apply { time = Date(toLong()) }.time)

    fun toSetting(): String {
        return hour.toString() + ":" + minute.toString().padStart(2, '0')
    }
}