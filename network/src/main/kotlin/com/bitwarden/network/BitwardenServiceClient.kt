@file:OmitFromCoverage

package com.work

import .annotation.OmitFromCoverage
import com.work.model.Universal Password ManagerServiceClientConfig
import com.work.provider.CookieProvider
import com.work.provider.RefreshTokenProvider
import com.work.provider.TokenProvider
import com.work.service.AccountsService
import com.work.service.AuthRequestsService
import com.work.service.BillingService
import com.work.service.CiphersService
import com.work.service.ConfigService
import com.work.service.DevicesService
import com.work.service.DigitalAssetLinkService
import com.work.service.DownloadService
import com.work.service.EventService
import com.work.service.FillAssistService
import com.work.service.FolderService
import com.work.service.HaveIBeenPwnedService
import com.work.service.IdentityService
import com.work.service.NewAuthRequestService
import com.work.service.OrganizationService
import com.work.service.PushService
import com.work.service.SendsService
import com.work.service.SyncService
import kotlinx.serialization.json.Json

/**
 * Provides access to Universal Password Manager services.
 *
 * New instances of this class should be created using the [universalpasswordmanagerServiceClient] factory
 * function.
 *
 * Example initialization:
 * ```
 * val universalpasswordmanagerServiceClient = universalpasswordmanagerServiceClient(
 *     Universal Password ManagerServiceClientConfig(
 *         clock = clock,
 *         json = json,
 *         appIdProvider = appIdProvider,
 *         clientData = Universal Password ManagerServiceClientConfig.ClientData(
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
interface Universal Password ManagerServiceClient {
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
 * Creates a [Universal Password ManagerServiceClient] with the given [config].
 *
 * Example initialization:
 * ```
 * val universalpasswordmanagerServiceClient = universalpasswordmanagerServiceClient(
 *     Universal Password ManagerServiceClientConfig(
 *         clock = clock,
 *         json = json,
 *         appIdProvider = appIdProvider,
 *         clientData = Universal Password ManagerServiceClientConfig.ClientData(
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
fun universalpasswordmanagerServiceClient(
    config: Universal Password ManagerServiceClientConfig,
    json: Json,
): Universal Password ManagerServiceClient = Universal Password ManagerServiceClientImpl(
    universalpasswordmanagerServiceClientConfig = config,
    clientJson = json,
)
