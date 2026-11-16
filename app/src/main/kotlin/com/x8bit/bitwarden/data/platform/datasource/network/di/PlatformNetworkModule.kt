package com.x8bit.universalpasswordmanager.data.platform.datasource.network.di

import .core.data.manager.BuildInfoManager
import com.work.Universal Password ManagerServiceClient
import com.work.universalpasswordmanagerServiceClient
import com.work.interceptor.BaseUrlsProvider
import com.work.model.Universal Password ManagerServiceClientConfig
import com.work.service.ConfigService
import com.work.service.EventService
import com.work.service.FillAssistService
import com.work.service.PushService
import com.x8bit.universalpasswordmanager.data.auth.datasource.disk.AuthDiskSource
import com.x8bit.universalpasswordmanager.data.auth.manager.AuthTokenManager
import com.x8bit.universalpasswordmanager.data.platform.datasource.network.util.HEADER_VALUE_CLIENT_NAME
import com.x8bit.universalpasswordmanager.data.platform.datasource.network.util.HEADER_VALUE_CLIENT_VERSION
import com.x8bit.universalpasswordmanager.data.platform.datasource.network.util.HEADER_VALUE_USER_AGENT
import com.x8bit.universalpasswordmanager.data.platform.manager.CertificateManager
import com.x8bit.universalpasswordmanager.data.platform.manager.network.NetworkCookieManager
import com.x8bit.universalpasswordmanager.data.platform.manager.network.NetworkPermissionManager
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
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): FillAssistService = universalpasswordmanagerServiceClient.fillAssistService

    @Provides
    @Singleton
    fun providesConfigService(
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): ConfigService = universalpasswordmanagerServiceClient.configService

    @Provides
    @Singleton
    fun providesEventService(
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): EventService = universalpasswordmanagerServiceClient.eventService

    @Provides
    @Singleton
    fun providePushService(
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): PushService = universalpasswordmanagerServiceClient.pushService

    @Provides
    @Singleton
    fun provideUniversal Password ManagerServiceClientConfig(
        authTokenManager: AuthTokenManager,
        baseUrlsProvider: BaseUrlsProvider,
        authDiskSource: AuthDiskSource,
        certificateManager: CertificateManager,
        buildInfoManager: BuildInfoManager,
        networkCookieManager: NetworkCookieManager,
        networkPermissionManager: NetworkPermissionManager,
        clock: Clock,
    ): Universal Password ManagerServiceClientConfig = Universal Password ManagerServiceClientConfig(
        clock = clock,
        appIdProvider = authDiskSource,
        clientData = Universal Password ManagerServiceClientConfig.ClientData(
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
    fun provideUniversal Password ManagerServiceClient(
        serviceClientConfig: Universal Password ManagerServiceClientConfig,
        json: Json,
    ): Universal Password ManagerServiceClient = universalpasswordmanagerServiceClient(
        config = serviceClientConfig,
        json = json,
    )
}
