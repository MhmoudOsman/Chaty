package com.chaty.app.messages_screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaty.app.di.annotations.MainDispatcher
import com.chaty.app.messages_screen.state.ContactUiIntent
import com.chaty.app.messages_screen.state.ContactUiState
import com.chaty.domain.user.usecase.GetContactsUseCase
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
class ContactVM @Inject constructor(
    @MainDispatcher private val dispatcher: CoroutineDispatcher,
    private val getContactsUseCase: GetContactsUseCase
) : ViewModel() {
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        Log.e(TAG, "error: ${exception.message}",exception )
        _uiState.value = ContactUiState.Error(exception.message ?: "")
    }
    val userIntent = Channel<ContactUiIntent>(Channel.UNLIMITED)
    private val _uiState = MutableStateFlow<ContactUiState>(ContactUiState.Idle)
    val uiState get() = _uiState
    init {
        viewModelScope.launch(errorHandler + dispatcher){
            userIntent.consumeAsFlow().onEach {
                when (it) {
                    ContactUiIntent.Clear -> _uiState.value = ContactUiState.Idle
                    ContactUiIntent.GetContacts -> getContacts()
                }
            }.catch {
                Log.e(TAG, "error: ${it.message}", it)
            }.launchIn(this)
            userIntent.send(ContactUiIntent.GetContacts)
        }

    }

    private fun getContacts() {
        _uiState.value = ContactUiState.Loading
        viewModelScope.launch(errorHandler + dispatcher) {
            _uiState.value = ContactUiState.Success(getContactsUseCase())
        }

    }

companion object{
    private const val TAG = "ContactVM"
}
}