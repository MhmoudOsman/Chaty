package com.chaty.app.di.modules

import com.chaty.app.di.annotations.DefaultDispatcher
import com.chaty.app.di.annotations.IODispatcher
import com.chaty.app.di.annotations.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @IODispatcher
    @Provides
    fun provideDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun provideDispatcherMAIN(): CoroutineDispatcher = Dispatchers.Main

    @DefaultDispatcher
    @Provides
    fun provideDispatcherDefault(): CoroutineDispatcher = Dispatchers.Default
}