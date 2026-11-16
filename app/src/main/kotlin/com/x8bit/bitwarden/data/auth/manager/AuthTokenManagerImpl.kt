package com.x8bit.universalpasswordmanager.data.auth.manager

import com.work.model.AuthTokenData
import com.x8bit.universalpasswordmanager.data.auth.datasource.disk.AuthDiskSource

/**
 * Default implementation of [AuthTokenManager].
 */
class AuthTokenManagerImpl(
    private val authDiskSource: AuthDiskSource,
) : AuthTokenManager {

    override fun getAuthTokenDataOrNull(userId: String): AuthTokenData? =
        authDiskSource
            .getAccountTokens(userId = userId)
            ?.takeIf { it.accessToken != null }
            ?.let {
                AuthTokenData(
                    userId = userId,
                    accessToken = requireNotNull(it.accessToken),
                    expiresAtSec = it.expiresAtSec,
                )
            }

    override fun getAuthTokenDataOrNull(): AuthTokenData? = authDiskSource
        .userState
        ?.activeUserId
        ?.let(::getAuthTokenDataOrNull)
}
