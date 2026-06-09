package com.android.example.plantmamaapp_v3.ui

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.example.plantmamaapp_v3.R
import com.android.example.plantmamaapp_v3.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object PlantEditDestination : NavigationDestination {
    override val route = "item_edit"
    val titleRes = "Edit Plant"
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    navigateHome: () -> Unit,
    onProfilePicClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlantEditViewModel = viewModel(factory = AppViewModelProvider.Factory),
    viewModel2: MainScreenViewModel,
    viewModel3: PhotoViewModel = viewModel(factory = AppViewModelProvider.Factory)
){


    val coroutineScope = rememberCoroutineScope()
    val plantUiState = viewModel.plantUiState
    //val profileUiState by viewModel.plantFlow.collectAsStateWithLifecycle()
    //val currentUri = Uri.parse(profileUiState.plant.profilePic)
    if(!viewModel2.newProfilePicSelected){
        viewModel2.currentUri = Uri.parse(viewModel2.currentPlant.profilePic)
    }
    val profileOrDefault = remember { viewModel2.currentUri }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = plantUiState.plantDetails.datePurchased
    )

    if (showDatePicker){
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false},
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { selectedTime ->
                        viewModel.updateUiState(
                            plantUiState.plantDetails.copy(datePurchased = selectedTime)
                        )
                    }
                    showDatePicker = false
                }) {
                    Text("Ok")
                }

            },
            dismissButton = {
                TextButton(onClick = {showDatePicker = false}) {
                    Text("Cancel")
                }
            }
        ){
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = PlantEditDestination.titleRes,
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->

        Column (modifier = Modifier
            .padding(
                start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                top = innerPadding.calculateTopPadding(),
                end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
            ),
                horizontalAlignment = Alignment.CenterHorizontally){
            if (profileOrDefault.equals(Uri.EMPTY)) {
                AddPlantProfile(
                    uri = "",
                    icon = painterResource(R.drawable.baseline_upload_24),
                    onProfilePicClick
                )
            }
            if (!profileOrDefault.equals(Uri.EMPTY)) {
                AddPlantProfile(
                    uri = viewModel2.currentUri.toString(),
                    icon = painterResource(R.drawable.baseline_upload_24),
                    onProfilePicClick
                )
                val newUri = viewModel3.generateNewUri(viewModel2.currentUri)
                viewModel.updateUiState(plantUiState.plantDetails.copy(profilePic = newUri))
            }

            PlantEntryBody(
                plantUiState = viewModel.plantUiState,
                onItemValueChange = viewModel::updateUiState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.updatePlant()
                        navigateBack()
                    }
                },
                onDeleteClick = {
                    coroutineScope.launch {
                        viewModel.deletePlant()
                        navigateHome()
                    }

                },

                modifier = Modifier
                    .padding(),
                onClickDate = {showDatePicker = true}
            )
        }

    }
}

@Composable
fun PlantEntryBody(
    plantUiState: PlantUiState,
    onItemValueChange: (PlantDetails) -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    onClickDate: () -> Unit
) {


    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PlantInputForm(
            itemDetails = plantUiState.plantDetails,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth(),
            onClickDate = onClickDate
        )
        Button(
            onClick = onSaveClick,
            enabled = plantUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Edit")
        }

        Button(
            onClick = onDeleteClick,
            enabled = plantUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Delete")
        }
    }
}

@Composable
fun PlantInputForm(
    itemDetails: PlantDetails,
    modifier: Modifier = Modifier,
    onValueChange: (PlantDetails) -> Unit = {},
    onClickDate: () -> Unit,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        OutlinedTextField(
            value = itemDetails.name,
            onValueChange = {
                onValueChange(itemDetails.copy(name = it))
                            },
            label = { Text("Name*") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        Box(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onClickDate),
        ){
            OutlinedTextField(
                value = formatLongToDateString(itemDetails.datePurchased),
                onValueChange = {  },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {Text("Date Purchased") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
                singleLine = true,
                readOnly = true
            )
            //this is needed to make the text field uneditable
            Box(
                modifier = Modifier.matchParentSize().clickable(onClick = onClickDate)
            )
        }

        OutlinedTextField(
            value = itemDetails.type,
            onValueChange = { onValueChange(itemDetails.copy(type = it)) },
            label = { Text("Type") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = itemDetails.description,
            onValueChange = { onValueChange(itemDetails.copy(description = it)) },
            label = { Text("Notes") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        if (enabled) {
            Text(
                text = "*required fields",
                modifier = Modifier.padding(start = 20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "back"
                    )
                }
            }
        }
    )
}

@Composable
fun profilePic(
    plantDetails: PlantDetails,
    onItemValueChange: (PlantDetails) -> Unit,
    onProfilePicClick: () -> Unit,
    viewModel: MainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
){

    val newUri by remember{ mutableStateOf(generateNewUri(viewModel.currentUri)) }
        AddPlantProfile(
            uri = plantDetails.profilePic,
            icon = painterResource(R.drawable.baseline_upload_24),
            {
                //viewModel.cameraForProfile = true
                onProfilePicClick()
                onItemValueChange(plantDetails.copy(profilePic = newUri))
            }
        )
}

fun generateNewUri(uri: Uri): String {
    var newUri = uri.toString()
    if (newUri.contains("picker")) {
        newUri = "content://media/external/images/media/" + extractNumbers(uri.toString())
    }
    return newUri
}

fun extractNumbers(input: String): String {
    val regex = "\\d+".toRegex()
    val matchResults = regex.findAll(input).toList()
    return matchResults.lastOrNull()?.value ?: ""
}

