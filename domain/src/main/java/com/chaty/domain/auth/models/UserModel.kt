package com.chaty.domain.auth.models

data class UserModel(
    val id: String,
    val name: String,
    val info: String,
    val image: String,
    val token: String,
    val phone: String
)
