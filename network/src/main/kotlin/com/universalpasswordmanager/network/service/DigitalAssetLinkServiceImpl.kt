package com.universalpasswordmanager.network.service

import com.universalpasswordmanager.network.api.DigitalAssetLinkApi
import com.universalpasswordmanager.network.model.DigitalAssetLinkCheckResponseJson
import com.universalpasswordmanager.network.util.toResult

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
