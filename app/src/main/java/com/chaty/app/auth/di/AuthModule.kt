package com.chaty.app.auth.di

import com.chaty.data.auth.repo.AuthRepoImpl
import com.chaty.domain.auth.repo.AuthRepo
import com.chaty.domain.auth.usecase.LoginUseCase
import com.chaty.app.di.annotations.IODispatcher
import com.chaty.domain.auth.usecase.SendOTPUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideAuthRepo(@IODispatcher dispatcher: CoroutineDispatcher): AuthRepo = AuthRepoImpl(dispatcher)

    @Provides
    fun provideSendOtpUseCase(repo: AuthRepo): SendOTPUseCase = SendOTPUseCase(repo)

    @Provides
    fun provideLoginUseCase(repo: AuthRepo): LoginUseCase = LoginUseCase(repo)
}