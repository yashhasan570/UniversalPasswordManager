package com.x8bit.universalpasswordmanager.data.platform.provider.di

import com.work.interceptor.BaseUrlsProvider
import com.x8bit.universalpasswordmanager.data.platform.datasource.disk.EnvironmentDiskSource
import com.x8bit.universalpasswordmanager.data.platform.provider.BaseUrlsProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This module initializes platform-specific "Provider" dependencies for the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object PlatformProvidersModule {

    @Provides
    @Singleton
    fun provideBaseUrlsProvider(
        environmentDiskSource: EnvironmentDiskSource,
    ): BaseUrlsProvider =
        BaseUrlsProviderImpl(environmentDiskSource = environmentDiskSource)
}
