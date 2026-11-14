package com.universalpasswordmanager.network

import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.network.interceptor.AuthTokenManager
import com.universalpasswordmanager.network.interceptor.BaseUrlInterceptors
import com.universalpasswordmanager.network.interceptor.CookieInterceptor
import com.universalpasswordmanager.network.interceptor.DeviceIdentifierInterceptor
import com.universalpasswordmanager.network.interceptor.HeadersInterceptor
import com.universalpasswordmanager.network.interceptor.PermissionInterceptor
import com.universalpasswordmanager.network.model.UniversalServiceClientConfig
import com.universalpasswordmanager.network.provider.CookieProvider
import com.universalpasswordmanager.network.provider.RefreshTokenProvider
import com.universalpasswordmanager.network.provider.TokenProvider
import com.universalpasswordmanager.network.retrofit.Retrofits
import com.universalpasswordmanager.network.retrofit.RetrofitsImpl
import com.universalpasswordmanager.network.service.AccountsServiceImpl
import com.universalpasswordmanager.network.service.AuthRequestsService
import com.universalpasswordmanager.network.service.AuthRequestsServiceImpl
import com.universalpasswordmanager.network.service.BillingService
import com.universalpasswordmanager.network.service.BillingServiceImpl
import com.universalpasswordmanager.network.service.CiphersService
import com.universalpasswordmanager.network.service.CiphersServiceImpl
import com.universalpasswordmanager.network.service.ConfigService
import com.universalpasswordmanager.network.service.ConfigServiceImpl
import com.universalpasswordmanager.network.service.DevicesService
import com.universalpasswordmanager.network.service.DevicesServiceImpl
import com.universalpasswordmanager.network.service.DigitalAssetLinkService
import com.universalpasswordmanager.network.service.DigitalAssetLinkServiceImpl
import com.universalpasswordmanager.network.service.DownloadService
import com.universalpasswordmanager.network.service.DownloadServiceImpl
import com.universalpasswordmanager.network.service.EventService
import com.universalpasswordmanager.network.service.EventServiceImpl
import com.universalpasswordmanager.network.service.FillAssistService
import com.universalpasswordmanager.network.service.FillAssistServiceImpl
import com.universalpasswordmanager.network.service.FolderService
import com.universalpasswordmanager.network.service.FolderServiceImpl
import com.universalpasswordmanager.network.service.HaveIBeenPwnedService
import com.universalpasswordmanager.network.service.HaveIBeenPwnedServiceImpl
import com.universalpasswordmanager.network.service.IdentityService
import com.universalpasswordmanager.network.service.IdentityServiceImpl
import com.universalpasswordmanager.network.service.NewAuthRequestService
import com.universalpasswordmanager.network.service.NewAuthRequestServiceImpl
import com.universalpasswordmanager.network.service.OrganizationService
import com.universalpasswordmanager.network.service.OrganizationServiceImpl
import com.universalpasswordmanager.network.service.PushService
import com.universalpasswordmanager.network.service.PushServiceImpl
import com.universalpasswordmanager.network.service.SendsServiceImpl
import com.universalpasswordmanager.network.service.SyncServiceImpl
import kotlinx.serialization.json.Json
import retrofit2.create

/**
 * Primary implementation of [UniversalServiceClient].
 */
@OmitFromCoverage
internal class UniversalServiceClientImpl(
    private val universalpasswordmanagerServiceClientConfig: UniversalServiceClientConfig,
    private val clientJson: Json,
) : UniversalServiceClient {

    private val authTokenManager: AuthTokenManager = AuthTokenManager(
        clock = universalpasswordmanagerServiceClientConfig.clock,
        authTokenProvider = universalpasswordmanagerServiceClientConfig.authTokenProvider,
    )
    override val tokenProvider: TokenProvider = authTokenManager

    override val cookieProvider: CookieProvider = universalpasswordmanagerServiceClientConfig.cookieProvider

    private val retrofits: Retrofits by lazy {
        RetrofitsImpl(
            authTokenManager = authTokenManager,
            baseUrlInterceptors = BaseUrlInterceptors(
                baseUrlsProvider = universalpasswordmanagerServiceClientConfig.baseUrlsProvider,
            ),
            cookieInterceptor = CookieInterceptor(
                cookieProvider = cookieProvider,
            ),
            deviceIdentifierInterceptor = DeviceIdentifierInterceptor(
                appIdProvider = universalpasswordmanagerServiceClientConfig.appIdProvider,
            ),
            permissionInterceptor = PermissionInterceptor(
                permissionProvider = universalpasswordmanagerServiceClientConfig.permissionProvider,
            ),
            headersInterceptor = HeadersInterceptor(
                userAgent = universalpasswordmanagerServiceClientConfig.clientData.userAgent,
                clientName = universalpasswordmanagerServiceClientConfig.clientData.clientName,
                clientVersion = universalpasswordmanagerServiceClientConfig.clientData.clientVersion,
            ),
            logHttpBody = universalpasswordmanagerServiceClientConfig.enableHttpBodyLogging,
            certificateProvider = universalpasswordmanagerServiceClientConfig.certificateProvider,
            json = clientJson,
        )
    }

    override val accountsService by lazy {
        AccountsServiceImpl(
            unauthenticatedAccountsApi = retrofits.unauthenticatedApiRetrofit.create(),
            authenticatedAccountsApi = retrofits.authenticatedApiRetrofit.create(),
            unauthenticatedKeyConnectorApi = retrofits.createStaticRetrofit().create(),
            authenticatedKeyConnectorApi = retrofits
                .createStaticRetrofit(isAuthenticated = true)
                .create(),
            json = clientJson,
        )
    }

    override val authRequestsService: AuthRequestsService by lazy {
        AuthRequestsServiceImpl(
            authenticatedAuthRequestsApi = retrofits.authenticatedApiRetrofit.create(),
        )
    }

    override val billingService: BillingService by lazy {
        BillingServiceImpl(
            authenticatedBillingApi = retrofits.authenticatedApiRetrofit.create(),
        )
    }

    override val ciphersService: CiphersService by lazy {
        CiphersServiceImpl(
            azureApi = retrofits.createStaticRetrofit().create(),
            ciphersApi = retrofits.authenticatedApiRetrofit.create(),
            json = clientJson,
            clock = universalpasswordmanagerServiceClientConfig.clock,
        )
    }

    override val configService: ConfigService by lazy {
        ConfigServiceImpl(
            configApi = retrofits.authenticatedApiRetrofit.create(),
        )
    }

    override val devicesService: DevicesService by lazy {
        DevicesServiceImpl(
            authenticatedDevicesApi = retrofits.authenticatedApiRetrofit.create(),
            unauthenticatedDevicesApi = retrofits.unauthenticatedApiRetrofit.create(),
        )
    }

    override val digitalAssetLinkService: DigitalAssetLinkService by lazy {
        DigitalAssetLinkServiceImpl(
            digitalAssetLinkApi = retrofits
                .createStaticRetrofit(baseUrl = "https://digitalassetlinks.googleapis.com/")
                .create(),
        )
    }

    override val downloadService: DownloadService by lazy {
        DownloadServiceImpl(downloadApi = retrofits.createStaticRetrofit().create())
    }

    override val eventService: EventService by lazy {
        EventServiceImpl(eventApi = retrofits.authenticatedEventsRetrofit.create())
    }

    override val folderService: FolderService by lazy {
        FolderServiceImpl(
            foldersApi = retrofits.authenticatedApiRetrofit.create(),
            json = clientJson,
        )
    }

    override val fillAssistService: FillAssistService by lazy {
        FillAssistServiceImpl(api = retrofits.fillAssistRetrofit.create())
    }

    override val haveIBeenPwnedService: HaveIBeenPwnedService by lazy {
        HaveIBeenPwnedServiceImpl(
            api = retrofits
                .createStaticRetrofit(baseUrl = "https://api.pwnedpasswords.com")
                .create(),
        )
    }

    override val identityService: IdentityService by lazy {
        IdentityServiceImpl(
            unauthenticatedIdentityApi = retrofits.unauthenticatedIdentityRetrofit.create(),
            json = clientJson,
        )
    }

    override val newAuthRequestService: NewAuthRequestService by lazy {
        NewAuthRequestServiceImpl(
            authenticatedAuthRequestsApi = retrofits.authenticatedApiRetrofit.create(),
            unauthenticatedAuthRequestsApi = retrofits.unauthenticatedApiRetrofit.create(),
        )
    }

    override val organizationService: OrganizationService by lazy {
        OrganizationServiceImpl(
            authenticatedOrganizationApi = retrofits.authenticatedApiRetrofit.create(),
            unauthenticatedOrganizationApi = retrofits.unauthenticatedApiRetrofit.create(),
        )
    }

    override val pushService: PushService by lazy {
        PushServiceImpl(
            pushApi = retrofits.authenticatedApiRetrofit.create(),
            appId = universalpasswordmanagerServiceClientConfig.appIdProvider.uniqueAppId,
        )
    }

    override val sendsService by lazy {
        SendsServiceImpl(
            sendsApi = retrofits.authenticatedApiRetrofit.create(),
            azureApi = retrofits.createStaticRetrofit().create(),
            json = clientJson,
            clock = universalpasswordmanagerServiceClientConfig.clock,
        )
    }

    override val syncService by lazy {
        SyncServiceImpl(
            syncApi = retrofits.authenticatedApiRetrofit.create(),
        )
    }

    override fun setRefreshTokenProvider(refreshTokenProvider: RefreshTokenProvider?) {
        authTokenManager.refreshTokenProvider = refreshTokenProvider
    }
}
