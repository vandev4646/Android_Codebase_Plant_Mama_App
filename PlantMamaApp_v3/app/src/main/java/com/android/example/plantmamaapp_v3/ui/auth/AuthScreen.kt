package com.android.example.plantmamaapp_v3.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.example.plantmamaapp_v3.ui.AppViewModelProvider
import com.android.example.plantmamaapp_v3.ui.navigation.NavigationDestination

object AuthScreenDesintation : NavigationDestination {
    override val route = "auth_screen"
}

@Composable
fun AuthScreen(
    onAuthSucess: () -> Unit,
    viewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val authResult by viewModel.authResult.collectAsState()

    LaunchedEffect(authResult) {
        if(authResult is AuthResult.Sucess) onAuthSucess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if(viewModel.isSignUpMode) "Create Account" else "Welcome Back",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = {viewModel.email = it},
            label = {Text("Email Address")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = {viewModel.password= it},
            label = {Text("Password")},
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        when(authResult){
            is AuthResult.Loading -> CircularProgressIndicator()
            is AuthResult.Error -> {
                Text(
                    text = (authResult as AuthResult.Error).theMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            else -> {}
        }

        Button(
            onClick = {viewModel.handleAuthAction()},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (viewModel.isSignUpMode) "Sign Up" else "Log In")
        }

        TextButton(
            onClick = {viewModel.isSignUpMode = !viewModel.isSignUpMode}
        ) {
            Text(
                if (viewModel.isSignUpMode) "Already have an Account? Log In"
                else "Don't have an account? Sign Up"
            )
        }
    }

}