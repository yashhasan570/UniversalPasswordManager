package com.universalpasswordmanager.data.platform.manager.sdk.repository

import com.universalpasswordmanager.core.ClientManagedTokens
import com.universalpasswordmanager.data.auth.datasource.disk.AuthDiskSource

/**
 * A user-scoped implementation of a Universal SDK [ClientManagedTokens].
 *
 * Note: This intentionally provides the raw stored token without proactive expiration checks
 * or refresh logic. The SDK handles automatic token refresh internally.
 */
class SdkTokenRepository(
    private val userId: String?,
    private val accessToken: String?,
    private val authDiskSource: AuthDiskSource,
) : ClientManagedTokens {
    override suspend fun getAccessToken(): String? =
        accessToken ?: userId?.let { authDiskSource.getAccountTokens(userId = it)?.accessToken }
}
