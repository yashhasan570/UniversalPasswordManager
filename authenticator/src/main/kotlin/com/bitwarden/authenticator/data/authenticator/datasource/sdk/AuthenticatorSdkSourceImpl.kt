package .authenticator.data.authenticator.datasource.sdk

import .authenticator.data.platform.manager.SdkClientManager
import .generators.PasswordGeneratorRequest
import .sdk.Client
import .vault.TotpResponse
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [AuthenticatorSdkSource].
 */
class AuthenticatorSdkSourceImpl @Inject constructor(
    private val sdkClientManager: SdkClientManager,
) : AuthenticatorSdkSource {

    override suspend fun generateTotp(
        totp: String,
        time: Instant,
    ): Result<TotpResponse> = runCatching {
        getClient()
            .vault()
            .generateTotp(
                key = totp,
                time = time,
            )
    }

    override suspend fun generateBiometricsKey(): Result<String> =
        runCatching {
            getClient()
                .generators()
                .password(
                    PasswordGeneratorRequest(
                        lowercase = true,
                        uppercase = true,
                        numbers = true,
                        special = true,
                        length = 7.toUByte(),
                        avoidAmbiguous = true,
                        minLowercase = null,
                        minUppercase = null,
                        minNumber = null,
                        minSpecial = null,
                    ),
                )
        }

    private suspend fun getClient(): Client = sdkClientManager.getOrCreateClient()
}
