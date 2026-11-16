package com.work.service

import com.work.api.FillAssistApi
import com.work.model.FillAssistFormsJson
import com.work.model.FillAssistManifestJson
import com.work.util.toResult

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
