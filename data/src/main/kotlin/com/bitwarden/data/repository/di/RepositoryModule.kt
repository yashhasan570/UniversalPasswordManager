package .data.repository.di

import .core.data.manager.dispatcher.DispatcherManager
import .data.datasource.disk.ConfigDiskSource
import .data.repository.ServerConfigRepository
import .data.repository.ServerConfigRepositoryImpl
import com.work.service.ConfigService
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
