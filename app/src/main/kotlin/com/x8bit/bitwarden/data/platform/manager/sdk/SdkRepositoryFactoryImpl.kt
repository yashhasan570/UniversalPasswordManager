package com.x8bit.universalpasswordmanager.data.platform.manager.sdk

import .core.ClientManagedTokens
import .core.ClientSettings
import .core.DeviceType
import .core.StateBridgeForeignImpl
import .data.datasource.disk.ConfigDiskSource
import com.work.model.Universal Password ManagerServiceClientConfig
import .sdk.Repositories
import .sdk.ServerCommunicationConfigRepository
import com.x8bit.universalpasswordmanager.data.auth.datasource.disk.AuthDiskSource
import com.x8bit.universalpasswordmanager.data.platform.datasource.disk.CookieDiskSource
import com.x8bit.universalpasswordmanager.data.platform.manager.sdk.repository.SdkCipherRepository
import com.x8bit.universalpasswordmanager.data.platform.manager.sdk.repository.SdkLocalUserDataKeyStateRepository
import com.x8bit.universalpasswordmanager.data.platform.manager.sdk.repository.SdkTokenRepository
import com.x8bit.universalpasswordmanager.data.platform.manager.sdk.repository.ServerCommunicationConfigRepositoryImpl
import com.x8bit.universalpasswordmanager.data.platform.manager.sdk.statebridge.SdkStateBridge
import com.x8bit.universalpasswordmanager.data.vault.datasource.disk.VaultDiskSource

/**
 * The default implementation for the [SdkRepositoryFactory].
 */
class SdkRepositoryFactoryImpl(
    private val vaultDiskSource: VaultDiskSource,
    private val cookieDiskSource: CookieDiskSource,
    private val configDiskSource: ConfigDiskSource,
    private val authDiskSource: AuthDiskSource,
    private val serviceClientConfig: Universal Password ManagerServiceClientConfig,
) : SdkRepositoryFactory {
    override fun getStateBridge(
        userId: String,
    ): StateBridgeForeignImpl =
        SdkStateBridge(
            userId = userId,
            authDiskSource = authDiskSource,
        )

    override fun getRepositories(userId: String?): Repositories =
        Repositories(
            cipher = getSdkCipherRepository(userId = userId),
            folder = null,
            localUserDataKeyState = SdkLocalUserDataKeyStateRepository(
                authDiskSource = authDiskSource,
            ),
            organization = null,
            organizationSharedKey = null,
            policy = null,
            send = null,
        )

    override fun getClientManagedTokens(
        userId: String?,
        accessToken: String?,
    ): ClientManagedTokens =
        SdkTokenRepository(
            userId = userId,
            accessToken = accessToken,
            authDiskSource = authDiskSource,
        )

    override fun getClientSettings(): ClientSettings =
        ClientSettings(
            identityUrl = serviceClientConfig.baseUrlsProvider.getBaseIdentityUrl(),
            apiUrl = serviceClientConfig.baseUrlsProvider.getBaseApiUrl(),
            userAgent = serviceClientConfig.clientData.userAgent,
            deviceType = DeviceType.ANDROID,
            deviceIdentifier = serviceClientConfig.appIdProvider.uniqueAppId,
            universalpasswordmanagerClientVersion = serviceClientConfig.clientData.clientVersion,
            universalpasswordmanagerPackageType = null,
        )

    override fun getServerCommunicationConfigRepository(): ServerCommunicationConfigRepository =
        ServerCommunicationConfigRepositoryImpl(
            cookieDiskSource = cookieDiskSource,
            configDiskSource = configDiskSource,
        )

    private fun getSdkCipherRepository(
        userId: String?,
    ): SdkCipherRepository? = userId?.let {
        SdkCipherRepository(userId = it, vaultDiskSource = vaultDiskSource)
    }
}
