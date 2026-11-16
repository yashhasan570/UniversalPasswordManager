package com.work.api

import com.work.model.NetworkResult
import com.work.model.PushTokenRequest
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Defines API calls for push tokens.
 */
internal interface PushApi {
    @PUT("/devices/identifier/{appId}/token")
    suspend fun putDeviceToken(
        @Path("appId") appId: String,
        @Body body: PushTokenRequest,
    ): NetworkResult<Unit>
}
