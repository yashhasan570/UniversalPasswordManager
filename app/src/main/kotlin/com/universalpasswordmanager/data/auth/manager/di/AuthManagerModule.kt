package com.universalpasswordmanager.data.auth.manager.di

import android.content.Context
import com.universalpasswordmanager.core.data.manager.dispatcher.DispatcherManager
import com.universalpasswordmanager.core.data.manager.toast.ToastManager
import com.universalpasswordmanager.network.service.AccountsService
import com.universalpasswordmanager.network.service.AuthRequestsService
import com.universalpasswordmanager.network.service.DevicesService
import com.universalpasswordmanager.network.service.NewAuthRequestService
import com.universalpasswordmanager.network.service.OrganizationService
import com.universalpasswordmanager.data.auth.datasource.disk.AuthDiskSource
import com.universalpasswordmanager.data.auth.datasource.sdk.AuthSdkSource
import com.universalpasswordmanager.data.auth.manager.AddTotpItemFromAuthenticatorManager
import com.universalpasswordmanager.data.auth.manager.AddTotpItemFromAuthenticatorManagerImpl
import com.universalpasswordmanager.data.auth.manager.AuthRequestManager
import com.universalpasswordmanager.data.auth.manager.AuthRequestManagerImpl
import com.universalpasswordmanager.data.auth.manager.AuthRequestNotificationManager
import com.universalpasswordmanager.data.auth.manager.AuthRequestNotificationManagerImpl
import com.universalpasswordmanager.data.auth.manager.AuthStateManager
import com.universalpasswordmanager.data.auth.manager.AuthStateManagerImpl
import com.universalpasswordmanager.data.auth.manager.AuthTokenManager
import com.universalpasswordmanager.data.auth.manager.AuthTokenManagerImpl
import com.universalpasswordmanager.data.auth.manager.KdfManager
import com.universalpasswordmanager.data.auth.manager.KdfManagerImpl
import com.universalpasswordmanager.data.auth.manager.KeyConnectorManager
import com.universalpasswordmanager.data.auth.manager.KeyConnectorManagerImpl
import com.universalpasswordmanager.data.auth.manager.OrganizationManager
import com.universalpasswordmanager.data.auth.manager.OrganizationManagerImpl
import com.universalpasswordmanager.data.auth.manager.TrustedDeviceManager
import com.universalpasswordmanager.data.auth.manager.TrustedDeviceManagerImpl
import com.universalpasswordmanager.data.auth.manager.UserLogoutManager
import com.universalpasswordmanager.data.auth.manager.UserLogoutManagerImpl
import com.universalpasswordmanager.data.auth.manager.UserStateManager
import com.universalpasswordmanager.data.auth.manager.UserStateManagerImpl
import com.universalpasswordmanager.data.platform.datasource.disk.PushDiskSource
import com.universalpasswordmanager.data.platform.datasource.disk.SettingsDiskSource
import com.universalpasswordmanager.data.platform.manager.CredentialExchangeRegistryManager
import com.universalpasswordmanager.data.platform.manager.FeatureFlagManager
import com.universalpasswordmanager.data.platform.manager.FirstTimeActionManager
import com.universalpasswordmanager.data.platform.manager.PolicyManager
import com.universalpasswordmanager.data.platform.manager.PushManager
import com.universalpasswordmanager.data.platform.manager.policy.UserNotificationPolicyManager
import com.universalpasswordmanager.data.tools.generator.datasource.disk.GeneratorDiskSource
import com.universalpasswordmanager.data.tools.generator.datasource.disk.PasswordHistoryDiskSource
import com.universalpasswordmanager.data.vault.datasource.disk.VaultDiskSource
import com.universalpasswordmanager.data.vault.datasource.sdk.VaultSdkSource
import com.universalpasswordmanager.data.vault.manager.VaultLockManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

/**
 * Provides managers in the auth package.
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthManagerModule {

    @Provides
    @Singleton
    fun provideAuthRequestNotificationManager(
        @ApplicationContext context: Context,
        authDiskSource: AuthDiskSource,
        pushManager: PushManager,
        dispatcherManager: DispatcherManager,
    ): AuthRequestNotificationManager =
        AuthRequestNotificationManagerImpl(
            context = context,
            authDiskSource = authDiskSource,
            pushManager = pushManager,
            dispatcherManager = dispatcherManager,
        )

    @Provides
    @Singleton
    fun provideAuthRequestManager(
        clock: Clock,
        authRequestsService: AuthRequestsService,
        newAuthRequestService: NewAuthRequestService,
        authSdkSource: AuthSdkSource,
        vaultSdkSource: VaultSdkSource,
        authDiskSource: AuthDiskSource,
        pushManager: PushManager,
    ): AuthRequestManager =
        AuthRequestManagerImpl(
            clock = clock,
            authRequestsService = authRequestsService,
            newAuthRequestService = newAuthRequestService,
            authSdkSource = authSdkSource,
            vaultSdkSource = vaultSdkSource,
            authDiskSource = authDiskSource,
            pushManager = pushManager,
        )

    @Provides
    @Singleton
    fun providesAuthStateManager(
        authDiskSource: AuthDiskSource,
        dispatcherManager: DispatcherManager,
    ): AuthStateManager = AuthStateManagerImpl(
        authDiskSource = authDiskSource,
        dispatcherManager = dispatcherManager,
    )

    @Provides
    @Singleton
    fun provideKeyConnectorManager(
        accountsService: AccountsService,
        authSdkSource: AuthSdkSource,
        vaultSdkSource: VaultSdkSource,
        featureFlagManager: FeatureFlagManager,
    ): KeyConnectorManager =
        KeyConnectorManagerImpl(
            accountsService = accountsService,
            authSdkSource = authSdkSource,
            vaultSdkSource = vaultSdkSource,
            featureFlagManager = featureFlagManager,
        )

    @Provides
    @Singleton
    fun provideTrustedDeviceManager(
        authDiskSource: AuthDiskSource,
        vaultSdkSource: VaultSdkSource,
        devicesService: DevicesService,
    ): TrustedDeviceManager =
        TrustedDeviceManagerImpl(
            authDiskSource = authDiskSource,
            vaultSdkSource = vaultSdkSource,
            devicesService = devicesService,
        )

    @Provides
    @Singleton
    fun provideUserLogoutManager(
        authDiskSource: AuthDiskSource,
        generatorDiskSource: GeneratorDiskSource,
        passwordHistoryDiskSource: PasswordHistoryDiskSource,
        pushDiskSource: PushDiskSource,
        settingsDiskSource: SettingsDiskSource,
        toastManager: ToastManager,
        vaultDiskSource: VaultDiskSource,
        vaultSdkSource: VaultSdkSource,
        dispatcherManager: DispatcherManager,
        credentialExchangeRegistryManager: CredentialExchangeRegistryManager,
        userNotificationPolicyManager: UserNotificationPolicyManager,
    ): UserLogoutManager =
        UserLogoutManagerImpl(
            authDiskSource = authDiskSource,
            generatorDiskSource = generatorDiskSource,
            passwordHistoryDiskSource = passwordHistoryDiskSource,
            pushDiskSource = pushDiskSource,
            settingsDiskSource = settingsDiskSource,
            toastManager = toastManager,
            vaultDiskSource = vaultDiskSource,
            vaultSdkSource = vaultSdkSource,
            dispatcherManager = dispatcherManager,
            credentialExchangeRegistryManager = credentialExchangeRegistryManager,
            userNotificationPolicyManager = userNotificationPolicyManager,
        )

    @Provides
    @Singleton
    fun providesAddTotpItemFromAuthenticatorManager(): AddTotpItemFromAuthenticatorManager =
        AddTotpItemFromAuthenticatorManagerImpl()

    @Provides
    @Singleton
    fun providesAuthTokenManager(
        authDiskSource: AuthDiskSource,
    ): AuthTokenManager = AuthTokenManagerImpl(authDiskSource = authDiskSource)

    @Provides
    @Singleton
    fun providesKdfManager(
        authDiskSource: AuthDiskSource,
        vaultSdkSource: VaultSdkSource,
        accountsService: AccountsService,
    ): KdfManager = KdfManagerImpl(
        authDiskSource = authDiskSource,
        vaultSdkSource = vaultSdkSource,
        accountsService = accountsService,
    )

    @Provides
    @Singleton
    fun providesOrganizationManager(
        authDiskSource: AuthDiskSource,
        organizationService: OrganizationService,
    ): OrganizationManager = OrganizationManagerImpl(
        authDiskSource = authDiskSource,
        organizationService = organizationService,
    )

    @Provides
    @Singleton
    fun providesUserStateManager(
        authDiskSource: AuthDiskSource,
        firstTimeActionManager: FirstTimeActionManager,
        vaultLockManager: VaultLockManager,
        policyManager: PolicyManager,
        dispatcherManager: DispatcherManager,
    ): UserStateManager = UserStateManagerImpl(
        authDiskSource = authDiskSource,
        firstTimeActionManager = firstTimeActionManager,
        vaultLockManager = vaultLockManager,
        policyManager = policyManager,
        dispatcherManager = dispatcherManager,
    )
}
