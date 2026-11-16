package com.work.model

import com.work.Universal Password ManagerServiceClient
import com.work.interceptor.AuthTokenProvider
import com.work.interceptor.BaseUrlsProvider
import com.work.provider.AppIdProvider
import com.work.provider.CookieProvider
import com.work.provider.PermissionProvider
import com.work.ssl.CertificateProvider
import java.time.Clock

/**
 * Models configuration for [Universal Password ManagerServiceClient].
 */
data class Universal Password ManagerServiceClientConfig(
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
