package com.android.example.plantmamaapp_v3.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.example.plantmamaapp_v3.R
import com.android.example.plantmamaapp_v3.data.Recurrence
import com.android.example.plantmamaapp_v3.data.ReminderWM
import kotlinx.coroutines.launch
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
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

    var expanded by remember { mutableStateOf(false) }
    var selectedRecurrence by remember { mutableStateOf(Recurrence.ONCE) }
    val recurrenceOptions = Recurrence.values().toList()

    CheckReminderPermission()
    reminderViewModel.updateUiState(reminderUiState.reminderDetails.copy(plantID = viewModel.currentPlant.id.toString()))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(650.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
    ) {

        Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_large)))


        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.add_reminder),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )

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
                    if (it.isNotBlank()) {
                        reminderViewModel.updateUiState(
                            reminderUiState.reminderDetails.copy(
                                title = it
                            )
                        )
                    }

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
            Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_small)))

            DateTimePickerComponent()

            // Dropdown menu for recurrence selection
            Box {
                OutlinedTextField(
                    value = selectedRecurrence.name,
                    onValueChange = {},
                    label = { Text("Recurrence") },
                    modifier = Modifier.fillMaxWidth(0.9f),
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            Modifier.clickable { expanded = true })
                    })
                DropdownMenu(expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    recurrenceOptions.forEach { option ->
                        DropdownMenuItem(onClick = {
                            selectedRecurrence = option
                            expanded = false
                        }, text = {Text(text = option.name)} )
                    }
                }
            }


            Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_small)))

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
                        var reminderWM = ReminderWM(
                            duration = totalDelay.toLong(),
                            unit = TimeUnit.MINUTES,
                            plantName = viewModel.currentPlant.name,
                            reminderTitle = reminderUiState.reminderDetails.title,
                            reminderIdentifier = reminderIdentifier,
                            recurrence = selectedRecurrence
                        )

                        reminderWM.reminderIdentifier = reminderIdentifier
                        reminderViewModel.updateUiState(reminderUiState.reminderDetails.copy(plantID = viewModel.currentPlant.id.toString()))
                        reminderViewModel.updateUiState(
                            reminderUiState.reminderDetails.copy(
                                wmIdentifier = reminderIdentifier
                            )
                        )

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

    fun getCurrentDateInMillisUTC(): Long {
        val currentDate = LocalDate.now(ZoneOffset.UTC)
        return currentDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    }

    fun formatDateFromMillisUTC(millis: Long): String {
        val date = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
        val formatter = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.getDefault())
        return date.format(formatter)
    }

    val initialSelectedDateMillis = getCurrentDateInMillisUTC()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = getCurrentDateInMillisUTC(),
        yearRange = 2024..2030,
    )

    //Variables for showing statues
    var showDatePicker by remember { mutableStateOf(false) }
    var dateSelected by remember { mutableStateOf(false) }


    var showAdvancedExample by remember { mutableStateOf(false) }
    var selectedTime: TimePickerState? by remember { mutableStateOf(null) }

    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

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
            .fillMaxHeight(0.55f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Row {
            Text(text = "Reminder Date", modifier = Modifier.padding(16.dp))

            Button(
                onClick = {
                    if (pastDate) {
                        totalDelay = delayYearS - delayMonthS - delayDayS
                        delayYearS = 0
                        delayMonthS = 0
                        delayDayS = 0
                    }
                    showDatePicker = true //changing the visibility state
                },
                modifier = Modifier.fillMaxWidth(0.5f),
            ) {
                if (dateSelected) {
                    Text(
                        text = formatDateFromMillisUTC(
                            datePickerState.selectedDateMillis ?: initialSelectedDateMillis
                        )
                    )
                } else
                    Text(text = "Choose Date")
            }

        }

        Divider(modifier = Modifier.padding(vertical = 24.dp))

        Row {
            Text(text = "Reminder Time", modifier = Modifier.padding(16.dp))

            Button(
                onClick = {
                    Log.d("DEBUG", "Button clicked")
                    if (pastTime) {
                        totalDelay -= delayHourS - delayMinS
                        delayHourS = 0
                        delayMinS = 0
                    }
                    showAdvancedExample = true
                },
                modifier = Modifier.fillMaxWidth(0.5f),
            ) {
                if (selectedTime != null) {

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
                            .toInt() - currentYear) * 525600).toInt()
                        delayMonthS = ((monthFormatter.format(datePickerState.selectedDateMillis)
                            .toInt() - currentMonth) * 43800).toInt()
                        delayDayS = ((dayFormatter.format(datePickerState.selectedDateMillis)
                            .toInt() - (currentDay - 1)) * 1440).toInt()

                        // if ((dateSelected)) {
                        totalDelay += delayDayS + delayMonthS + delayYearS
                        delayYearS = 0
                        delayMonthS = 0
                        delayDayS = 0

                        // }
                        pastDate = true
                        dateSelected = true
                        reminderViewModel.updateUiState(
                            reminderUiState.reminderDetails.copy(
                                date = formatDateFromMillisUTC(
                                    datePickerState.selectedDateMillis ?: initialSelectedDateMillis
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
                delayHourS =
                    ((selectedTime!!.hour - currentHour) * 60).toInt()
                delayMinS = (selectedTime!!.minute - currentMin)

                totalDelay += ((delayHourS + delayMinS))

                delayHourS = 0
                delayMinS = 0
                showAdvancedExample = false
            },
        )
    }

}

//Copilot suggested this code.
@Composable
fun CheckReminderPermission() {
    val context = LocalContext.current
    var hasNotificationPermission by remember { mutableStateOf(false) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasNotificationPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )) {
                PackageManager.PERMISSION_GRANTED -> {
                    hasNotificationPermission = true
                }

                else -> {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            hasNotificationPermission = true
        }
    }

    if (hasNotificationPermission) {
        // Your code to send notifications
        Text("Notification permission granted")
    } else {
        Text("Requesting notification permission...")
    }
}








