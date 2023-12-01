package com.chaty.app.messages_screen.di

import android.content.Context
import com.chaty.domain.user.repo.UserRepo
import com.chaty.domain.user.usecase.GetContactsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ContactModule {

    @Provides
    fun provideGetContactsUseCase(
        repo: UserRepo,
        @ApplicationContext context: Context
    ): GetContactsUseCase = GetContactsUseCase(repo, context)

}