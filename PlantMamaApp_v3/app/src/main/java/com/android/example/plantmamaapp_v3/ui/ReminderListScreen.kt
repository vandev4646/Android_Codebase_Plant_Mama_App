package com.android.example.plantmamaapp_v3.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.WorkManager
import com.android.example.plantmamaapp_v3.R
import com.android.example.plantmamaapp_v3.data.Reminder
import kotlinx.coroutines.launch

@Composable
fun ReminderListScreen(
    reminderListiewModel: ReminderListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    ) {
    //for reminder list display
    val reminderListUiState by reminderListiewModel.reminderListUiState.collectAsState()
    val reminderList = reminderListUiState.reminderList


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier,
    ) {
        if (reminderList.isEmpty()) {
            Text(
                text = "Oops! No reminders are added yet for this plant. Tap the reminder icon above to add a reminder",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier,
            )
        } else {
            ReminderList(
                reminderList = reminderList,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small)),

            )
        }
    }
}

@Composable
private fun ReminderList(
    reminderList: List<Reminder>,
    modifier: Modifier = Modifier,
    deleteReminderViewModel: DeleteReminderViewModel = viewModel(factory = AppViewModelProvider.Factory),

    ) {
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier,
    ) {
        items(items = reminderList, key = { it.id }) { item ->
            ReminderItem(reminder = item,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small)),
                onClick = {

                        coroutineScope.launch {
                            deleteReminderViewModel.deleteReminder(item)
                            deleteReminderViewModel.waterRepository.deleteReminder(item.wmIdentifier)
                        }

                })
        }
    }
}




@Composable
private fun ReminderItem(
    reminder: Reminder,
    modifier: Modifier = Modifier,
    onClick:() -> Unit = {}
) {

    Card(
        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            var expanded by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))

                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = { onClick()
                                  },
                        leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null) }
                    )
                }
            }
            Row (
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = reminder.date,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = reminder.time,
                    style = MaterialTheme.typography.titleMedium
                )

            }

        }

    }
}