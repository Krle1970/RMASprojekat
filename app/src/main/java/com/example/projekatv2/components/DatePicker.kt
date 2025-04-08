package com.example.projekatv2.screens.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(selectedDate: MutableState<LocalDate>) {
    val context = LocalContext.current

    val year = selectedDate.value.year
    val month = selectedDate.value.monthValue - 1
    val day = selectedDate.value.dayOfMonth

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            selectedDate.value = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
        },
        year, month, day
    )

    Column(modifier = Modifier.fillMaxWidth().clickable {
        datePickerDialog.show()
    }) {
        Text(text = "Izaberi datum")
        Text(text = selectedDate.value.toString())
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimePicker(selectedTime: MutableState<LocalTime>) {
    val context = LocalContext.current

    val hour = selectedTime.value.hour
    val minute = selectedTime.value.minute

    val timePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            selectedTime.value = LocalTime.of(selectedHour, selectedMinute)
        },
        hour, minute, true
    )

    Column(modifier = Modifier.fillMaxWidth().clickable {
        timePickerDialog.show()
    }) {
        Text(text = "Izaberi vreme")
        Text(text = selectedTime.value.toString())
    }
}
