package com.chaty.domain.user.repo

import android.net.Uri
import com.chaty.domain.user.models.UserModel

interface UserRepo {
    suspend fun getUser(): UserModel
    suspend fun saveUser(name: String, info: String, image: String, phone: String): Boolean
    suspend fun uploadProfilePhoto(image: Uri): String
}