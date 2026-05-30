package com.android.example.plantmamaapp_v3.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.example.plantmamaapp_v3.R
import kotlinx.coroutines.launch

data class InfoGroup (
    val header: String,
    val icon: ImageVector,
    val details: String
)



@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InfoDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
){

    val items = listOf(
        InfoGroup("Your Data is Yours", Icons.Filled.Shield, "No information is collected. Everything is stored on your device."),
        InfoGroup("Photo Storage", Icons.Filled.PhotoLibrary, "Photos are stored in a 'Plant Mama' album. Even if the app is deleted, your photos stay in your library."),
        InfoGroup("Important", Icons.Outlined.Warning, "If you delete the app your plant records are permanently lost, as we do not use cloud storage.")
    )

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                //header
                Text(
                    text = "About Plant Mama",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Created by a plant mama, for plant mamas.",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "This app was built to help you track your plant's growth and stay on top of daily care.",
                    style = MaterialTheme.typography.bodyMedium, // Swapped from rigid emphasized labels
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.0.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(20.dp) // Auto spaces rows
                ) {
                    items.forEach { item ->
                        InfoRow(item.header, item.icon, item.details)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Done")
                }
            }
        }
    }
}

@Composable
fun InfoRow(header: String, icon: ImageVector, details: String){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp) // Spaces icon from text column
    ) {
        Icon(
            imageVector = icon,
            contentDescription = header,
            tint = MaterialTheme.colorScheme.primary, // Adds brand color to icons
            modifier = Modifier.size(24.dp)
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp) // Spaces header from description
        ) {
            Text(
                text = header,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = details,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}