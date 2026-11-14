package com.universalpasswordmanager.network.api

import androidx.annotation.Keep
import com.universalpasswordmanager.network.model.DevicesResponseJson
import com.universalpasswordmanager.network.model.NetworkResult
import com.universalpasswordmanager.network.model.TrustedDeviceKeysRequestJson
import com.universalpasswordmanager.network.model.TrustedDeviceKeysResponseJson
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
