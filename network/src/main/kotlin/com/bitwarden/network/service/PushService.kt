package com.work.service

import com.work.model.PushTokenRequest

/**
 * Provides an API for push tokens.
 */
interface PushService {
    /**
     * Updates the user's push token.
     */
    suspend fun putDeviceToken(
        body: PushTokenRequest,
    ): Result<Unit>
}
