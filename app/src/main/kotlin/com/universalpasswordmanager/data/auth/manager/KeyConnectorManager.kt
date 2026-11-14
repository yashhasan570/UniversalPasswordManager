package com.universalpasswordmanager.data.auth.manager

import com.universalpasswordmanager.crypto.Kdf
import com.universalpasswordmanager.network.model.AccountKeysJson
import com.universalpasswordmanager.network.model.KdfTypeJson
import com.universalpasswordmanager.data.auth.manager.model.MigrateExistingUserToKeyConnectorResult
import com.universalpasswordmanager.data.auth.manager.model.MigrateNewUserToKeyConnectorResult

/**
 * Manager used to interface with a key connector.
 */
interface KeyConnectorManager {
    /**
     * Migrates an existing user to use the key connector.
     */
    @Suppress("LongParameterList")
    suspend fun migrateExistingUserToKeyConnector(
        userId: String,
        url: String,
        userKeyEncrypted: String,
        salt: String,
        masterPassword: String,
        kdf: Kdf,
    ): Result<MigrateExistingUserToKeyConnectorResult>

    /**
     * Migrates a new user to use the key connector.
     */
    @Suppress("LongParameterList")
    suspend fun migrateNewUserToKeyConnector(
        userId: String,
        accountKeys: AccountKeysJson?,
        url: String,
        accessToken: String,
        kdfType: KdfTypeJson,
        kdfIterations: Int?,
        kdfMemory: Int?,
        kdfParallelism: Int?,
        organizationIdentifier: String,
    ): Result<MigrateNewUserToKeyConnectorResult>
}
