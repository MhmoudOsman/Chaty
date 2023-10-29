package com.chaty.domain.auth.state

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

sealed class PhoneAuthResult {
    data object Loading : PhoneAuthResult()
    data class VerificationCompleted(val credentials: PhoneAuthCredential) : PhoneAuthResult()
    data class CodeSent(
        val verificationId: String,
        val token: PhoneAuthProvider.ForceResendingToken
    ) : PhoneAuthResult()

    data class VerificationFailed(val exception: Exception) : PhoneAuthResult()
}
