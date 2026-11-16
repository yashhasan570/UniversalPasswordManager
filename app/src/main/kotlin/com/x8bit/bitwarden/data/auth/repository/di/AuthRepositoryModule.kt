package com.x8bit.universalpasswordmanager.data.auth.repository.di

import .core.data.manager.dispatcher.DispatcherManager
import .core.data.manager.toast.ToastManager
import .data.datasource.disk.ConfigDiskSource
import com.work.service.AccountsService
import com.work.service.DevicesService
import com.work.service.HaveIBeenPwnedService
import com.work.service.IdentityService
import com.work.service.OrganizationService
import com.x8bit.universalpasswordmanager.data.auth.datasource.disk.AuthDiskSource
import com.x8bit.universalpasswordmanager.data.auth.datasource.sdk.AuthSdkSource
import com.x8bit.universalpasswordmanager.data.auth.manager.AuthRequestManager
import com.x8bit.universalpasswordmanager.data.auth.manager.AuthStateManager
import com.x8bit.universalpasswordmanager.data.auth.manager.KdfManager
import com.x8bit.universalpasswordmanager.data.auth.manager.KeyConnectorManager
import com.x8bit.universalpasswordmanager.data.auth.manager.OrganizationManager
import com.x8bit.universalpasswordmanager.data.auth.manager.TrustedDeviceManager
import com.x8bit.universalpasswordmanager.data.auth.manager.UserLogoutManager
import com.x8bit.universalpasswordmanager.data.auth.manager.UserStateManager
import com.x8bit.universalpasswordmanager.data.auth.repository.AuthRepository
import com.x8bit.universalpasswordmanager.data.auth.repository.AuthRepositoryImpl
import com.x8bit.universalpasswordmanager.data.platform.datasource.disk.SettingsDiskSource
import com.x8bit.universalpasswordmanager.data.platform.manager.BiometricsEncryptionManager
import com.x8bit.universalpasswordmanager.data.platform.manager.FeatureFlagManager
import com.x8bit.universalpasswordmanager.data.platform.manager.LogsManager
import com.x8bit.universalpasswordmanager.data.platform.manager.PushManager
import com.x8bit.universalpasswordmanager.data.platform.manager.policy.PasswordPolicyManager
import com.x8bit.universalpasswordmanager.data.platform.repository.EnvironmentRepository
import com.x8bit.universalpasswordmanager.data.platform.repository.SettingsRepository
import com.x8bit.universalpasswordmanager.data.vault.datasource.sdk.VaultSdkSource
import com.x8bit.universalpasswordmanager.data.vault.repository.VaultRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

/**
 * Provides repositories in the auth package.
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {

    @Provides
    @Singleton
    fun providesAuthRepository(
        clock: Clock,
        accountsService: AccountsService,
        devicesService: DevicesService,
        identityService: IdentityService,
        haveIBeenPwnedService: HaveIBeenPwnedService,
        organizationService: OrganizationService,
        authSdkSource: AuthSdkSource,
        vaultSdkSource: VaultSdkSource,
        authDiskSource: AuthDiskSource,
        settingsDiskSource: SettingsDiskSource,
        configDiskSource: ConfigDiskSource,
        dispatcherManager: DispatcherManager,
        environmentRepository: EnvironmentRepository,
        settingsRepository: SettingsRepository,
        vaultRepository: VaultRepository,
        biometricsEncryptionManager: BiometricsEncryptionManager,
        keyConnectorManager: KeyConnectorManager,
        authRequestManager: AuthRequestManager,
        trustedDeviceManager: TrustedDeviceManager,
        userLogoutManager: UserLogoutManager,
        pushManager: PushManager,
        passwordPolicyManager: PasswordPolicyManager,
        logsManager: LogsManager,
        userStateManager: UserStateManager,
        kdfManager: KdfManager,
        toastManager: ToastManager,
        featureFlagManager: FeatureFlagManager,
        organizationManager: OrganizationManager,
        authStateManager: AuthStateManager,
    ): AuthRepository = AuthRepositoryImpl(
        clock = clock,
        accountsService = accountsService,
        devicesService = devicesService,
        identityService = identityService,
        organizationService = organizationService,
        authSdkSource = authSdkSource,
        vaultSdkSource = vaultSdkSource,
        authDiskSource = authDiskSource,
        settingsDiskSource = settingsDiskSource,
        configDiskSource = configDiskSource,
        haveIBeenPwnedService = haveIBeenPwnedService,
        dispatcherManager = dispatcherManager,
        environmentRepository = environmentRepository,
        settingsRepository = settingsRepository,
        vaultRepository = vaultRepository,
        biometricsEncryptionManager = biometricsEncryptionManager,
        keyConnectorManager = keyConnectorManager,
        authRequestManager = authRequestManager,
        trustedDeviceManager = trustedDeviceManager,
        userLogoutManager = userLogoutManager,
        pushManager = pushManager,
        passwordPolicyManager = passwordPolicyManager,
        logsManager = logsManager,
        userStateManager = userStateManager,
        kdfManager = kdfManager,
        toastManager = toastManager,
        featureFlagManager = featureFlagManager,
        organizationManager = organizationManager,
        authStateManager = authStateManager,
    )
}
