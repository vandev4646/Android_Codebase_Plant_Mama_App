package com.android.example.plantmamaapp_v3.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.xr.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.xr.compose.material3.ExperimentalMaterial3XrApi

data class ToolbarItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3XrApi::class)
@Composable
fun PlantBottomBar(
    navController: NavController,
    onAddPlantClick: () -> Unit,
    modifier: Modifier = Modifier
){
    val items = listOf(
        ToolbarItem("Home", Icons.Filled.Home, PlantMamaHomeDesintation.route),
        ToolbarItem("Reminders", Icons.Filled.Notifications, AllRemindersDesintation.route),
        ToolbarItem("Photos", Icons.Filled.PhotoLibrary, AllPhotosDesintation.route),
        ToolbarItem("Add Plant", Icons.Filled.Add, "add_plant")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        modifier = modifier
            .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 4.dp,
        color = Color.Transparent // Surface passes coloring responsibility to NavigationBar
    ) {
        NavigationBar(
            modifier = Modifier.height(64.dp),
            containerColor = Color.White.copy(alpha = 0.85f),
            tonalElevation = 0.dp,
            windowInsets = WindowInsets(0.dp)
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    onClick = {
                        if (item.route == "add_plant") {
                            onAddPlantClick() // Open dialog instead of navigating
                        } else if (currentRoute != item.route) {
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
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.size(24.dp) // Enforces a cleaner, larger size
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontSize = 11.sp, // Shrinks the text down cleanly
                            maxLines = 1
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.DarkGray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.DarkGray,
                        indicatorColor = Color.Transparent // Removes the default material pill background highlight
                    )
                )
            }
        }
    }
    /*
    Surface(
        modifier = modifier
            .padding(start = 12.dp, end = 12.dp, bottom = 12.dp, top = 32.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color.Transparent,
        shadowElevation = 4.dp
    ){
        HorizontalFloatingToolbar(
            expanded = true,
            colors = FloatingToolbarDefaults.vibrantFloatingToolbarColors()
        ){
            items.forEach { item ->
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp))
                    Text(
                        text = item.title,
                        fontSize = 11.sp, // Shrinks the text down cleanly
                        maxLines = 1
                    )
                }
            }
        }
    }
    */
}