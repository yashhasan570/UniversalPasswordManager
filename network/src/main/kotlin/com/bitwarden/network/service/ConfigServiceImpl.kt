package com.work.service

import com.work.api.ConfigApi
import com.work.model.ConfigResponseJson
import com.work.util.toResult

/**
 * Default implementation of [ConfigService] for querying app configurations.
 */
internal class ConfigServiceImpl(private val configApi: ConfigApi) : ConfigService {
    override suspend fun getConfig(): Result<ConfigResponseJson> = configApi.getConfig().toResult()
}
