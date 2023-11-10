package com.chaty.app.auth.state

import com.chaty.domain.user.models.UserModel

sealed class CreateAccountUiState {
    data object Idle : CreateAccountUiState()
    data object Loading : CreateAccountUiState()
    data object SaveSuccess : CreateAccountUiState()
    data class LoadSuccess(val user: UserModel) : CreateAccountUiState()
    data class Error(val message: String, val code: CreateAccountErrorsCode) : CreateAccountUiState()
}
