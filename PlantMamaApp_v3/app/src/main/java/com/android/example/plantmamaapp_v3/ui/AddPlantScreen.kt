package com.android.example.plantmamaapp_v3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.android.example.plantmamaapp_v3.R
import com.android.example.plantmamaapp_v3.ui.theme.PLantMamaTheme
import kotlinx.coroutines.launch

@Composable
fun AddPlant(
    onDismissRequest:() -> Unit,
    onConfirmation:() -> Unit,
    viewModel: PlantEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    val plantUiState = viewModel.plantUiState
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text(text = stringResource(R.string.add_plant), modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.labelLarge)
                //Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_large)))
                AddPlantProfile(painter = painterResource(R.drawable.plant_logo), icon = painterResource(R.drawable.baseline_upload_24), contentDescription = "Adding Plant Profile Pic")
                //Name enter value
                OutlinedTextField(
                    value = plantUiState.plantDetails.name,
                    singleLine = true,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    onValueChange = {viewModel.updateUiState(plantUiState.plantDetails.copy(name = it))},
                    label = { Text("Name*")},
                    isError = false,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {  }
                    )
                )

                //Age enter value
                OutlinedTextField(
                    value = plantUiState.plantDetails.age.toString(),
                    singleLine = true,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    onValueChange = {viewModel.updateUiState(plantUiState.plantDetails.copy(age = it))},
                    label = { Text("Age") },
                    isError = false,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {  }
                    )
                )

                //Type enter value
                OutlinedTextField(
                    value = plantUiState.plantDetails.type,
                    singleLine = true,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    onValueChange = {viewModel.updateUiState(plantUiState.plantDetails.copy(type = it))},
                    label = { Text("Type") },
                    isError = false,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {  }
                    )
                )

                //Description enter value
                OutlinedTextField(
                    value = plantUiState.plantDetails.description,
                    singleLine = false,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    onValueChange = {viewModel.updateUiState(plantUiState.plantDetails.copy(description = it))},
                    label = {Text("Description") },
                    isError = false,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {  }
                    )
                )

                Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_medium)))

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
                            coroutineScope.launch {
                                viewModel.saveItem()
                                onConfirmation()
                            } },
                        modifier = Modifier.padding(8.dp),
                        enabled = plantUiState.isEntryValid
                    ) {
                        Text("Add")
                    }
                }

            }

        }
        
    }

}

@Composable
fun AddPlantProfile(
    painter: Painter,
    icon: Painter,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(70.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Box(modifier = Modifier.height(70.dp).width(70.dp),contentAlignment = Alignment.Center){
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.FillHeight,
            )
            //Icon(painter = icon, contentDescription = "Add Profile Pic")
            Box(modifier = Modifier.height(70.dp).width(70.dp),
                contentAlignment = Alignment.BottomCenter ){
                Icon(painter = icon, contentDescription = "Add Profile Pic")
            }
        }

    }

}

/*
@Preview (showBackground = true)
@Composable
fun displayAddPlant(){
    PLantMamaTheme {
        AddPlant(onDismissRequest = { /*TODO*/ }) {
            
        }
    }
}

 */