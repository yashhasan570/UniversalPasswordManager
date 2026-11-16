package com.work.service

import com.work.api.DigitalAssetLinkApi
import com.work.model.DigitalAssetLinkCheckResponseJson
import com.work.util.toResult

/**
 * Primary implementation of [DigitalAssetLinkService].
 */
internal class DigitalAssetLinkServiceImpl(
    private val digitalAssetLinkApi: DigitalAssetLinkApi,
) : DigitalAssetLinkService {

    override suspend fun checkDigitalAssetLinksRelations(
        sourceWebSite: String,
        targetPackageName: String,
        targetCertificateFingerprint: String,
        relations: List<String>,
    ): Result<DigitalAssetLinkCheckResponseJson> = digitalAssetLinkApi
        .checkDigitalAssetLinksRelations(
            sourceWebSite = sourceWebSite,
            targetPackageName = targetPackageName,
            targetCertificateFingerprint = targetCertificateFingerprint,
            relations = relations,
        )
        .toResult()
}
