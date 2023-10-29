package com.chaty.data.auth

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.chaty.data.auth.network.dto.UserDto
import com.chaty.domain.auth.models.UserModel
import com.chaty.domain.auth.state.PhoneAuthResult

fun UserModel.asDTO(): UserDto = UserDto(
    id = this.id,
    name = this.name,
    info = this.info,
    image = this.image,
    token = this.token,
    phone = this.phone
)

fun PhoneAuthOptions.Builder.buildWithCallbacks(result: (PhoneAuthResult) -> Unit): PhoneAuthOptions {
    val callback =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                result(PhoneAuthResult.VerificationCompleted(credential))
            }

            override fun onVerificationFailed(e: FirebaseException) {
                result(PhoneAuthResult.VerificationFailed(e))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                result(PhoneAuthResult.CodeSent(verificationId, token))
            }
        }

    return setCallbacks(callback).build()
}