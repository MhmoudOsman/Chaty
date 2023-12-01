package com.chaty.app.auth.di

import android.content.Context
import com.chaty.app.di.annotations.IODispatcher
import com.chaty.data.auth.repo.AuthRepoImpl
import com.chaty.data.user.repo.UserRepoImpl
import com.chaty.domain.auth.repo.AuthRepo
import com.chaty.domain.auth.usecase.LoginUseCase
import com.chaty.domain.auth.usecase.SendOTPUseCase
import com.chaty.domain.user.repo.UserRepo
import com.chaty.domain.user.usecase.GetUserUseCase
import com.chaty.domain.user.usecase.SaveUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideAuthRepo(
        @ApplicationContext context: Context,
        @IODispatcher dispatcher: CoroutineDispatcher
    ): AuthRepo = AuthRepoImpl(context, dispatcher)
   @Singleton
    @Provides
    fun provideUserRepo(
        @ApplicationContext context: Context,
        @IODispatcher dispatcher: CoroutineDispatcher
    ): UserRepo = UserRepoImpl(context, dispatcher)

    @Provides
    fun provideSendOtpUseCase(repo: AuthRepo): SendOTPUseCase = SendOTPUseCase(repo)

    @Provides
    fun provideLoginUseCase(repo: AuthRepo): LoginUseCase = LoginUseCase(repo)
    @Provides
    fun provideGetUserUseCase(repo: UserRepo): GetUserUseCase = GetUserUseCase(repo)
    @Provides
    fun provideSaveUserUseCase(repo: UserRepo): SaveUserUseCase = SaveUserUseCase(repo)
}