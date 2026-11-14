package com.universalpasswordmanager.data.auth.repository.di

import com.universalpasswordmanager.core.data.manager.dispatcher.DispatcherManager
import com.universalpasswordmanager.core.data.manager.toast.ToastManager
import com.universalpasswordmanager.data.datasource.disk.ConfigDiskSource
import com.universalpasswordmanager.network.service.AccountsService
import com.universalpasswordmanager.network.service.DevicesService
import com.universalpasswordmanager.network.service.HaveIBeenPwnedService
import com.universalpasswordmanager.network.service.IdentityService
import com.universalpasswordmanager.network.service.OrganizationService
import com.universalpasswordmanager.data.auth.datasource.disk.AuthDiskSource
import com.universalpasswordmanager.data.auth.datasource.sdk.AuthSdkSource
import com.universalpasswordmanager.data.auth.manager.AuthRequestManager
import com.universalpasswordmanager.data.auth.manager.AuthStateManager
import com.universalpasswordmanager.data.auth.manager.KdfManager
import com.universalpasswordmanager.data.auth.manager.KeyConnectorManager
import com.universalpasswordmanager.data.auth.manager.OrganizationManager
import com.universalpasswordmanager.data.auth.manager.TrustedDeviceManager
import com.universalpasswordmanager.data.auth.manager.UserLogoutManager
import com.universalpasswordmanager.data.auth.manager.UserStateManager
import com.universalpasswordmanager.data.auth.repository.AuthRepository
import com.universalpasswordmanager.data.auth.repository.AuthRepositoryImpl
import com.universalpasswordmanager.data.platform.datasource.disk.SettingsDiskSource
import com.universalpasswordmanager.data.platform.manager.BiometricsEncryptionManager
import com.universalpasswordmanager.data.platform.manager.FeatureFlagManager
import com.universalpasswordmanager.data.platform.manager.LogsManager
import com.universalpasswordmanager.data.platform.manager.PushManager
import com.universalpasswordmanager.data.platform.manager.policy.PasswordPolicyManager
import com.universalpasswordmanager.data.platform.repository.EnvironmentRepository
import com.universalpasswordmanager.data.platform.repository.SettingsRepository
import com.universalpasswordmanager.data.vault.datasource.sdk.VaultSdkSource
import com.universalpasswordmanager.data.vault.repository.VaultRepository
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
