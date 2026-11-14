package com.universalpasswordmanager.data.platform.manager.sdk

import com.universalpasswordmanager.servercommunicationconfig.ServerCommunicationConfigPlatformApi
import com.universalpasswordmanager.data.platform.manager.CookieAcquisitionRequestManager
import com.universalpasswordmanager.data.platform.manager.sdk.platformapi.ServerCommunicationConfigPlatformApiImpl

/**
 * Factory for creating and managing sdk platform api's.
 */
class SdkPlatformApiFactoryImpl(
    private val serverCommConfigManager: CookieAcquisitionRequestManager,
) : SdkPlatformApiFactory {
    /**
     * Retrieves or creates a [ServerCommunicationConfigPlatformApi] for use with the Universal SDK.
     */
    override fun getServerCommunicationConfigPlatformApi(): ServerCommunicationConfigPlatformApi =
        ServerCommunicationConfigPlatformApiImpl(
            serverCommConfigManager = serverCommConfigManager,
        )
}
