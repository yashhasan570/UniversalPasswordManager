package com.universalpasswordmanager.data.platform.provider

import com.universalpasswordmanager.data.repository.util.baseApiUrl
import com.universalpasswordmanager.data.repository.util.baseEventsUrl
import com.universalpasswordmanager.data.repository.util.baseIdentityUrl
import com.universalpasswordmanager.data.repository.util.toEnvironmentUrlsOrDefault
import com.universalpasswordmanager.network.interceptor.BaseUrlsProvider
import com.universalpasswordmanager.data.platform.datasource.disk.EnvironmentDiskSource

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
