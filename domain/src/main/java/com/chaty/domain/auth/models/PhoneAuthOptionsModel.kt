package com.chaty.domain.auth.models

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider

data class PhoneAuthOptionsModel(
    val phone: String,
    val activity: Activity,
    val token: PhoneAuthProvider.ForceResendingToken? = null
)
