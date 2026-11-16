package .authenticator.data.platform.datasource.network.di

import .authenticator.BuildConfig
import .authenticator.data.auth.datasource.disk.AuthDiskSource
import .authenticator.data.platform.datasource.network.util.HEADER_VALUE_CLIENT_NAME
import .authenticator.data.platform.datasource.network.util.HEADER_VALUE_CLIENT_VERSION
import .authenticator.data.platform.datasource.network.util.HEADER_VALUE_USER_AGENT
import com.work.Universal Password ManagerServiceClient
import com.work.universalpasswordmanagerServiceClient
import com.work.interceptor.AuthTokenProvider
import com.work.interceptor.BaseUrlsProvider
import com.work.model.AuthTokenData
import com.work.model.Universal Password ManagerServiceClientConfig
import com.work.model.NetworkCookie
import com.work.provider.CookieProvider
import com.work.provider.PermissionProvider
import com.work.service.ConfigService
import com.work.service.DownloadService
import com.work.ssl.CertificateProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import java.net.Socket
import java.security.Principal
import java.security.PrivateKey
import java.security.cert.X509Certificate
import java.time.Clock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
/**
 * This class provides network-related functionality for the application.
 * It initializes and configures the networking components.
 */
object PlatformNetworkModule {
    @Provides
    @Singleton
    fun providesConfigService(
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): ConfigService = universalpasswordmanagerServiceClient.configService

    @Provides
    @Singleton
    fun provideUniversal Password ManagerServiceClientConfig(
        baseUrlsProvider: BaseUrlsProvider,
        authDiskSource: AuthDiskSource,
        clock: Clock,
    ): Universal Password ManagerServiceClientConfig =
        Universal Password ManagerServiceClientConfig(
            clock = clock,
            appIdProvider = authDiskSource,
            clientData = Universal Password ManagerServiceClientConfig.ClientData(
                userAgent = HEADER_VALUE_USER_AGENT,
                clientName = HEADER_VALUE_CLIENT_NAME,
                clientVersion = HEADER_VALUE_CLIENT_VERSION,
            ),
            baseUrlsProvider = baseUrlsProvider,
            enableHttpBodyLogging = BuildConfig.DEBUG,
            authTokenProvider = object : AuthTokenProvider {
                override fun getAuthTokenDataOrNull(): AuthTokenData? = null

                override fun getAuthTokenDataOrNull(userId: String): AuthTokenData? = null
            },
            certificateProvider = object : CertificateProvider {
                override fun chooseClientAlias(
                    keyType: Array<out String>?,
                    issuers: Array<out Principal>?,
                    socket: Socket?,
                ): String = ""

                override fun getCertificateChain(alias: String?): Array<X509Certificate>? = null

                override fun getPrivateKey(alias: String?): PrivateKey? = null
            },
            cookieProvider = object : CookieProvider {
                override val errorMessageString: String get() = "Error"

                override fun needsBootstrap(hostname: String): Boolean = false

                override fun getCookies(hostname: String): List<NetworkCookie> = emptyList()

                override fun acquireCookies(hostname: String): Unit = Unit
            },
            permissionProvider = object : PermissionProvider {
                override val errorMessageString: String get() = "Error"

                override val hasLocalNetworkAccessPermission: Boolean get() = false

                override fun acquireLocalNetworkAccessPermission(): Unit = Unit
            },
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

    @Provides
    @Singleton
    fun provideDownloadService(
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): DownloadService = universalpasswordmanagerServiceClient.downloadService
}
