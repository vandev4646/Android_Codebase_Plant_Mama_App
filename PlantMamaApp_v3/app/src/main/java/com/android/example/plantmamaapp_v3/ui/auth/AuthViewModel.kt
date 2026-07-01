package com.android.example.plantmamaapp_v3.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.plantmamaapp_v3.data.SyncRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(
    private val syncRepository: SyncRepository
): ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isSignUpMode by mutableStateOf(false)

    private val _authResult = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val authResult: StateFlow<AuthResult> = _authResult

    init {
        checkExistingSession()
    }

    private fun checkExistingSession(){
        if (auth.currentUser != null){
            _authResult.value = AuthResult.Sucess
        }
    }

    fun handleAuthAction(){
        if(email.isBlank() || password.isBlank()){
            _authResult.value = AuthResult.Error("Fields cannot be empty")
            return
        }

        _authResult.value = AuthResult.Loading

        viewModelScope.launch {
            try{
                val userId = if (isSignUpMode){
                    //create account
                    val result = auth.createUserWithEmailAndPassword(email, password).await()
                    val userId = result.user?.uid

                    //add new account to the user collection in firestore
                    if(userId != null){
                        val userProfile = hashMapOf(
                            "email" to email,
                            "createdAt" to System.currentTimeMillis()
                        )
                        firestore.collection("users").document(userId).set(userProfile).await()
                    }
                    userId
                }
                else{
                    val result = auth.signInWithEmailAndPassword(email, password).await()
                    result.user?.uid

                }

                if(userId != null){
                    if(isSignUpMode){
                        syncRepository.syncOnSignUp()
                    } else{
                        syncRepository.syncOnSignIn(userId)
                    }
                }
                _authResult.value = AuthResult.Sucess
            } catch (e: Exception){
                _authResult.value = AuthResult.Error(e.localizedMessage ?: "An error occurred")
            }
        }
    }

    fun clearResult(){
        _authResult.value = AuthResult.Idle
    }
}