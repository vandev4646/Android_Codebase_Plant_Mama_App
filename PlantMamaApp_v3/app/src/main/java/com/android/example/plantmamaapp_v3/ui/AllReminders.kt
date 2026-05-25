package com.android.example.plantmamaapp_v3.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.android.example.plantmamaapp_v3.ui.navigation.NavigationDestination

object AllRemindersDesintation : NavigationDestination {
    override val route = "all_reminders"
}

@Composable
fun AllReminders(){
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Reminders Screen View")
    }
}