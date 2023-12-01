package com.chaty.domain.auth.usecase

import com.chaty.domain.auth.repo.AuthRepo

class LogoutUseCase(private val repo: AuthRepo) {
    suspend operator fun invoke(): Boolean = repo.logout()
}