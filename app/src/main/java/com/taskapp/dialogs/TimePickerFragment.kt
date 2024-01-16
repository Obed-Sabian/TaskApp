package com.taskapp.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.taskapp.R
import java.util.*

class TimePickerFragment(val listener: (hour:Int, minute:Int) -> Unit): DialogFragment(), TimePickerDialog.OnTimeSetListener {
    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        listener(hour, minute)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        return TimePickerDialog(activity as Context, R.style.timePickerTheme, this, hour, minute, false)
    }
}