package com.chaty.domain.user.usecase

import android.net.Uri
import com.chaty.domain.user.repo.UserRepo

class SaveUserUseCase(private val repo: UserRepo) {
    suspend operator fun invoke(
        name: String,
        info: String,
        image: Uri,
        phone: String
    ): Boolean = repo.uploadProfilePhoto(image).let { repo.saveUser(name, info, it, phone) }
    suspend operator fun invoke(
        name: String,
        info: String,
        image: String,
        phone: String
    ): Boolean =  repo.saveUser(name, info, image, phone) }