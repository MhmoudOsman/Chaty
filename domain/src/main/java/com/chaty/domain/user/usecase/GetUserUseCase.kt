package com.chaty.domain.user.usecase

import com.chaty.domain.user.repo.UserRepo

class GetUserUseCase(private val repo: UserRepo) {
    suspend operator fun invoke() = repo.getUser()
}