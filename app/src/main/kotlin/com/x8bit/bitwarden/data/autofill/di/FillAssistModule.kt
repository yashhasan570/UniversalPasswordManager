package com.x8bit.universalpasswordmanager.data.autofill.di

import android.content.SharedPreferences
import .core.data.manager.dispatcher.DispatcherManager
import .data.datasource.disk.di.UnencryptedPreferences
import .data.repository.ServerConfigRepository
import com.work.service.FillAssistService
import com.x8bit.universalpasswordmanager.data.autofill.datasource.disk.FillAssistDiskSource
import com.x8bit.universalpasswordmanager.data.autofill.datasource.disk.FillAssistDiskSourceImpl
import com.x8bit.universalpasswordmanager.data.autofill.manager.FillAssistManager
import com.x8bit.universalpasswordmanager.data.autofill.manager.FillAssistManagerImpl
import com.x8bit.universalpasswordmanager.data.platform.datasource.disk.EnvironmentDiskSource
import com.x8bit.universalpasswordmanager.data.platform.manager.FeatureFlagManager
import com.x8bit.universalpasswordmanager.data.platform.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import kotlinx.serialization.json.Json
import javax.inject.Singleton

/**
 * Provides fill-assist dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object FillAssistModule {

    @Provides
    @Singleton
    fun providesFillAssistDiskSource(
        @UnencryptedPreferences sharedPreferences: SharedPreferences,
        json: Json,
    ): FillAssistDiskSource =
        FillAssistDiskSourceImpl(
            sharedPreferences = sharedPreferences,
            json = json,
        )

    @Provides
    @Singleton
    fun providesFillAssistManager(
        fillAssistService: FillAssistService,
        fillAssistDiskSource: FillAssistDiskSource,
        featureFlagManager: FeatureFlagManager,
        serverConfigRepository: ServerConfigRepository,
        settingsRepository: SettingsRepository,
        environmentDiskSource: EnvironmentDiskSource,
        clock: Clock,
        dispatcherManager: DispatcherManager,
    ): FillAssistManager =
        FillAssistManagerImpl(
            fillAssistService = fillAssistService,
            fillAssistDiskSource = fillAssistDiskSource,
            featureFlagManager = featureFlagManager,
            serverConfigRepository = serverConfigRepository,
            settingsRepository = settingsRepository,
            environmentDiskSource = environmentDiskSource,
            clock = clock,
            dispatcherManager = dispatcherManager,
        )
}
