package com.iftikar.ownerintern.di

import com.iftikar.ownerintern.data.repository.DestinationRepositoryImpl
import com.iftikar.ownerintern.domain.repository.DestinationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindDestinationRepository(impl: DestinationRepositoryImpl): DestinationRepository
}