package com.chaty.app.auth.state

import com.chaty.domain.auth.models.PhoneAuthOptionsModel

sealed class AuthUiIntent{
    data object Clear : AuthUiIntent()
    data class SendOTP(val optionsModel: PhoneAuthOptionsModel) : AuthUiIntent()
    data class VerifyOTP(val verificationId: String, val code: String) : AuthUiIntent()
}
