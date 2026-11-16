package com.x8bit.universalpasswordmanager.data.platform.datasource.network.authenticator

import com.work.provider.RefreshTokenProvider
import com.x8bit.universalpasswordmanager.data.auth.repository.model.LogoutReason

/**
 * A provider for all the functionality needed to properly refresh the users access token.
 */
interface AuthenticatorProvider : RefreshTokenProvider {

    /**
     * The currently active user's ID.
     */
    val activeUserId: String?

    /**
     * Attempts to logout the user based on the [userId].
     */
    fun logout(userId: String, reason: LogoutReason)
}
