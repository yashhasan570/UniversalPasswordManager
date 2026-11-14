package com.universalpasswordmanager.data.repository.di

import com.universalpasswordmanager.core.data.manager.dispatcher.DispatcherManager
import com.universalpasswordmanager.data.datasource.disk.ConfigDiskSource
import com.universalpasswordmanager.data.repository.ServerConfigRepository
import com.universalpasswordmanager.data.repository.ServerConfigRepositoryImpl
import com.universalpasswordmanager.network.service.ConfigService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

/**
 * Provides repositories in the data module.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideServerConfigRepository(
        configDiskSource: ConfigDiskSource,
        configService: ConfigService,
        clock: Clock,
        dispatcherManager: DispatcherManager,
    ): ServerConfigRepository =
        ServerConfigRepositoryImpl(
            configDiskSource = configDiskSource,
            configService = configService,
            clock = clock,
            dispatcherManager = dispatcherManager,
        )
}
