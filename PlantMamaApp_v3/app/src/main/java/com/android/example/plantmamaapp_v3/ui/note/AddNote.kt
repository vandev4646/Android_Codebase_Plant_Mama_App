package com.android.example.plantmamaapp_v3.ui.note

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.android.example.plantmamaapp_v3.data.Photo
import com.android.example.plantmamaapp_v3.ui.AppViewModelProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun AddNote(
    plantId: Int,
    plantGalleryPhotos: List<Photo>,
    onNavigateBack: () -> Unit,
    viewModel: NoteViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.noteUiState
    var showDatePicker by remember { mutableStateOf(false) }
    val dateInteractionSource = remember { MutableInteractionSource() }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.noteDetails.date
    )

    //date picker dialog
    if(showDatePicker){
        DatePickerDialog(
            onDismissRequest = {showDatePicker = false},
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let{
                            timestamp ->
                            viewModel.updateUiState(uiState.noteDetails.copy(date = timestamp))
                        }
                        showDatePicker = false
                    }) { Text("OK")}
            },
            dismissButton = {
                TextButton(onClick = {showDatePicker = false}) { Text("Cancel") }
            }
        ) {DatePicker(state = datePickerState) }
    }

    //UI Screen
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Plant Note")},
                navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                },
                actions = {
                    IconButton(
                        onClick = {viewModel.saveNote(plantId, onNavigateBack)},
                        enabled = uiState.isEntryValid
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Add Note")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement =  Arrangement.spacedBy(16.dp)
        ) {
            //Note Details
            OutlinedTextField(
                value = uiState.noteDetails.title,
                onValueChange = {viewModel.updateUiState(uiState.noteDetails.copy(title = it))},
                label = {Text("Note Details *")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            //Note Date
            val formattedNoteDate = remember (uiState.noteDetails.date){
                val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                formatter.format(Date(uiState.noteDetails.date))
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(interactionSource = dateInteractionSource,
                        indication = null) { showDatePicker = true }
            ){
                OutlinedTextField(
                    value = formattedNoteDate,
                    onValueChange = {},
                    label = {Text("Note Date")},
                    readOnly = true,
                    enabled = true,
                )
                //this is needed to make the text field uneditable
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { showDatePicker = true }
                )
            }


            //Photo gallery
            Text(
                text = "Attach Photos from Gallery",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            if(plantGalleryPhotos.isEmpty()){
                Text(
                    text = "No photos added to this plant profile yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ){
                    items(plantGalleryPhotos){ photo ->
                        val isSelected = uiState.selectedPhotos.contains(photo)

                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable{ viewModel.togglePhotoSelection(photo)}
                        ){
                            AsyncImage(
                                model = photo.uri,
                                contentDescription = "Plant gallery image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            if(isSelected){
                                Surface(
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.TopEnd)
                                        .padding(2.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }

    }


}