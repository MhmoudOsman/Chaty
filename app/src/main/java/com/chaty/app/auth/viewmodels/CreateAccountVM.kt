package com.chaty.app.auth.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaty.app.auth.state.CreateAccountErrorsCode
import com.chaty.app.auth.state.CreateAccountUiIntent
import com.chaty.app.auth.state.CreateAccountUiState
import com.chaty.app.di.annotations.MainDispatcher
import com.chaty.domain.user.usecase.GetUserUseCase
import com.chaty.domain.user.usecase.SaveUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAccountVM @Inject constructor(
    @MainDispatcher private val dispatcher: CoroutineDispatcher,
    private val getUserUseCase: GetUserUseCase,
    private val saveUserUseCase: SaveUserUseCase
) : ViewModel() {
    private val errorHandler = CoroutineExceptionHandler { _, th ->
        Log.e(TAG, "error: ${th.message}", th)
        _uiState.value = CreateAccountUiState.Error(th.message?:"Error", CreateAccountErrorsCode.UNKNOWN)

    }
    val userIntent = Channel<CreateAccountUiIntent>(Channel.UNLIMITED)
    private val _uiState = MutableStateFlow<CreateAccountUiState>(CreateAccountUiState.Idle)
    val uiState get() = _uiState

    init {
        userIntent.consumeAsFlow().onEach {
            when (it) {
                is CreateAccountUiIntent.Clear -> _uiState.value = CreateAccountUiState.Idle
                is CreateAccountUiIntent.SaveUser -> saveUser(it.name, it.info, it.image, it.phone)
                is CreateAccountUiIntent.SaveUserWithPic -> saveUser(it.name, it.info, it.image, it.phone)
            }
        }.catch {
            Log.e(TAG, "error: ${it.message}", it)
            _uiState.value = CreateAccountUiState.Error(it.message?:"Error", CreateAccountErrorsCode.UNKNOWN)
        }.launchIn(viewModelScope)
        getUser()
    }

    private fun getUser() {
        _uiState.value = CreateAccountUiState.Loading
        viewModelScope.launch(errorHandler + dispatcher) {
            _uiState.value = CreateAccountUiState.LoadSuccess(getUserUseCase())
        }
    }

    private fun saveUser(
        name: String,
        info: String,
        image: Uri,
        phone: String
    ) {
        _uiState.value = CreateAccountUiState.Loading
        viewModelScope.launch(errorHandler + dispatcher) {
          if (saveUserUseCase(name, info, image, phone)){
            _uiState.value = CreateAccountUiState.SaveSuccess
          }  else{
            _uiState.value = CreateAccountUiState.Error("Load Failed", CreateAccountErrorsCode.LOAD_USER_FAILED)
          }
        }
    }
    private fun saveUser(
        name: String,
        info: String,
        image: String,
        phone: String
    ) {
        _uiState.value = CreateAccountUiState.Loading
        viewModelScope.launch(errorHandler + dispatcher) {
          if (saveUserUseCase(name, info, image, phone)){
            _uiState.value = CreateAccountUiState.SaveSuccess
          }  else{
            _uiState.value = CreateAccountUiState.Error("Save Failed", CreateAccountErrorsCode.SAVE_USER_FAILED)
          }
        }
    }

    companion object {
        private const val TAG = "CreateAccountVM"
    }
}