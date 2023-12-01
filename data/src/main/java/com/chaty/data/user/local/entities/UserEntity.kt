package com.chaty.data.user.local.entities

import com.chaty.domain.user.models.UserModel

data class UserEntity(
    val id: String,
    val name: String,
    val info: String,
    val image: String,
    val token: String,
    val phone: String,
    val lastMessage: String
){
    fun asModel(): UserModel = UserModel(
        id = id,
        name = name,
        info = info,
        image = image,
        token = token,
        phone = phone,
        lastMessage = lastMessage
    )
}


