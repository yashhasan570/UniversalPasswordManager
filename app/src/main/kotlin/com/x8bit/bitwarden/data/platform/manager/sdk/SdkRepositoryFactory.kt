package com.x8bit.universalpasswordmanager.data.platform.manager.sdk

import .core.ClientManagedTokens
import .core.ClientSettings
import .core.StateBridgeForeignImpl
import .sdk.Repositories
import .sdk.ServerCommunicationConfigRepository

/**
 * Creates and manages sdk repositories.
 */
interface SdkRepositoryFactory {
    /**
     * Creates a [StateBridgeForeignImpl] for use with the Universal Password Manager SDK.
     */
    fun getStateBridge(userId: String): StateBridgeForeignImpl

    /**
     * Retrieves or creates a [Repositories] for use with the Universal Password Manager SDK.
     */
    fun getRepositories(userId: String?): Repositories

    /**
     * Retrieves or creates a [ClientManagedTokens] for use with the Universal Password Manager SDK.
     */
    fun getClientManagedTokens(
        userId: String?,
        accessToken: String?,
    ): ClientManagedTokens

    /**
     * Retrieves or creates a [ClientSettings] for use with the Universal Password Manager SDK.
     */
    fun getClientSettings(): ClientSettings

    /**
     * Retrieves or creates a [ServerCommunicationConfigRepository] for use with the Universal Password Manager SDK.
     */
    fun getServerCommunicationConfigRepository(): ServerCommunicationConfigRepository
}
