package com.chaty.domain.auth.usecase

import com.chaty.domain.auth.models.PhoneAuthOptionsModel
import com.chaty.domain.auth.repo.AuthRepo

class SendOTPUseCase(private val repo: AuthRepo) {
    suspend operator fun invoke(optionsModel: PhoneAuthOptionsModel) = repo.sendOtp(optionsModel)
}