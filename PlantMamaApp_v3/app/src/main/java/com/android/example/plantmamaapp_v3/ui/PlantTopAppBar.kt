package com.android.example.plantmamaapp_v3.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
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
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.android.example.plantmamaapp_v3.R

/**
 * Composable that displays a Top Bar with an icon and text.
 *
 * @param modifier modifiers to set to this composable
 */
@Composable
fun PlantTopAppBar(
    navController: NavController,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onAddPlantClick: () -> Unit,
    onInfoClick: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {

    val items = listOf(
        ToolbarItem("Home", Icons.Filled.Home, PlantMamaHomeDesintation.route),
        ToolbarItem("All Reminders", Icons.Filled.Notifications, AllRemindersDesintation.route),
        ToolbarItem("All Photos", Icons.Filled.PhotoLibrary, AllPhotosDesintation.route),
        ToolbarItem("Add New Plant", Icons.Filled.Add, "add_plant"),
                ToolbarItem("Info", Icons.Filled.Info, "info"),
        ToolbarItem("Sign Out", Icons.AutoMirrored.Filled.Logout, "sign_out")
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
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
                items.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = { Text(item.title) },
                        leadingIcon = { Icon(item.icon, contentDescription = item.title) },
                        onClick = {
                            if (item.route == "add_plant") {
                                onAddPlantClick()
                            } else if (item.route == "info"){
                                onInfoClick()
                            } else if(item.route == "sign_out")
                                onSignOut()
                            else if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    // Pop up to the start destination to avoid building up a massive stack
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when reselecting
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        }
                    )
                }

            }
        }
    )

}