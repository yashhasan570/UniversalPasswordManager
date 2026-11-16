package .authenticator.data.authenticator.datasource.sdk

import .vault.TotpResponse
import java.time.Instant

/**
 * Source of authenticator information from the Universal Password Manager SDK.
 */
interface AuthenticatorSdkSource {

    /**
     * Generate a verification code and the period using the totp code.
     */
    suspend fun generateTotp(
        totp: String,
        time: Instant,
    ): Result<TotpResponse>

    /**
     * Generate a random key for seeding biometrics.
     */
    suspend fun generateBiometricsKey(): Result<String>
}
