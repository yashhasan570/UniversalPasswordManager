package com.universalpasswordmanager.data.platform.datasource.network.di

import com.universalpasswordmanager.core.data.manager.BuildInfoManager
import com.universalpasswordmanager.network.UniversalServiceClient
import com.universalpasswordmanager.network.universalpasswordmanagerServiceClient
import com.universalpasswordmanager.network.interceptor.BaseUrlsProvider
import com.universalpasswordmanager.network.model.UniversalServiceClientConfig
import com.universalpasswordmanager.network.service.ConfigService
import com.universalpasswordmanager.network.service.EventService
import com.universalpasswordmanager.network.service.FillAssistService
import com.universalpasswordmanager.network.service.PushService
import com.universalpasswordmanager.data.auth.datasource.disk.AuthDiskSource
import com.universalpasswordmanager.data.auth.manager.AuthTokenManager
import com.universalpasswordmanager.data.platform.datasource.network.util.HEADER_VALUE_CLIENT_NAME
import com.universalpasswordmanager.data.platform.datasource.network.util.HEADER_VALUE_CLIENT_VERSION
import com.universalpasswordmanager.data.platform.datasource.network.util.HEADER_VALUE_USER_AGENT
import com.universalpasswordmanager.data.platform.manager.CertificateManager
import com.universalpasswordmanager.data.platform.manager.network.NetworkCookieManager
import com.universalpasswordmanager.data.platform.manager.network.NetworkPermissionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import java.time.Clock
import javax.inject.Singleton

/**
 * This class provides network-related functionality for the application.
 * It initializes and configures the networking components.
 */
@Module
@InstallIn(SingletonComponent::class)
object PlatformNetworkModule {

    @Provides
    @Singleton
    fun providesFillAssistService(
        universalpasswordmanagerServiceClient: UniversalServiceClient,
    ): FillAssistService = universalpasswordmanagerServiceClient.fillAssistService

    @Provides
    @Singleton
    fun providesConfigService(
        universalpasswordmanagerServiceClient: UniversalServiceClient,
    ): ConfigService = universalpasswordmanagerServiceClient.configService

    @Provides
    @Singleton
    fun providesEventService(
        universalpasswordmanagerServiceClient: UniversalServiceClient,
    ): EventService = universalpasswordmanagerServiceClient.eventService

    @Provides
    @Singleton
    fun providePushService(
        universalpasswordmanagerServiceClient: UniversalServiceClient,
    ): PushService = universalpasswordmanagerServiceClient.pushService

    @Provides
    @Singleton
    fun provideUniversalServiceClientConfig(
        authTokenManager: AuthTokenManager,
        baseUrlsProvider: BaseUrlsProvider,
        authDiskSource: AuthDiskSource,
        certificateManager: CertificateManager,
        buildInfoManager: BuildInfoManager,
        networkCookieManager: NetworkCookieManager,
        networkPermissionManager: NetworkPermissionManager,
        clock: Clock,
    ): UniversalServiceClientConfig = UniversalServiceClientConfig(
        clock = clock,
        appIdProvider = authDiskSource,
        clientData = UniversalServiceClientConfig.ClientData(
            userAgent = HEADER_VALUE_USER_AGENT,
            clientName = HEADER_VALUE_CLIENT_NAME,
            clientVersion = HEADER_VALUE_CLIENT_VERSION,
        ),
        authTokenProvider = authTokenManager,
        baseUrlsProvider = baseUrlsProvider,
        certificateProvider = certificateManager,
        enableHttpBodyLogging = buildInfoManager.isDevBuild,
        cookieProvider = networkCookieManager,
        permissionProvider = networkPermissionManager,
    )

    @Provides
    @Singleton
    fun provideUniversalServiceClient(
        serviceClientConfig: UniversalServiceClientConfig,
        json: Json,
    ): UniversalServiceClient = universalpasswordmanagerServiceClient(
        config = serviceClientConfig,
        json = json,
    )
}
