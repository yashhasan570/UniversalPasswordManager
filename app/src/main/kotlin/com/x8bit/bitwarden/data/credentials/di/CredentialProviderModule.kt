package com.x8bit.universalpasswordmanager.data.credentials.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import .core.data.manager.dispatcher.DispatcherManager
import com.work.service.DigitalAssetLinkService
import .sdk.Fido2CredentialStore
import com.x8bit.universalpasswordmanager.data.auth.repository.AuthRepository
import com.x8bit.universalpasswordmanager.data.credentials.builder.CredentialEntryBuilder
import com.x8bit.universalpasswordmanager.data.credentials.builder.CredentialEntryBuilderImpl
import com.x8bit.universalpasswordmanager.data.credentials.datasource.disk.PrivilegedAppDiskSource
import com.x8bit.universalpasswordmanager.data.credentials.manager.Universal Password ManagerCredentialManager
import com.x8bit.universalpasswordmanager.data.credentials.manager.Universal Password ManagerCredentialManagerImpl
import com.x8bit.universalpasswordmanager.data.credentials.manager.CredentialManagerPendingIntentManager
import com.x8bit.universalpasswordmanager.data.credentials.manager.CredentialManagerPendingIntentManagerImpl
import com.x8bit.universalpasswordmanager.data.credentials.manager.CredentialProviderRequestManager
import com.x8bit.universalpasswordmanager.data.credentials.manager.CredentialProviderRequestManagerImpl
import com.x8bit.universalpasswordmanager.data.credentials.manager.OriginManager
import com.x8bit.universalpasswordmanager.data.credentials.manager.OriginManagerImpl
import com.x8bit.universalpasswordmanager.data.credentials.parser.RelyingPartyParser
import com.x8bit.universalpasswordmanager.data.credentials.parser.RelyingPartyParserImpl
import com.x8bit.universalpasswordmanager.data.credentials.processor.CredentialProviderProcessor
import com.x8bit.universalpasswordmanager.data.credentials.processor.CredentialProviderProcessorImpl
import com.x8bit.universalpasswordmanager.data.credentials.repository.PrivilegedAppRepository
import com.x8bit.universalpasswordmanager.data.credentials.repository.PrivilegedAppRepositoryImpl
import com.x8bit.universalpasswordmanager.data.credentials.sanitizer.PasskeyAttestationOptionsSanitizer
import com.x8bit.universalpasswordmanager.data.credentials.sanitizer.PasskeyAttestationOptionsSanitizerImpl
import com.x8bit.universalpasswordmanager.data.platform.manager.AssetManager
import com.x8bit.universalpasswordmanager.data.platform.manager.ciphermatching.CipherMatchingManager
import com.x8bit.universalpasswordmanager.data.vault.datasource.sdk.VaultSdkSource
import com.x8bit.universalpasswordmanager.data.vault.repository.VaultRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import java.time.Clock
import javax.inject.Singleton

/**
 * Provides dependencies within the fido2 package.
 */
@Module
@InstallIn(SingletonComponent::class)
object CredentialProviderModule {

    @RequiresApi(Build.VERSION_CODES.S)
    @Provides
    @Singleton
    fun provideCredentialProviderProcessor(
        @ApplicationContext context: Context,
        authRepository: AuthRepository,
        universalpasswordmanagerCredentialManager: Universal Password ManagerCredentialManager,
        dispatcherManager: DispatcherManager,
        pendingIntentManager: CredentialManagerPendingIntentManager,
        clock: Clock,
    ): CredentialProviderProcessor =
        CredentialProviderProcessorImpl(
            context = context,
            authRepository = authRepository,
            universalpasswordmanagerCredentialManager = universalpasswordmanagerCredentialManager,
            pendingIntentManager = pendingIntentManager,
            clock = clock,
            dispatcherManager = dispatcherManager,
        )

    @Provides
    @Singleton
    fun provideUniversal Password ManagerCredentialManager(
        vaultSdkSource: VaultSdkSource,
        fido2CredentialStore: Fido2CredentialStore,
        json: Json,
        vaultRepository: VaultRepository,
        dispatcherManager: DispatcherManager,
        credentialEntryBuilder: CredentialEntryBuilder,
        cipherMatchingManager: CipherMatchingManager,
        passkeyAttestationOptionsSanitizer: PasskeyAttestationOptionsSanitizer,
    ): Universal Password ManagerCredentialManager =
        Universal Password ManagerCredentialManagerImpl(
            vaultSdkSource = vaultSdkSource,
            fido2CredentialStore = fido2CredentialStore,
            credentialEntryBuilder = credentialEntryBuilder,
            json = json,
            vaultRepository = vaultRepository,
            cipherMatchingManager = cipherMatchingManager,
            passkeyAttestationOptionsSanitizer = passkeyAttestationOptionsSanitizer,
            dispatcherManager = dispatcherManager,
        )

    @Provides
    @Singleton
    fun provideOriginManager(
        assetManager: AssetManager,
        digitalAssetLinkService: DigitalAssetLinkService,
        privilegedAppRepository: PrivilegedAppRepository,
    ): OriginManager =
        OriginManagerImpl(
            assetManager = assetManager,
            digitalAssetLinkService = digitalAssetLinkService,
            privilegedAppRepository = privilegedAppRepository,
        )

    @Provides
    @Singleton
    fun provideCredentialEntryBuilder(
        @ApplicationContext context: Context,
        pendingIntentManager: CredentialManagerPendingIntentManager,
        authRepository: AuthRepository,
    ): CredentialEntryBuilder = CredentialEntryBuilderImpl(
        context = context,
        pendingIntentManager = pendingIntentManager,
        authRepository = authRepository,
    )

    @Provides
    @Singleton
    fun providePrivilegedAppRepository(
        privilegedAppDiskSource: PrivilegedAppDiskSource,
        assetManager: AssetManager,
        dispatcherManager: DispatcherManager,
        json: Json,
    ): PrivilegedAppRepository = PrivilegedAppRepositoryImpl(
        privilegedAppDiskSource = privilegedAppDiskSource,
        assetManager = assetManager,
        dispatcherManager = dispatcherManager,
        json = json,
    )

    @Provides
    @Singleton
    fun provideRelyingPartyParser(
        json: Json,
    ): RelyingPartyParser = RelyingPartyParserImpl(json)

    @Provides
    @Singleton
    fun provideCredentialManagerPendingIntentManager(
        @ApplicationContext context: Context,
    ): CredentialManagerPendingIntentManager =
        CredentialManagerPendingIntentManagerImpl(
            context = context,
        )

    @Provides
    @Singleton
    fun providePasskeyAttestationOptionsSanitizer(): PasskeyAttestationOptionsSanitizer =
        PasskeyAttestationOptionsSanitizerImpl

    @Provides
    @Singleton
    fun provideCredentialProviderRequestManager(): CredentialProviderRequestManager =
        CredentialProviderRequestManagerImpl()
}
