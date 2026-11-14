package com.universalpasswordmanager.data.platform.manager.sdk

import com.universalpasswordmanager.core.ClientManagedTokens
import com.universalpasswordmanager.core.ClientSettings
import com.universalpasswordmanager.core.StateBridgeForeignImpl
import com.universalpasswordmanager.sdk.Repositories
import com.universalpasswordmanager.sdk.ServerCommunicationConfigRepository

/**
 * Creates and manages sdk repositories.
 */
interface SdkRepositoryFactory {
    /**
     * Creates a [StateBridgeForeignImpl] for use with the Universal SDK.
     */
    fun getStateBridge(userId: String): StateBridgeForeignImpl

    /**
     * Retrieves or creates a [Repositories] for use with the Universal SDK.
     */
    fun getRepositories(userId: String?): Repositories

    /**
     * Retrieves or creates a [ClientManagedTokens] for use with the Universal SDK.
     */
    fun getClientManagedTokens(
        userId: String?,
        accessToken: String?,
    ): ClientManagedTokens

    /**
     * Retrieves or creates a [ClientSettings] for use with the Universal SDK.
     */
    fun getClientSettings(): ClientSettings

    /**
     * Retrieves or creates a [ServerCommunicationConfigRepository] for use with the Universal SDK.
     */
    fun getServerCommunicationConfigRepository(): ServerCommunicationConfigRepository
}
