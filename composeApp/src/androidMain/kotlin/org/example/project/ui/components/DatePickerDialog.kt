package org.example.project.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.Instant
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
class DatePickerStateImpl(
    initialDate: LocalDate
) : DatePickerState {
    override var displayMode: DisplayMode = DisplayMode.Picker
        get() = field
        set(value) { field = value }

    override var displayedMonthMillis: Long = initialDate
        .atStartOfDay()
        .toInstant(ZoneOffset.UTC)
        .toEpochMilli()
        get() = field
        set(value) { field = value }

    override val selectableDates: SelectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean = true
    }

    override var selectedDateMillis: Long? = initialDate
        .atStartOfDay()
        .toInstant(ZoneOffset.UTC)
        .toEpochMilli()
        get() = field
        set(value) { field = value }

    override val yearRange: IntRange = 1995..LocalDate.now().year

    private var _selectedDate: LocalDate = initialDate

    fun getSelectedDate(): LocalDate = _selectedDate

    fun updateDate(date: LocalDate) {
        _selectedDate = date
        selectedDateMillis = date
            .atStartOfDay()
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli()
    }

    fun formatDate(): String {
        return _selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = remember { DatePickerStateImpl(LocalDate.now()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("日付を選択") },
        text = {
            DatePicker(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = datePickerState.selectedDateMillis
                ),
                showModeToggle = false
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()
                        datePickerState.updateDate(selectedDate)
                        onDateSelected(selectedDate)
                    }
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("キャンセル")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberDatePickerState(
    initialSelectedDateMillis: Long? = null
): DatePickerState {
    val initialDate = initialSelectedDateMillis?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
    } ?: LocalDate.now()
    
    return remember { DatePickerStateImpl(initialDate) }
} 