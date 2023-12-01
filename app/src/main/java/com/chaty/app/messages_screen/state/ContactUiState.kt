package com.chaty.app.messages_screen.state

import com.chaty.domain.user.models.UserModel

sealed class ContactUiState {
    data object Idle : ContactUiState()
    data object Loading : ContactUiState()
    data class Success(val data:List<UserModel>) : ContactUiState()
    data class Error(val message: String) : ContactUiState()
}
