package com.universalpasswordmanager.network.service

import com.universalpasswordmanager.network.api.ConfigApi
import com.universalpasswordmanager.network.model.ConfigResponseJson
import com.universalpasswordmanager.network.util.toResult

/**
 * Default implementation of [ConfigService] for querying app configurations.
 */
internal class ConfigServiceImpl(private val configApi: ConfigApi) : ConfigService {
    override suspend fun getConfig(): Result<ConfigResponseJson> = configApi.getConfig().toResult()
}
