package com.work.model

import androidx.annotation.Keep
import .core.data.serializer.BaseEnumeratedIntSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The type of provider.
 */
@Serializable(with = ProviderTypeSerializer::class)
enum class ProviderType {
    /**
     * Managed Service Provider - sells and manages its clients' Universal Password Manager organizations.
     */
    @SerialName("0")
    MSP,

    /**
     * Reseller partner - sells Universal Password Manager to its clients but does not have any administrative
     * access.
     */
    @SerialName("1")
    RESELLER,

    /**
     * Business unit provider - used to manage multiple organizations which form part of a single
     * large enterprise.
     */
    @SerialName("2")
    BUSINESS_UNIT,
}

@Keep
private class ProviderTypeSerializer : BaseEnumeratedIntSerializer<ProviderType>(
    className = "ProviderType",
    values = ProviderType.entries.toTypedArray(),
)
