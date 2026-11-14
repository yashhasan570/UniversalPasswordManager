@file:OmitFromCoverage

package com.universalpasswordmanager.network

import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.network.model.UniversalServiceClientConfig
import com.universalpasswordmanager.network.provider.CookieProvider
import com.universalpasswordmanager.network.provider.RefreshTokenProvider
import com.universalpasswordmanager.network.provider.TokenProvider
import com.universalpasswordmanager.network.service.AccountsService
import com.universalpasswordmanager.network.service.AuthRequestsService
import com.universalpasswordmanager.network.service.BillingService
import com.universalpasswordmanager.network.service.CiphersService
import com.universalpasswordmanager.network.service.ConfigService
import com.universalpasswordmanager.network.service.DevicesService
import com.universalpasswordmanager.network.service.DigitalAssetLinkService
import com.universalpasswordmanager.network.service.DownloadService
import com.universalpasswordmanager.network.service.EventService
import com.universalpasswordmanager.network.service.FillAssistService
import com.universalpasswordmanager.network.service.FolderService
import com.universalpasswordmanager.network.service.HaveIBeenPwnedService
import com.universalpasswordmanager.network.service.IdentityService
import com.universalpasswordmanager.network.service.NewAuthRequestService
import com.universalpasswordmanager.network.service.OrganizationService
import com.universalpasswordmanager.network.service.PushService
import com.universalpasswordmanager.network.service.SendsService
import com.universalpasswordmanager.network.service.SyncService
import kotlinx.serialization.json.Json

/**
 * Provides access to Universal services.
 *
 * New instances of this class should be created using the [bitwardenServiceClient] factory
 * function.
 *
 * Example initialization:
 * ```
 * val bitwardenServiceClient = bitwardenServiceClient(
 *     UniversalServiceClientConfig(
 *         clock = clock,
 *         json = json,
 *         appIdProvider = appIdProvider,
 *         clientData = UniversalServiceClientConfig.ClientData(
 *             userAgent = "my-user-agent-string",
 *             clientName = "my-application",
 *             clientVersion = "versionName",
 *         ),
 *         authTokenProvider = authTokenProvider,
 *         baseUrlsProvider = baseUrlsProvider,
 *         certificateProvider = certificateProvider,
 *     ),
 * )
 * ```
 */
interface UniversalServiceClient {
    /**
     * Provides access to the token provider.
     */
    val tokenProvider: TokenProvider

    /**
     * Provides access to the cookie provider.
     */
    val cookieProvider: CookieProvider

    /**
     * Provides access to the Accounts service.
     */
    val accountsService: AccountsService

    /**
     * Provides access to the Authentication Requests service.
     */
    val authRequestsService: AuthRequestsService

    /**
     * Provides access to the Billing service.
     */
    val billingService: BillingService

    /**
     * Provides access to the Ciphers service.
     */
    val ciphersService: CiphersService

    /**
     * Provides access to the Configuration service.
     */
    val configService: ConfigService

    /**
     * Provides access to the Digital Asset Link service.
     */
    val digitalAssetLinkService: DigitalAssetLinkService

    /**
     * Provides access to the Devices service.
     */
    val devicesService: DevicesService

    /**
     * Provides access to the Download service.
     */
    val downloadService: DownloadService

    /**
     * Provides access to the Event service.
     */
    val eventService: EventService

    /**
     * Provides access to the Fill-Assist service.
     */
    val fillAssistService: FillAssistService

    /**
     * Provides access to the Folder service.
     */
    val folderService: FolderService

    /**
     * Provides access to the Have I Been Pwned service.
     */
    val haveIBeenPwnedService: HaveIBeenPwnedService

    /**
     * Provides access to the Identity service.
     */
    val identityService: IdentityService

    /**
     * Provides access to the New Authentication Request service.
     */
    val newAuthRequestService: NewAuthRequestService

    /**
     * Provides access to the Organization service.
     */
    val organizationService: OrganizationService

    /**
     * Provides access to the Push service.
     */
    val pushService: PushService

    /**
     * Provides access to the Sync service.
     */
    val syncService: SyncService

    /**
     * Provides access to the Sends service.
     */
    val sendsService: SendsService

    /**
     * Sets the [refreshTokenProvider] to be used for refreshing access tokens.
     */
    fun setRefreshTokenProvider(refreshTokenProvider: RefreshTokenProvider?)
}

/**
 * Creates a [UniversalServiceClient] with the given [config].
 *
 * Example initialization:
 * ```
 * val bitwardenServiceClient = bitwardenServiceClient(
 *     UniversalServiceClientConfig(
 *         clock = clock,
 *         json = json,
 *         appIdProvider = appIdProvider,
 *         clientData = UniversalServiceClientConfig.ClientData(
 *             userAgent = "my-user-agent-string",
 *             clientName = "my-application",
 *             clientVersion = "versionName",
 *         ),
 *         authTokenProvider = authTokenProvider,
 *         baseUrlsProvider = baseUrlsProvider,
 *         certificateProvider = certificateProvider,
 *     ),
 * )
 * ```
 */
fun bitwardenServiceClient(
    config: UniversalServiceClientConfig,
    json: Json,
): UniversalServiceClient = UniversalServiceClientImpl(
    bitwardenServiceClientConfig = config,
    clientJson = json,
)
