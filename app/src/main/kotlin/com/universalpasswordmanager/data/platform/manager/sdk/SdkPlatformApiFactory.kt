package com.universalpasswordmanager.data.platform.manager.sdk

import com.universalpasswordmanager.servercommunicationconfig.ServerCommunicationConfigPlatformApi

/**
 * Creates and manages sdk platform api's.
 */
interface SdkPlatformApiFactory {

    /**
     * Retrieves or creates a [ServerCommunicationConfigPlatformApi] for use with the Universal SDK.
     */
    fun getServerCommunicationConfigPlatformApi(): ServerCommunicationConfigPlatformApi
}
