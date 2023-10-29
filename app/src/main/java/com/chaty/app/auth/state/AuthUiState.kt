package com.chaty.app.auth.state

import com.google.firebase.auth.PhoneAuthProvider

sealed class AuthUiState {
    data object Idle : AuthUiState()
    data object Loading : AuthUiState()

    data class SendOTPSuccess(
        val verificationId: String,
        val token: PhoneAuthProvider.ForceResendingToken
    ) : AuthUiState()

    data object LoginSuccess : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
