package com.universalpasswordmanager.data.vault.datasource.sdk

import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.core.data.manager.dispatcher.DispatcherManager
import com.universalpasswordmanager.core.data.util.asSuccess
import com.universalpasswordmanager.data.manager.NativeLibraryManager
import com.universalpasswordmanager.data.platform.manager.FeatureFlagManager
import com.universalpasswordmanager.data.platform.manager.SdkClientManagerImpl
import com.universalpasswordmanager.data.platform.manager.sdk.SdkPlatformApiFactory
import com.universalpasswordmanager.data.platform.manager.sdk.SdkRepositoryFactory
import com.universalpasswordmanager.data.platform.manager.sdk.log.SdkLoggerFactory

/**
 * The default instance of the [ScopedVaultSdkSource]. This uses its own instance of the
 * [SdkClientManagerImpl] to keep it separate from the rest of the app.
 */
@OmitFromCoverage
class ScopedVaultSdkSourceImpl(
    dispatcherManager: DispatcherManager,
    featureFlagManager: FeatureFlagManager,
    sdkRepositoryFactory: SdkRepositoryFactory,
    sdkPlatformApiFactory: SdkPlatformApiFactory,
    sdkLoggerFactory: SdkLoggerFactory,
    vaultSdkSource: VaultSdkSource = VaultSdkSourceImpl(
        sdkClientManager = SdkClientManagerImpl(
            dispatcherManager = dispatcherManager,
            // We do not want to have the real NativeLibraryManager used here to avoid
            // initializing the library twice.
            nativeLibraryManager = object : NativeLibraryManager {
                override fun loadLibrary(libraryName: String): Result<Unit> = Unit.asSuccess()
            },
            sdkRepoFactory = sdkRepositoryFactory,
            featureFlagManager = featureFlagManager,
            sdkPlatformApiFactory = sdkPlatformApiFactory,
            sdkLoggerFactory = sdkLoggerFactory,
        ),
        dispatcherManager = dispatcherManager,
    ),
) : ScopedVaultSdkSource, VaultSdkSource by vaultSdkSource
