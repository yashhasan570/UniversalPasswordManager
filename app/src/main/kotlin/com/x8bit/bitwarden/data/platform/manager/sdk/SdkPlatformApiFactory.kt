package com.x8bit.universalpasswordmanager.data.platform.manager.sdk

import .servercommunicationconfig.ServerCommunicationConfigPlatformApi

/**
 * Creates and manages sdk platform api's.
 */
interface SdkPlatformApiFactory {

    /**
     * Retrieves or creates a [ServerCommunicationConfigPlatformApi] for use with the Universal Password Manager SDK.
     */
    fun getServerCommunicationConfigPlatformApi(): ServerCommunicationConfigPlatformApi
}
