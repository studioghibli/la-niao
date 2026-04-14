package com.laniao.di

import com.laniao.util.Clock
import com.laniao.util.DispatcherProvider
import com.laniao.util.StandardDispatcherProvider
import com.laniao.util.SystemClock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    @Provides
    @Singleton
    fun provideClock(): Clock = SystemClock()

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = StandardDispatcherProvider()
}
