package com.work.service

import com.work.api.PushApi
import com.work.model.PushTokenRequest
import com.work.util.toResult

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
