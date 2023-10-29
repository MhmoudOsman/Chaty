package com.chaty.data.auth.local.entities

import com.chaty.domain.auth.models.UserModel

data class UserEntity(
    val id: String,
    val name: String,
    val info: String,
    val image: String,
    val token: String,
    val phone: String
){
    fun asModel(): UserModel = UserModel(
        id = id,
        name = name,
        info = info,
        image = image,
        token = token,
        phone = phone
    )
}


