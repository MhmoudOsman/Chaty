package com.chaty.domain.auth.repo

import com.chaty.domain.auth.models.PhoneAuthOptionsModel
import com.chaty.domain.auth.state.PhoneAuthResult
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.flow.Flow

interface AuthRepo {
    suspend fun sendOtp(optionsModel: PhoneAuthOptionsModel):Flow<PhoneAuthResult>
    suspend fun login(credential: PhoneAuthCredential): Boolean
    suspend fun login(verificationId: String, code: String): Boolean

}