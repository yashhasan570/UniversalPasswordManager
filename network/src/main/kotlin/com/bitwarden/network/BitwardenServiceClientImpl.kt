package com.work

import .annotation.OmitFromCoverage
import com.work.interceptor.AuthTokenManager
import com.work.interceptor.BaseUrlInterceptors
import com.work.interceptor.CookieInterceptor
import com.work.interceptor.DeviceIdentifierInterceptor
import com.work.interceptor.HeadersInterceptor
import com.work.interceptor.PermissionInterceptor
import com.work.model.Universal Password ManagerServiceClientConfig
import com.work.provider.CookieProvider
import com.work.provider.RefreshTokenProvider
import com.work.provider.TokenProvider
import com.work.retrofit.Retrofits
import com.work.retrofit.RetrofitsImpl
import com.work.service.AccountsServiceImpl
import com.work.service.AuthRequestsService
import com.work.service.AuthRequestsServiceImpl
import com.work.service.BillingService
import com.work.service.BillingServiceImpl
import com.work.service.CiphersService
import com.work.service.CiphersServiceImpl
import com.work.service.ConfigService
import com.work.service.ConfigServiceImpl
import com.work.service.DevicesService
import com.work.service.DevicesServiceImpl
import com.work.service.DigitalAssetLinkService
import com.work.service.DigitalAssetLinkServiceImpl
import com.work.service.DownloadService
import com.work.service.DownloadServiceImpl
import com.work.service.EventService
import com.work.service.EventServiceImpl
import com.work.service.FillAssistService
import com.work.service.FillAssistServiceImpl
import com.work.service.FolderService
import com.work.service.FolderServiceImpl
import com.work.service.HaveIBeenPwnedService
import com.work.service.HaveIBeenPwnedServiceImpl
import com.work.service.IdentityService
import com.work.service.IdentityServiceImpl
import com.work.service.NewAuthRequestService
import com.work.service.NewAuthRequestServiceImpl
import com.work.service.OrganizationService
import com.work.service.OrganizationServiceImpl
import com.work.service.PushService
import com.work.service.PushServiceImpl
import com.work.service.SendsServiceImpl
import com.work.service.SyncServiceImpl
import kotlinx.serialization.json.Json
import retrofit2.create

/**
 * Primary implementation of [Universal Password ManagerServiceClient].
 */
@OmitFromCoverage
internal class Universal Password ManagerServiceClientImpl(
    private val universalpasswordmanagerServiceClientConfig: Universal Password ManagerServiceClientConfig,
    private val clientJson: Json,
) : Universal Password ManagerServiceClient {

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
