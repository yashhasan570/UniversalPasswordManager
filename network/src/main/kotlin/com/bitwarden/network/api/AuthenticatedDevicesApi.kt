package com.work.api

import androidx.annotation.Keep
import com.work.model.DevicesResponseJson
import com.work.model.NetworkResult
import com.work.model.TrustedDeviceKeysRequestJson
import com.work.model.TrustedDeviceKeysResponseJson
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Defines raw calls under the /devices API that require authentication.
 */
@Keep
internal interface AuthenticatedDevicesApi {
    @GET("/devices")
    suspend fun getDevices(): NetworkResult<DevicesResponseJson>

    @PUT("/devices/{appId}/keys")
    suspend fun updateTrustedDeviceKeys(
        @Path(value = "appId") appId: String,
        @Body request: TrustedDeviceKeysRequestJson,
    ): NetworkResult<TrustedDeviceKeysResponseJson>
}
