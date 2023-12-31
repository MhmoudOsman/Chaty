package com.chaty.domain.user.models

data class UserModel(
    val id: String,
    val displayName: String = "",
    val name: String,
    val info: String,
    val image: String,
    val token: String,
    val phone: String,
    val lastMessage: String,
)
