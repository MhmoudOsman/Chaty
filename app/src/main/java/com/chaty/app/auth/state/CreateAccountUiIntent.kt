package com.chaty.app.auth.state

import android.net.Uri

sealed class CreateAccountUiIntent{
    data object Clear : CreateAccountUiIntent()
    data class SaveUser(
        val name: String,
        val info: String,
        val image: String,
        val phone: String) : CreateAccountUiIntent()
    data class SaveUserWithPic(
       val name: String,
       val info: String,
       val image: Uri,
       val phone: String) : CreateAccountUiIntent()
}
