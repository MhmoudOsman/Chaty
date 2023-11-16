package com.chaty.app.auth.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaty.domain.auth.state.PhoneAuthResult
import com.chaty.domain.auth.usecase.LoginUseCase
import com.chaty.domain.auth.usecase.SendOTPUseCase
import com.chaty.app.auth.state.AuthUiIntent
import com.chaty.app.auth.state.AuthUiState
import com.chaty.app.di.annotations.MainDispatcher
import com.chaty.app.auth.state.AuthErrorsCode
import com.chaty.domain.auth.models.PhoneAuthOptionsModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
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
class LoginVM @Inject constructor(
    @MainDispatcher private val dispatcher: CoroutineDispatcher,
    private val loginUseCase: LoginUseCase,
    private val sendOtpUseCase: SendOTPUseCase
) : ViewModel() {
    private val errorHandler = CoroutineExceptionHandler { _, th ->
        Log.e(TAG, "error: ${th.message}", th)
        when (th) {
            is FirebaseAuthInvalidCredentialsException -> {
                _uiState.value = AuthUiState.Error("Invalid Credentials", AuthErrorsCode.INVALID_VERIFICATION_CODE)
            }
            is FirebaseNetworkException->{
                _uiState.value = AuthUiState.Error("No Internet Connection",AuthErrorsCode.NO_INTERNET_CONNECTION)
            }
            else -> {
                _uiState.value = AuthUiState.Error(th.message?:"Error", AuthErrorsCode.UNKNOWN)
            }
        }
    }

    val userIntent = Channel<AuthUiIntent>(Channel.UNLIMITED)
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState get() = _uiState

    init {
        userIntent.consumeAsFlow().onEach {
            when (it) {
                is AuthUiIntent.Clear -> _uiState.value = AuthUiState.Idle
                is AuthUiIntent.SendOTP -> sendOtp(it.optionsModel)
                is AuthUiIntent.VerifyOTP -> login(it.verificationId, it.code)
            }
        }.catch {
            Log.e(TAG, "error: ${it.message}", it)
        }.launchIn(viewModelScope)

    }

    private fun sendOtp(optionsModel: PhoneAuthOptionsModel) {
        viewModelScope.launch(errorHandler + dispatcher) {
            sendOtpUseCase(optionsModel).onEach {
                when (it) {
                    is PhoneAuthResult.Loading -> {
                        _uiState.value = AuthUiState.Loading
                    }
                    is PhoneAuthResult.CodeSent -> {
                        _uiState.value = AuthUiState.SendOTPSuccess(it.verificationId, it.token)
                    }

                    is PhoneAuthResult.VerificationCompleted -> {
                        login(it.credentials)
                    }
                    is PhoneAuthResult.VerificationFailed -> {
                        Log.e(TAG, "sendOtp: ${it.exception.message}", it.exception)
                        when (it.exception) {
                            is FirebaseAuthInvalidCredentialsException -> {
                                _uiState.value = AuthUiState.Error("Invalid Credentials",
                                    AuthErrorsCode.INVALID_PHONE_NUMBER)
                            }
                            is FirebaseNetworkException->{
                                _uiState.value = AuthUiState.Error("No Internet Connection",AuthErrorsCode.NO_INTERNET_CONNECTION)
                            }
                            else -> {
                                _uiState.value = AuthUiState.Error(it.exception.message?:"Unknown Error", AuthErrorsCode.UNKNOWN)
                            }
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    private fun login(credential: PhoneAuthCredential) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch(errorHandler + dispatcher) {
            if (loginUseCase(credential)){
                _uiState.value = AuthUiState.LoginSuccess
            }else{
                _uiState.value = AuthUiState.Error("Login Failed", AuthErrorsCode.UNKNOWN)
            }
        }
    }

    private fun login(verificationId: String, code: String) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch(errorHandler + dispatcher) {
            if (loginUseCase(verificationId, code)){
                _uiState.value = AuthUiState.LoginSuccess
            }else{
                _uiState.value = AuthUiState.Error("Login Failed", AuthErrorsCode.UNKNOWN)
            }
        }
    }
    companion object {
        private const val TAG = "LoginVM"
    }
}