package com.android.example.plantmamaapp_v3.ui

import android.icu.util.Calendar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.example.plantmamaapp_v3.R
import com.android.example.plantmamaapp_v3.data.ReminderWM
import kotlinx.coroutines.launch
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale
import java.util.concurrent.TimeUnit

private var totalDelay: Int = 0

@Composable
fun AddReminder(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    viewModel: PlantMamaMainScreenViewModel,
    reminderViewModel: ReminderEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val reminderUiState = reminderViewModel.reminderUiState

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
    ) {

        Column(
            modifier = Modifier
        ) {
            Text(
                text = stringResource(R.string.add_reminder),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.labelLarge
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            OutlinedTextField(
                value = reminderUiState.reminderDetails.title,
                singleLine = true,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth(0.9f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                ),
                onValueChange = {
                    reminderViewModel.updateUiState(
                        reminderUiState.reminderDetails.copy(
                            title = it
                        )
                    )
                },
                label = {
                    Text("Reminder Name*")
                },
                isError = false,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { }
                )
            )
            Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_medium)))

            DateTimePickerComponent()

            Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_medium)))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                TextButton(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text("Cancel")
                }
                TextButton(
                    onClick = {
                        val reminderIdentifier =
                            viewModel.currentPlant.name + currentTimeMillis().toString()
                        val reminderWM = ReminderWM(
                            duration = totalDelay.toLong(),
                            unit = TimeUnit.SECONDS,
                            plantName = viewModel.currentPlant.name,
                            reminderIdentifier = reminderIdentifier
                        )
                        reminderViewModel.updateUiState(
                            reminderUiState.reminderDetails.copy(
                                wmIdentifier = reminderIdentifier
                            )
                        )
                        reminderViewModel.updateUiState(reminderUiState.reminderDetails.copy(plantID = viewModel.currentPlant.id.toString()))
                        viewModel.scheduleReminder(reminderWM)
                        coroutineScope.launch {
                            reminderViewModel.saveItem()
                        }
                        onConfirmation()
                    },
                    modifier = Modifier.padding(8.dp),
                    enabled = reminderUiState.isEntryValid
                ) {
                    Text("Add")
                }
            }
        }
    }
}


@Composable
fun DateTimePickerComponent(reminderViewModel: ReminderEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    //shores the date chosen
    val date = remember {
        Calendar.getInstance().apply {
        }.timeInMillis
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date,
        yearRange = 2024..2030,
    )

    //Variables for showing statues
    var showDatePicker by remember { mutableStateOf(false) }
    var dateSelected by remember { mutableStateOf(false) }


    var showAdvancedExample by remember { mutableStateOf(false) }
    var selectedTime: TimePickerState? by remember { mutableStateOf(null) }

    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val dateFormatter = remember { SimpleDateFormat("MMM dd yyyy", Locale.getDefault()) }

    val yearFormatter = remember { SimpleDateFormat("yyyy", Locale.getDefault()) }
    val monthFormatter = remember { SimpleDateFormat("MM", Locale.getDefault()) }
    val dayFormatter = remember { SimpleDateFormat("dd", Locale.getDefault()) }

    val currentYear = LocalDate.now().year
    val currentMonth = LocalDate.now().monthValue
    val currentDay = LocalDate.now().dayOfMonth
    val currentHour = LocalTime.now().hour
    val currentMin = LocalTime.now().minute

    var delayYearS: Int = 0
    var delayMonthS: Int = 0
    var delayDayS: Int = 0
    var delayHourS: Int = 0
    var delayMinS: Int = 0

    var pastDate by remember {
        mutableStateOf(false)
    }

    var pastTime by remember {
        mutableStateOf(true)
    }

    val reminderUiState = reminderViewModel.reminderUiState

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.75f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Row {
            Text(text = "Reminder Date", modifier = Modifier.padding(16.dp))

            Button(
                onClick = {
                    if (pastDate) {
                        totalDelay -= delayYearS - delayMonthS - delayDayS
                    }
                    showDatePicker = true //changing the visibility state
                },
                modifier = Modifier.fillMaxWidth(0.5f),
            ) {
                if (dateSelected) {
                    Text(text = dateFormatter.format(datePickerState.selectedDateMillis))
                } else
                    Text(text = "Choose Date")
            }

        }

        Divider(modifier = Modifier.padding(vertical = 24.dp))

        Row {
            Text(text = "Reminder Time", modifier = Modifier.padding(16.dp))

            Button(
                onClick = {
                    if (pastTime) {
                        totalDelay -= delayHourS - delayMinS
                    }
                    showAdvancedExample = true
                },
                modifier = Modifier.fillMaxWidth(0.5f),
            ) {
                if (selectedTime != null) {
                    delayHourS =
                        ((selectedTime!!.hour - currentHour) * 3600.4566211200049111).toInt()
                    delayMinS = (selectedTime!!.minute - currentMin) * 60
                    totalDelay += delayHourS + delayMinS
                    val cal = java.util.Calendar.getInstance()
                    cal.set(java.util.Calendar.HOUR_OF_DAY, selectedTime!!.hour)
                    cal.set(java.util.Calendar.MINUTE, selectedTime!!.minute)
                    cal.isLenient = false
                    reminderViewModel.updateUiState(
                        reminderUiState.reminderDetails.copy(
                            time = formatter.format(
                                cal.time
                            )
                        )
                    )
                    Text(formatter.format(cal.time))
                } else
                    Text(text = "Choose Time")
            }
        }
    }

    // date picker component
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { /*TODO*/ },
            confirmButton = {
                TextButton(
                    onClick = {
                        delayYearS = ((yearFormatter.format(datePickerState.selectedDateMillis)
                            .toInt() - currentYear) * 3.154e+7).toInt()
                        delayMonthS = ((monthFormatter.format(datePickerState.selectedDateMillis)
                            .toInt() - currentMonth) * 2628336.2137829).toInt()
                        delayDayS = ((dayFormatter.format(datePickerState.selectedDateMillis)
                            .toInt() - currentDay) * 86410.958906880114228).toInt()

                        if ((dateSelected)) {
                            totalDelay += delayHourS + delayMinS

                        }
                        pastDate = true
                        dateSelected = true
                        reminderViewModel.updateUiState(
                            reminderUiState.reminderDetails.copy(
                                date = dateFormatter.format(
                                    datePickerState.selectedDateMillis
                                )
                            )
                        )
                        showDatePicker = false

                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) { Text("Cancel") }
            }
        )
        {
            DatePicker(state = datePickerState)
        }
    }


    when {
        showAdvancedExample -> AdvancedTimePickerExample(
            onDismiss = { showAdvancedExample = false },
            onConfirm = { time ->
                selectedTime = time
                showAdvancedExample = false
            },
        )
    }

}


