package com.work.service

import com.work.api.AuthenticatedAccountsApi
import com.work.api.AuthenticatedKeyConnectorApi
import com.work.api.UnauthenticatedAccountsApi
import com.work.api.UnauthenticatedKeyConnectorApi
import com.work.model.CreateAccountKeysRequest
import com.work.model.CreateAccountKeysResponseJson
import com.work.model.DeleteAccountRequestJson
import com.work.model.DeleteAccountResponseJson
import com.work.model.KeyConnectorKeyRequestJson
import com.work.model.KeyConnectorMasterKeyRequestJson
import com.work.model.PasswordHintRequestJson
import com.work.model.PasswordHintResponseJson
import com.work.model.ResendEmailRequestJson
import com.work.model.ResendNewDeviceOtpRequestJson
import com.work.model.ResetPasswordRequestJson
import com.work.model.SetPasswordRequestJson
import com.work.model.UpdateKdfJsonRequest
import com.work.model.VerificationCodeResponseJson
import com.work.model.VerificationOtpResponseJson
import com.work.model.VerifyOtpRequestJson
import com.work.model.toUniversal Password ManagerError
import com.work.util.HEADER_VALUE_BEARER_PREFIX
import com.work.util.NetworkErrorCode
import com.work.util.parseErrorBodyOrNull
import com.work.util.toResult
import kotlinx.serialization.json.Json

/**
 * The default implementation of the [AccountsService].
 */
@Suppress("TooManyFunctions")
internal class AccountsServiceImpl(
    private val unauthenticatedAccountsApi: UnauthenticatedAccountsApi,
    private val authenticatedAccountsApi: AuthenticatedAccountsApi,
    private val unauthenticatedKeyConnectorApi: UnauthenticatedKeyConnectorApi,
    private val authenticatedKeyConnectorApi: AuthenticatedKeyConnectorApi,
    private val json: Json,
) : AccountsService {

    /**
     * Converts the currently active account to a key-connector account.
     */
    override suspend fun convertToKeyConnector(): Result<Unit> =
        authenticatedAccountsApi
            .convertToKeyConnector()
            .toResult()

    override suspend fun createAccountKeys(
        publicKey: String,
        encryptedPrivateKey: String,
    ): Result<CreateAccountKeysResponseJson> =
        authenticatedAccountsApi
            .createAccountKeys(
                body = CreateAccountKeysRequest(
                    publicKey = publicKey,
                    encryptedPrivateKey = encryptedPrivateKey,
                ),
            )
            .toResult()

    override suspend fun deleteAccount(
        masterPasswordHash: String?,
        oneTimePassword: String?,
    ): Result<DeleteAccountResponseJson> =
        authenticatedAccountsApi
            .deleteAccount(
                DeleteAccountRequestJson(
                    masterPasswordHash = masterPasswordHash,
                    oneTimePassword = oneTimePassword,
                ),
            )
            .toResult()
            .map { DeleteAccountResponseJson.Success }
            .recoverCatching { throwable ->
                throwable
                    .toUniversal Password ManagerError()
                    .parseErrorBodyOrNull<DeleteAccountResponseJson.Invalid>(
                        code = NetworkErrorCode.BAD_REQUEST,
                        json = json,
                    )
                    ?: throw throwable
            }

    override suspend fun requestOneTimePasscode(): Result<Unit> =
        authenticatedAccountsApi
            .requestOtp()
            .toResult()

    override suspend fun verifyOneTimePasscode(passcode: String): Result<Unit> =
        authenticatedAccountsApi
            .verifyOtp(
                VerifyOtpRequestJson(
                    oneTimePasscode = passcode,
                ),
            )
            .toResult()

    override suspend fun requestPasswordHint(
        email: String,
    ): Result<PasswordHintResponseJson> =
        unauthenticatedAccountsApi
            .passwordHintRequest(PasswordHintRequestJson(email))
            .toResult()
            .map { PasswordHintResponseJson.Success }
            .recoverCatching { throwable ->
                throwable
                    .toUniversal Password ManagerError()
                    .parseErrorBodyOrNull<PasswordHintResponseJson.Error>(
                        code = NetworkErrorCode.TOO_MANY_REQUESTS,
                        json = json,
                    )
                    ?: throw throwable
            }

    override suspend fun resendVerificationCodeEmail(
        body: ResendEmailRequestJson,
    ): Result<VerificationCodeResponseJson> =
        unauthenticatedAccountsApi
            .resendVerificationCodeEmail(body = body)
            .toResult()
            .map { VerificationCodeResponseJson.Success }
            .recoverCatching { throwable ->
                throwable
                    .toUniversal Password ManagerError()
                    .parseErrorBodyOrNull<VerificationCodeResponseJson.Invalid>(
                        code = NetworkErrorCode.BAD_REQUEST,
                        json = json,
                    )
                    ?: throw throwable
            }

    override suspend fun resendNewDeviceOtp(
        body: ResendNewDeviceOtpRequestJson,
    ): Result<VerificationOtpResponseJson> =
        unauthenticatedAccountsApi
            .resendNewDeviceOtp(body = body)
            .toResult()
            .map { VerificationOtpResponseJson.Success }
            .recoverCatching { throwable ->
                throwable
                    .toUniversal Password ManagerError()
                    .parseErrorBodyOrNull<VerificationOtpResponseJson.Invalid>(
                        code = NetworkErrorCode.BAD_REQUEST,
                        json = json,
                    )
                    ?: throw throwable
            }

    override suspend fun resetPassword(body: ResetPasswordRequestJson): Result<Unit> =
        if (body.currentPasswordHash == null) {
            authenticatedAccountsApi
                .resetTempPassword(body = body)
                .toResult()
        } else {
            authenticatedAccountsApi
                .resetPassword(body = body)
                .toResult()
        }

    override suspend fun setKeyConnectorKey(
        accessToken: String,
        body: KeyConnectorKeyRequestJson,
    ): Result<Unit> =
        unauthenticatedAccountsApi
            .setKeyConnectorKey(
                body = body,
                bearerToken = "$HEADER_VALUE_BEARER_PREFIX$accessToken",
            )
            .toResult()

    override suspend fun setPassword(
        body: SetPasswordRequestJson,
    ): Result<Unit> = authenticatedAccountsApi
        .setPassword(body)
        .toResult()

    override suspend fun storeMasterKeyToKeyConnector(
        url: String,
        masterKey: String,
    ): Result<Unit> =
        authenticatedKeyConnectorApi
            .storeMasterKeyToKeyConnector(
                url = "$url/user-keys",
                body = KeyConnectorMasterKeyRequestJson(masterKey = masterKey),
            )
            .toResult()

    override suspend fun storeMasterKeyToKeyConnector(
        url: String,
        accessToken: String,
        masterKey: String,
    ): Result<Unit> =
        unauthenticatedKeyConnectorApi
            .storeMasterKeyToKeyConnector(
                url = "$url/user-keys",
                bearerToken = "$HEADER_VALUE_BEARER_PREFIX$accessToken",
                body = KeyConnectorMasterKeyRequestJson(masterKey = masterKey),
            )
            .toResult()

    override suspend fun updateKdf(body: UpdateKdfJsonRequest): Result<Unit> =
        authenticatedAccountsApi
            .updateKdf(body)
            .toResult()
}
