package com.chaty.domain.auth.usecase

import com.chaty.domain.auth.repo.AuthRepo
import com.google.firebase.auth.PhoneAuthCredential

class LoginUseCase(private val repo: AuthRepo) {
    suspend operator fun invoke(credential: PhoneAuthCredential): Boolean = repo.login(credential)
    suspend operator fun invoke(verificationId: String, code: String): Boolean = repo.login(verificationId, code)
}