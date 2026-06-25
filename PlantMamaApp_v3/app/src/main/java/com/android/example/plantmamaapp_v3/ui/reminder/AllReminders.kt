package com.android.example.plantmamaapp_v3.ui.reminder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.android.example.plantmamaapp_v3.R
import com.android.example.plantmamaapp_v3.ui.AppViewModelProvider
import com.android.example.plantmamaapp_v3.ui.navigation.NavigationDestination

object AllRemindersDesintation : NavigationDestination {
    override val route = "all_reminders"
}

@Composable
fun AllReminders(modifier: Modifier = Modifier, navController: NavController){

    val allReminderViewModel: AllReminderViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val allReminderUiState by allReminderViewModel.allReminderUiState.collectAsState()
    val reminderList = allReminderUiState.reminderList
    val noReminderMessage = "No Reminders. To add a new reminder, navigate to a plant profile. Then click the reminder icon on the right sidebar to add reminders for that plant"

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier,
        ) {
            if (reminderList.isEmpty()) {
                Text(
                    text = noReminderMessage,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier,
                )
            } else {
                Box(Modifier.fillMaxSize().padding(top = 16.dp), contentAlignment = Alignment.TopStart) {
                    ReminderList(
                        reminderList = reminderList,
                        modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small)),

                        )
                }
            }
        }
    }
}