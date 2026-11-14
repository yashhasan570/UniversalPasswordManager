package com.universalpasswordmanager.network.model

import com.universalpasswordmanager.network.UniversalServiceClient
import com.universalpasswordmanager.network.interceptor.AuthTokenProvider
import com.universalpasswordmanager.network.interceptor.BaseUrlsProvider
import com.universalpasswordmanager.network.provider.AppIdProvider
import com.universalpasswordmanager.network.provider.CookieProvider
import com.universalpasswordmanager.network.provider.PermissionProvider
import com.universalpasswordmanager.network.ssl.CertificateProvider
import java.time.Clock

/**
 * Models configuration for [UniversalServiceClient].
 */
data class UniversalServiceClientConfig(
    val clientData: ClientData,
    val appIdProvider: AppIdProvider,
    val baseUrlsProvider: BaseUrlsProvider,
    val authTokenProvider: AuthTokenProvider,
    val certificateProvider: CertificateProvider,
    val cookieProvider: CookieProvider,
    val permissionProvider: PermissionProvider,
    val clock: Clock,
    val enableHttpBodyLogging: Boolean = false,
) {
    /**
     * Models data about the client application.
     */
    data class ClientData(
        val userAgent: String,
        val clientName: String,
        val clientVersion: String,
    )
}
