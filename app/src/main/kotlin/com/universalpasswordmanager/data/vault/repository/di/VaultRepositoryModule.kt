package com.universalpasswordmanager.data.vault.repository.di

import com.universalpasswordmanager.core.data.manager.dispatcher.DispatcherManager
import com.universalpasswordmanager.data.auth.datasource.disk.AuthDiskSource
import com.universalpasswordmanager.data.platform.manager.FeatureFlagManager
import com.universalpasswordmanager.data.platform.manager.keyrotation.KeyRotationManager
import com.universalpasswordmanager.data.vault.datasource.disk.VaultDiskSource
import com.universalpasswordmanager.data.vault.datasource.sdk.VaultSdkSource
import com.universalpasswordmanager.data.vault.manager.CipherManager
import com.universalpasswordmanager.data.vault.manager.CredentialExchangeImportManager
import com.universalpasswordmanager.data.vault.manager.FolderManager
import com.universalpasswordmanager.data.vault.manager.PinProtectedUserKeyManager
import com.universalpasswordmanager.data.vault.manager.SendManager
import com.universalpasswordmanager.data.vault.manager.TotpCodeManager
import com.universalpasswordmanager.data.vault.manager.VaultLockManager
import com.universalpasswordmanager.data.vault.manager.VaultSyncManager
import com.universalpasswordmanager.data.vault.repository.VaultRepository
import com.universalpasswordmanager.data.vault.repository.VaultRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides repositories in the vault package.
 */
@Module
@InstallIn(SingletonComponent::class)
object VaultRepositoryModule {

    @Provides
    @Singleton
    fun providesVaultRepository(
        vaultDiskSource: VaultDiskSource,
        vaultSdkSource: VaultSdkSource,
        authDiskSource: AuthDiskSource,
        cipherManager: CipherManager,
        folderManager: FolderManager,
        sendManager: SendManager,
        vaultLockManager: VaultLockManager,
        dispatcherManager: DispatcherManager,
        totpCodeManager: TotpCodeManager,
        vaultSyncManager: VaultSyncManager,
        credentialExchangeImportManager: CredentialExchangeImportManager,
        pinProtectedUserKeyManager: PinProtectedUserKeyManager,
        keyRotationManager: KeyRotationManager,
        featureFlagManager: FeatureFlagManager,
    ): VaultRepository = VaultRepositoryImpl(
        vaultDiskSource = vaultDiskSource,
        vaultSdkSource = vaultSdkSource,
        authDiskSource = authDiskSource,
        cipherManager = cipherManager,
        folderManager = folderManager,
        sendManager = sendManager,
        vaultLockManager = vaultLockManager,
        dispatcherManager = dispatcherManager,
        totpCodeManager = totpCodeManager,
        vaultSyncManager = vaultSyncManager,
        credentialExchangeImportManager = credentialExchangeImportManager,
        pinProtectedUserKeyManager = pinProtectedUserKeyManager,
        keyRotationManager = keyRotationManager,
        featureFlagManager = featureFlagManager,
    )
}
