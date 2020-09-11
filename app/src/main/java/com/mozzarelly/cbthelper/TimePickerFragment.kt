package com.mozzarelly.cbthelper

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

class TimePickerFragment(private val _context: Context) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private val time: Time = ReminderRepository.getInstance(_context).reminderTime ?: Time(10, 0)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = requireContext()
        val (hour, minute) = time

        return TimePickerDialog(
            context, android.R.style.Theme_Material_Light_Dialog_Alert, this,
            hour, minute, DateFormat.is24HourFormat(activity)
        ).apply {
            setButton(DialogInterface.BUTTON_POSITIVE, "Remind", this)
        }
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val time = Time(hourOfDay, minute)
        ReminderRepository.getInstance(_context).reminderTime = time
    }

}
