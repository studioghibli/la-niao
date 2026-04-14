package com.laniao.di

import com.laniao.data.repository.DrinkEntryRepositoryImpl
import com.laniao.data.repository.ExerciseRepositoryImpl
import com.laniao.data.repository.ManuallyMissedTimeRepositoryImpl
import com.laniao.data.repository.PeeEntryRepositoryImpl
import com.laniao.data.repository.VoidScheduleRepositoryImpl
import com.laniao.data.repository.WaterIntakeRepositoryImpl
import com.laniao.domain.repository.DrinkEntryRepository
import com.laniao.domain.repository.ExerciseRepository
import com.laniao.domain.repository.ManuallyMissedTimeRepository
import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.domain.repository.VoidScheduleRepository
import com.laniao.domain.repository.WaterIntakeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for binding repository implementations to interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPeeEntryRepository(
        impl: PeeEntryRepositoryImpl
    ): PeeEntryRepository

    @Binds
    @Singleton
    abstract fun bindVoidScheduleRepository(
        impl: VoidScheduleRepositoryImpl
    ): VoidScheduleRepository

    @Binds
    @Singleton
    abstract fun bindWaterIntakeRepository(
        impl: WaterIntakeRepositoryImpl
    ): WaterIntakeRepository

    @Binds
    @Singleton
    abstract fun bindDrinkEntryRepository(
        impl: DrinkEntryRepositoryImpl
    ): DrinkEntryRepository

    @Binds
    @Singleton
    abstract fun bindExerciseRepository(
        impl: ExerciseRepositoryImpl
    ): ExerciseRepository

    @Binds
    @Singleton
    abstract fun bindManuallyMissedTimeRepository(
        impl: ManuallyMissedTimeRepositoryImpl
    ): ManuallyMissedTimeRepository
}
