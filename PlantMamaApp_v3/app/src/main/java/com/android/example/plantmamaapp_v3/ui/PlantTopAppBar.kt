package com.android.example.plantmamaapp_v3.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.example.plantmamaapp_v3.R

/**
 * Composable that displays a Top Bar with an icon and text.
 *
 * @param modifier modifiers to set to this composable
 */
@Composable
fun PlantTopAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
                Text(
                    text = "Plant Mama",
                    style = MaterialTheme.typography.displaySmall
                )
        },

        modifier = modifier.padding(8.dp, 16.dp, 8.dp, 12.dp),
        colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
        actions = {
                IconButton(onClick = {expanded = !expanded}) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Side menu"
                    )
                }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Home") },
                    leadingIcon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    onClick = { }
                )
                DropdownMenuItem(
                    text = { Text("All Reminders") },
                    leadingIcon = { Icon(Icons.Filled.Notifications, contentDescription = "All Remidners") },
                    onClick = {  }
                )
                DropdownMenuItem(
                    text = { Text("All Photos") },
                    leadingIcon = { Icon(Icons.Filled.PhotoLibrary, contentDescription = "All Photos") },
                    onClick = {  }
                )
                DropdownMenuItem(
                    text = { Text("Add New Plant") },
                    leadingIcon = { Icon(Icons.Filled.Add, contentDescription = "Add Plant") },
                    onClick = {  }
                )
                DropdownMenuItem(
                    text = { Text("Info") },
                    leadingIcon = { Icon(Icons.Filled.Info, contentDescription = "Info") },
                    onClick = {  }
                )

            }
        }
    )

}