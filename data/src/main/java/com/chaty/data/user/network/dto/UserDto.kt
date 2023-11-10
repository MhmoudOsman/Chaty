package com.chaty.data.user.network.dto

import com.chaty.data.user.local.entities.UserEntity
import com.chaty.domain.user.models.UserModel

data class UserDto(
    val id: String = "",
    val name: String = "",
    val info: String = "",
    val image: String = "",
    val token: String = "",
    val phone: String = ""
) {
    fun asModel(): UserModel = UserModel(
        id = id,
        name = name,
        info = info,
        image = image,
        token = token,
        phone = phone
    )

    fun asEntity(): UserEntity = UserEntity(
        id = id,
        name = name,
        info = info,
        image = image,
        token = token,
        phone = phone
    )
}


