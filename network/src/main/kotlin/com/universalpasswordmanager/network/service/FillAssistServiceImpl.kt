package com.universalpasswordmanager.network.service

import com.universalpasswordmanager.network.api.FillAssistApi
import com.universalpasswordmanager.network.model.FillAssistFormsJson
import com.universalpasswordmanager.network.model.FillAssistManifestJson
import com.universalpasswordmanager.network.util.toResult

/**
 * Default implementation of [FillAssistService].
 */
internal class FillAssistServiceImpl(
    private val api: FillAssistApi,
) : FillAssistService {

    override suspend fun getManifest(): Result<FillAssistManifestJson> =
        api.getManifest().toResult()

    override suspend fun getForms(filename: String): Result<FillAssistFormsJson> =
        api.getForms(filename = filename).toResult()
}
