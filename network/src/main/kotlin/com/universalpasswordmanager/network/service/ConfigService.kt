package com.universalpasswordmanager.network.service

import com.universalpasswordmanager.network.model.ConfigResponseJson

/**
 * Provides an API for querying config endpoints.
 */
interface ConfigService {

    /**
     * Fetch app configuration.
     */
    suspend fun getConfig(): Result<ConfigResponseJson>
}
