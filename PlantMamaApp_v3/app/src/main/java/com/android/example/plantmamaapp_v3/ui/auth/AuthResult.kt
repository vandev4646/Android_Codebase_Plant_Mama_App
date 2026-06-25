package com.android.example.plantmamaapp_v3.ui.auth

sealed class AuthResult {
    object Idle : AuthResult()
    object Loading: AuthResult()
    object Sucess: AuthResult()
    data class Error(val theMessage: String) : AuthResult()
}