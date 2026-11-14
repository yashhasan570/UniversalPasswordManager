package com.universalpasswordmanager.data.auth.manager

import com.universalpasswordmanager.core.UpdateKdfResponse
import com.universalpasswordmanager.core.data.util.flatMap
import com.universalpasswordmanager.crypto.Kdf
import com.universalpasswordmanager.network.model.KdfTypeJson
import com.universalpasswordmanager.network.model.MasterPasswordAuthenticationDataJson
import com.universalpasswordmanager.network.model.MasterPasswordUnlockDataJson
import com.universalpasswordmanager.network.model.UpdateKdfJsonRequest
import com.universalpasswordmanager.network.service.AccountsService
import com.universalpasswordmanager.data.auth.datasource.disk.AuthDiskSource
import com.universalpasswordmanager.data.auth.datasource.sdk.util.toKdfRequestModel
import com.universalpasswordmanager.data.auth.repository.model.UpdateKdfMinimumsResult
import com.universalpasswordmanager.data.auth.repository.util.toUserStateJsonKdfUpdatedMinimums
import com.universalpasswordmanager.data.auth.util.KdfParamsConstants.DEFAULT_PBKDF2_ITERATIONS
import com.universalpasswordmanager.data.vault.datasource.sdk.VaultSdkSource
import timber.log.Timber

/**
 * Default implementation of [KdfManager].
 */
class KdfManagerImpl(
    private val authDiskSource: AuthDiskSource,
    private val vaultSdkSource: VaultSdkSource,
    private val accountsService: AccountsService,
) : KdfManager {

    private val activeUserId: String? get() = authDiskSource.userState?.activeUserId

    override fun needsKdfUpdateToMinimums(userId: String): Boolean {
        val account = authDiskSource
            .userState
            ?.accounts
            ?.get(userId)
            ?: return false

        if (account.profile.userDecryptionOptions != null &&
            !account.profile.userDecryptionOptions.hasMasterPassword
        ) {
            return false
        }

        return account.profile.kdfType == KdfTypeJson.PBKDF2_SHA256 &&
            account.profile.kdfIterations != null &&
            account.profile.kdfIterations < DEFAULT_PBKDF2_ITERATIONS
    }

    override suspend fun updateKdfToMinimumsIfNeeded(
        userId: String,
        password: String,
    ): UpdateKdfMinimumsResult {
        // The update request is authenticated as the active user, so only update the KDF when
        // the given user is the active user. This prevents updating the KDF of a user that is not
        // active yet, such as when logging in to an additional account.
        if (userId != activeUserId) return UpdateKdfMinimumsResult.ActiveAccountNotFound

        if (!needsKdfUpdateToMinimums(userId = userId)) {
            return UpdateKdfMinimumsResult.Success
        }
        return vaultSdkSource
            .makeUpdateKdf(
                userId = userId,
                password = password,
                kdf = Kdf.Pbkdf2(iterations = DEFAULT_PBKDF2_ITERATIONS.toUInt()),
            )
            .flatMap {
                accountsService.updateKdf(createUpdateKdfRequest(it))
            }
            .fold(
                onSuccess = {
                    authDiskSource.userState = authDiskSource.userState
                        ?.toUserStateJsonKdfUpdatedMinimums(userId = userId)
                    Timber.d("[Auth] Upgraded user's KDF to minimums")
                    UpdateKdfMinimumsResult.Success
                },
                onFailure = { UpdateKdfMinimumsResult.Error(error = it) },
            )
    }

    private fun createUpdateKdfRequest(response: UpdateKdfResponse): UpdateKdfJsonRequest {
        val authData = response.masterPasswordAuthenticationData
        val oldAuthData = response.oldMasterPasswordAuthenticationData
        val unlockData = response.masterPasswordUnlockData

        return UpdateKdfJsonRequest(
            authenticationData = MasterPasswordAuthenticationDataJson(
                kdf = authData.kdf.toKdfRequestModel(),
                masterPasswordAuthenticationHash = authData.masterPasswordAuthenticationHash,
                salt = authData.salt,
            ),
            key = unlockData.masterKeyWrappedUserKey,
            masterPasswordHash = oldAuthData.masterPasswordAuthenticationHash,
            newMasterPasswordHash = authData.masterPasswordAuthenticationHash,
            unlockData = MasterPasswordUnlockDataJson(
                kdf = unlockData.kdf.toKdfRequestModel(),
                masterKeyWrappedUserKey = unlockData.masterKeyWrappedUserKey,
                salt = unlockData.salt,
                containedKeyId = unlockData.containedKeyId,
            ),
        )
    }
}
