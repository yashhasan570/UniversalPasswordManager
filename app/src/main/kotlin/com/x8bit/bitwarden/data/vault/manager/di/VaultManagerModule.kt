package com.x8bit.universalpasswordmanager.data.vault.manager.di

import android.content.Context
import .core.data.manager.dispatcher.DispatcherManager
import .core.data.manager.realtime.RealtimeManager
import .cxf.parser.CredentialExchangePayloadParser
import .data.manager.appstate.AppStateManager
import .data.manager.file.FileManager
import com.work.service.CiphersService
import com.work.service.DownloadService
import com.work.service.FolderService
import com.work.service.SendsService
import com.work.service.SyncService
import .ui.platform.feature.cardscanner.manager.CardScanManager
import .ui.platform.feature.cardscanner.manager.CardScanManagerImpl
import com.x8bit.universalpasswordmanager.data.auth.datasource.disk.AuthDiskSource
import com.x8bit.universalpasswordmanager.data.auth.datasource.sdk.AuthSdkSource
import com.x8bit.universalpasswordmanager.data.auth.manager.KdfManager
import com.x8bit.universalpasswordmanager.data.auth.manager.TrustedDeviceManager
import com.x8bit.universalpasswordmanager.data.auth.manager.UserLogoutManager
import com.x8bit.universalpasswordmanager.data.auth.manager.UserStateManager
import com.x8bit.universalpasswordmanager.data.autofill.manager.FillAssistManager
import com.x8bit.universalpasswordmanager.data.platform.datasource.disk.SettingsDiskSource
import com.x8bit.universalpasswordmanager.data.platform.manager.DatabaseSchemeManager
import com.x8bit.universalpasswordmanager.data.platform.manager.PolicyManager
import com.x8bit.universalpasswordmanager.data.platform.manager.PushManager
import com.x8bit.universalpasswordmanager.data.platform.manager.ReviewPromptManager
import com.x8bit.universalpasswordmanager.data.platform.manager.keyrotation.KeyRotationManager
import com.x8bit.universalpasswordmanager.data.platform.manager.network.NetworkConnectionManager
import com.x8bit.universalpasswordmanager.data.platform.manager.policy.PasswordPolicyManager
import com.x8bit.universalpasswordmanager.data.platform.repository.SettingsRepository
import com.x8bit.universalpasswordmanager.data.vault.datasource.disk.VaultDiskSource
import com.x8bit.universalpasswordmanager.data.vault.datasource.sdk.VaultSdkSource
import com.x8bit.universalpasswordmanager.data.vault.manager.CipherManager
import com.x8bit.universalpasswordmanager.data.vault.manager.CipherManagerImpl
import com.x8bit.universalpasswordmanager.data.vault.manager.CredentialExchangeImportManager
import com.x8bit.universalpasswordmanager.data.vault.manager.CredentialExchangeImportManagerImpl
import com.x8bit.universalpasswordmanager.data.vault.manager.FolderManager
import com.x8bit.universalpasswordmanager.data.vault.manager.FolderManagerImpl
import com.x8bit.universalpasswordmanager.data.vault.manager.PinProtectedUserKeyManager
import com.x8bit.universalpasswordmanager.data.vault.manager.PinProtectedUserKeyManagerImpl
import com.x8bit.universalpasswordmanager.data.vault.manager.SendManager
import com.x8bit.universalpasswordmanager.data.vault.manager.SendManagerImpl
import com.x8bit.universalpasswordmanager.data.vault.manager.TotpCodeManager
import com.x8bit.universalpasswordmanager.data.vault.manager.TotpCodeManagerImpl
import com.x8bit.universalpasswordmanager.data.vault.manager.VaultLockManager
import com.x8bit.universalpasswordmanager.data.vault.manager.VaultLockManagerImpl
import com.x8bit.universalpasswordmanager.data.vault.manager.VaultMigrationManager
import com.x8bit.universalpasswordmanager.data.vault.manager.VaultMigrationManagerImpl
import com.x8bit.universalpasswordmanager.data.vault.manager.VaultSyncManager
import com.x8bit.universalpasswordmanager.data.vault.manager.VaultSyncManagerImpl
import com.x8bit.universalpasswordmanager.data.vault.repository.VaultRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

/**
 * Provides managers in the vault package.
 */
@Module
@InstallIn(SingletonComponent::class)
object VaultManagerModule {

    @Provides
    @Singleton
    fun provideCardScanManager(): CardScanManager = CardScanManagerImpl()

    @Provides
    @Singleton
    fun provideVaultMigrationManager(
        authDiskSource: AuthDiskSource,
        vaultDiskSource: VaultDiskSource,
        vaultRepository: VaultRepository,
        vaultSdkSource: VaultSdkSource,
        ciphersService: CiphersService,
        settingsDiskSource: SettingsDiskSource,
        vaultLockManager: VaultLockManager,
        policyManager: PolicyManager,
        connectionManager: NetworkConnectionManager,
        dispatcherManager: DispatcherManager,
    ): VaultMigrationManager = VaultMigrationManagerImpl(
        authDiskSource = authDiskSource,
        vaultDiskSource = vaultDiskSource,
        vaultRepository = vaultRepository,
        vaultSdkSource = vaultSdkSource,
        ciphersService = ciphersService,
        settingsDiskSource = settingsDiskSource,
        vaultLockManager = vaultLockManager,
        policyManager = policyManager,
        connectionManager = connectionManager,
        dispatcherManager = dispatcherManager,
    )

    @Provides
    @Singleton
    fun provideCipherManager(
        downloadService: DownloadService,
        ciphersService: CiphersService,
        settingsDiskSource: SettingsDiskSource,
        vaultDiskSource: VaultDiskSource,
        vaultSdkSource: VaultSdkSource,
        authDiskSource: AuthDiskSource,
        fileManager: FileManager,
        clock: Clock,
        reviewPromptManager: ReviewPromptManager,
        dispatcherManager: DispatcherManager,
        pushManager: PushManager,
    ): CipherManager = CipherManagerImpl(
        downloadService = downloadService,
        fileManager = fileManager,
        settingsDiskSource = settingsDiskSource,
        authDiskSource = authDiskSource,
        ciphersService = ciphersService,
        vaultDiskSource = vaultDiskSource,
        vaultSdkSource = vaultSdkSource,
        clock = clock,
        reviewPromptManager = reviewPromptManager,
        dispatcherManager = dispatcherManager,
        pushManager = pushManager,
    )

    @Provides
    @Singleton
    fun provideFolderManager(
        folderService: FolderService,
        settingsDiskSource: SettingsDiskSource,
        vaultDiskSource: VaultDiskSource,
        vaultSdkSource: VaultSdkSource,
        authDiskSource: AuthDiskSource,
        dispatcherManager: DispatcherManager,
        pushManager: PushManager,
    ): FolderManager = FolderManagerImpl(
        authDiskSource = authDiskSource,
        settingsDiskSource = settingsDiskSource,
        folderService = folderService,
        vaultDiskSource = vaultDiskSource,
        vaultSdkSource = vaultSdkSource,
        dispatcherManager = dispatcherManager,
        pushManager = pushManager,
    )

    @Provides
    @Singleton
    fun provideSendManager(
        sendsService: SendsService,
        vaultDiskSource: VaultDiskSource,
        vaultSdkSource: VaultSdkSource,
        authDiskSource: AuthDiskSource,
        settingsDiskSource: SettingsDiskSource,
        fileManager: FileManager,
        reviewPromptManager: ReviewPromptManager,
        pushManager: PushManager,
        dispatcherManager: DispatcherManager,
    ): SendManager = SendManagerImpl(
        fileManager = fileManager,
        authDiskSource = authDiskSource,
        settingsDiskSource = settingsDiskSource,
        sendsService = sendsService,
        vaultDiskSource = vaultDiskSource,
        vaultSdkSource = vaultSdkSource,
        reviewPromptManager = reviewPromptManager,
        pushManager = pushManager,
        dispatcherManager = dispatcherManager,
    )

    @Provides
    @Singleton
    fun provideVaultLockManager(
        @ApplicationContext context: Context,
        clock: Clock,
        realtimeManager: RealtimeManager,
        authDiskSource: AuthDiskSource,
        authSdkSource: AuthSdkSource,
        vaultSdkSource: VaultSdkSource,
        settingsRepository: SettingsRepository,
        appStateManager: AppStateManager,
        userLogoutManager: UserLogoutManager,
        dispatcherManager: DispatcherManager,
        trustedDeviceManager: TrustedDeviceManager,
        kdfManager: KdfManager,
        pinProtectedUserKeyManager: PinProtectedUserKeyManager,
        passwordPolicyManager: PasswordPolicyManager,
        keyRotationManager: KeyRotationManager,
    ): VaultLockManager =
        VaultLockManagerImpl(
            context = context,
            clock = clock,
            realtimeManager = realtimeManager,
            authDiskSource = authDiskSource,
            authSdkSource = authSdkSource,
            vaultSdkSource = vaultSdkSource,
            settingsRepository = settingsRepository,
            appStateManager = appStateManager,
            userLogoutManager = userLogoutManager,
            dispatcherManager = dispatcherManager,
            trustedDeviceManager = trustedDeviceManager,
            kdfManager = kdfManager,
            pinProtectedUserKeyManager = pinProtectedUserKeyManager,
            passwordPolicyManager = passwordPolicyManager,
            keyRotationManager = keyRotationManager,
        )

    @Provides
    @Singleton
    fun providePinProtectedUserKeyManager(
        authDiskSource: AuthDiskSource,
        vaultSdkSource: VaultSdkSource,
    ): PinProtectedUserKeyManager =
        PinProtectedUserKeyManagerImpl(
            authDiskSource = authDiskSource,
            vaultSdkSource = vaultSdkSource,
        )

    @Provides
    @Singleton
    fun provideTotpManager(
        vaultSdkSource: VaultSdkSource,
        dispatcherManager: DispatcherManager,
        clock: Clock,
    ): TotpCodeManager =
        TotpCodeManagerImpl(
            vaultSdkSource = vaultSdkSource,
            dispatcherManager = dispatcherManager,
            clock = clock,
        )

    @Provides
    @Singleton
    fun provideVaultSyncManager(
        fillAssistManager: FillAssistManager,
        syncService: SyncService,
        settingsDiskSource: SettingsDiskSource,
        authDiskSource: AuthDiskSource,
        vaultDiskSource: VaultDiskSource,
        vaultSdkSource: VaultSdkSource,
        userLogoutManager: UserLogoutManager,
        userStateManager: UserStateManager,
        vaultLockManager: VaultLockManager,
        clock: Clock,
        databaseSchemeManager: DatabaseSchemeManager,
        pushManager: PushManager,
        dispatcherManager: DispatcherManager,
    ): VaultSyncManager = VaultSyncManagerImpl(
        fillAssistManager = fillAssistManager,
        syncService = syncService,
        settingsDiskSource = settingsDiskSource,
        authDiskSource = authDiskSource,
        vaultDiskSource = vaultDiskSource,
        vaultSdkSource = vaultSdkSource,
        userLogoutManager = userLogoutManager,
        userStateManager = userStateManager,
        vaultLockManager = vaultLockManager,
        clock = clock,
        databaseSchemeManager = databaseSchemeManager,
        pushManager = pushManager,
        dispatcherManager = dispatcherManager,
    )

    @Provides
    @Singleton
    fun provideCredentialExchangeImportManager(
        vaultSdkSource: VaultSdkSource,
        ciphersService: CiphersService,
        vaultSyncManager: VaultSyncManager,
        policyManager: PolicyManager,
        credentialExchangePayloadParser: CredentialExchangePayloadParser,
    ): CredentialExchangeImportManager = CredentialExchangeImportManagerImpl(
        vaultSdkSource = vaultSdkSource,
        ciphersService = ciphersService,
        vaultSyncManager = vaultSyncManager,
        policyManager = policyManager,
        credentialExchangePayloadParser = credentialExchangePayloadParser,
    )
}
