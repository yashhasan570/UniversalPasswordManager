package com.universalpasswordmanager.data.platform.manager.sdk

import com.universalpasswordmanager.core.ClientManagedTokens
import com.universalpasswordmanager.core.ClientSettings
import com.universalpasswordmanager.core.DeviceType
import com.universalpasswordmanager.core.StateBridgeForeignImpl
import com.universalpasswordmanager.data.datasource.disk.ConfigDiskSource
import com.universalpasswordmanager.network.model.UniversalServiceClientConfig
import com.universalpasswordmanager.sdk.Repositories
import com.universalpasswordmanager.sdk.ServerCommunicationConfigRepository
import com.universalpasswordmanager.data.auth.datasource.disk.AuthDiskSource
import com.universalpasswordmanager.data.platform.datasource.disk.CookieDiskSource
import com.universalpasswordmanager.data.platform.manager.sdk.repository.SdkCipherRepository
import com.universalpasswordmanager.data.platform.manager.sdk.repository.SdkLocalUserDataKeyStateRepository
import com.universalpasswordmanager.data.platform.manager.sdk.repository.SdkTokenRepository
import com.universalpasswordmanager.data.platform.manager.sdk.repository.ServerCommunicationConfigRepositoryImpl
import com.universalpasswordmanager.data.platform.manager.sdk.statebridge.SdkStateBridge
import com.universalpasswordmanager.data.vault.datasource.disk.VaultDiskSource

/**
 * The default implementation for the [SdkRepositoryFactory].
 */
class SdkRepositoryFactoryImpl(
    private val vaultDiskSource: VaultDiskSource,
    private val cookieDiskSource: CookieDiskSource,
    private val configDiskSource: ConfigDiskSource,
    private val authDiskSource: AuthDiskSource,
    private val serviceClientConfig: UniversalServiceClientConfig,
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
            bitwardenClientVersion = serviceClientConfig.clientData.clientVersion,
            bitwardenPackageType = null,
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
