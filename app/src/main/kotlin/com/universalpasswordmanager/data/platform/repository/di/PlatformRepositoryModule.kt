package com.universalpasswordmanager.data.platform.repository.di

import android.view.autofill.AutofillManager
import com.universalpasswordmanager.core.data.manager.BuildInfoManager
import com.universalpasswordmanager.core.data.manager.dispatcher.DispatcherManager
import com.universalpasswordmanager.data.manager.flightrecorder.FlightRecorderManager
import com.universalpasswordmanager.data.repository.ServerConfigRepository
import com.universalpasswordmanager.data.auth.datasource.disk.AuthDiskSource
import com.universalpasswordmanager.data.autofill.accessibility.manager.AccessibilityEnabledManager
import com.universalpasswordmanager.data.autofill.manager.AutofillEnabledManager
import com.universalpasswordmanager.data.platform.datasource.disk.CookieDiskSource
import com.universalpasswordmanager.data.platform.datasource.disk.EnvironmentDiskSource
import com.universalpasswordmanager.data.platform.datasource.disk.FeatureFlagOverrideDiskSource
import com.universalpasswordmanager.data.platform.datasource.disk.SettingsDiskSource
import com.universalpasswordmanager.data.platform.manager.PolicyManager
import com.universalpasswordmanager.data.platform.repository.AuthenticatorBridgeRepository
import com.universalpasswordmanager.data.platform.repository.AuthenticatorBridgeRepositoryImpl
import com.universalpasswordmanager.data.platform.repository.DebugMenuRepository
import com.universalpasswordmanager.data.platform.repository.DebugMenuRepositoryImpl
import com.universalpasswordmanager.data.platform.repository.EnvironmentRepository
import com.universalpasswordmanager.data.platform.repository.EnvironmentRepositoryImpl
import com.universalpasswordmanager.data.platform.repository.SettingsRepository
import com.universalpasswordmanager.data.platform.repository.SettingsRepositoryImpl
import com.universalpasswordmanager.data.vault.datasource.disk.VaultDiskSource
import com.universalpasswordmanager.data.vault.datasource.sdk.ScopedVaultSdkSource
import com.universalpasswordmanager.data.vault.datasource.sdk.VaultSdkSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides repositories in the auth package.
 */
@Module
@InstallIn(SingletonComponent::class)
object PlatformRepositoryModule {

    @Provides
    @Singleton
    fun providesAuthenticatorBridgeRepository(
        authDiskSource: AuthDiskSource,
        vaultDiskSource: VaultDiskSource,
        scopedVaultSdkSource: ScopedVaultSdkSource,
    ): AuthenticatorBridgeRepository = AuthenticatorBridgeRepositoryImpl(
        authDiskSource = authDiskSource,
        vaultDiskSource = vaultDiskSource,
        scopedVaultSdkSource = scopedVaultSdkSource,
    )

    @Provides
    @Singleton
    fun provideEnvironmentRepository(
        environmentDiskSource: EnvironmentDiskSource,
        authDiskSource: AuthDiskSource,
        dispatcherManager: DispatcherManager,
    ): EnvironmentRepository =
        EnvironmentRepositoryImpl(
            environmentDiskSource = environmentDiskSource,
            authDiskSource = authDiskSource,
            dispatcherManager = dispatcherManager,
        )

    @Provides
    @Singleton
    fun provideSettingsRepository(
        autofillManager: AutofillManager,
        autofillEnabledManager: AutofillEnabledManager,
        authDiskSource: AuthDiskSource,
        buildInfoManager: BuildInfoManager,
        settingsDiskSource: SettingsDiskSource,
        vaultSdkSource: VaultSdkSource,
        accessibilityEnabledManager: AccessibilityEnabledManager,
        dispatcherManager: DispatcherManager,
        policyManager: PolicyManager,
        flightRecorderManager: FlightRecorderManager,
    ): SettingsRepository =
        SettingsRepositoryImpl(
            autofillManager = autofillManager,
            autofillEnabledManager = autofillEnabledManager,
            authDiskSource = authDiskSource,
            buildInfoManager = buildInfoManager,
            settingsDiskSource = settingsDiskSource,
            vaultSdkSource = vaultSdkSource,
            accessibilityEnabledManager = accessibilityEnabledManager,
            dispatcherManager = dispatcherManager,
            policyManager = policyManager,
            flightRecorderManager = flightRecorderManager,
        )

    @Provides
    @Singleton
    fun provideDebugMenuRepository(
        featureFlagOverrideDiskSource: FeatureFlagOverrideDiskSource,
        serverConfigRepository: ServerConfigRepository,
        authDiskSource: AuthDiskSource,
        settingsDiskSource: SettingsDiskSource,
        cookieDiskSource: CookieDiskSource,
    ): DebugMenuRepository = DebugMenuRepositoryImpl(
        featureFlagOverrideDiskSource = featureFlagOverrideDiskSource,
        serverConfigRepository = serverConfigRepository,
        authDiskSource = authDiskSource,
        settingsDiskSource = settingsDiskSource,
        cookieDiskSource = cookieDiskSource,
    )
}
