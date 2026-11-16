package com.x8bit.universalpasswordmanager.data.platform.provider

import .data.repository.util.baseApiUrl
import .data.repository.util.baseEventsUrl
import .data.repository.util.baseIdentityUrl
import .data.repository.util.toEnvironmentUrlsOrDefault
import com.work.interceptor.BaseUrlsProvider
import com.x8bit.universalpasswordmanager.data.platform.datasource.disk.EnvironmentDiskSource

/**
 * The default implementation of [BaseUrlsProvider].
 */
class BaseUrlsProviderImpl(
    private val environmentDiskSource: EnvironmentDiskSource,
) : BaseUrlsProvider {

    override fun getBaseApiUrl(): String = environmentDiskSource
        .preAuthEnvironmentUrlData
        .toEnvironmentUrlsOrDefault()
        .baseApiUrl

    override fun getBaseIdentityUrl(): String = environmentDiskSource
        .preAuthEnvironmentUrlData
        .toEnvironmentUrlsOrDefault()
        .baseIdentityUrl

    override fun getBaseEventsUrl(): String = environmentDiskSource
        .preAuthEnvironmentUrlData
        .toEnvironmentUrlsOrDefault()
        .baseEventsUrl

    override fun getBaseFillAssistUrl(): String? = environmentDiskSource.fillAssistRulesUrl
}
