package com.universalpasswordmanager.network.service

import com.universalpasswordmanager.network.api.PushApi
import com.universalpasswordmanager.network.model.PushTokenRequest
import com.universalpasswordmanager.network.util.toResult

/**
 * Default implementation of [PushService].
 */
internal class PushServiceImpl(
    private val pushApi: PushApi,
    private val appId: String,
) : PushService {
    override suspend fun putDeviceToken(
        body: PushTokenRequest,
    ): Result<Unit> =
        pushApi
            .putDeviceToken(
                appId = appId,
                body = body,
            )
            .toResult()
}
