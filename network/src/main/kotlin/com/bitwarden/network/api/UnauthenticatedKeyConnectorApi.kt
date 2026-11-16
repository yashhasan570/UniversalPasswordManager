package com.work.api

import androidx.annotation.Keep
import com.work.model.KeyConnectorMasterKeyRequestJson
import com.work.model.NetworkResult
import com.work.util.HEADER_KEY_AUTHORIZATION
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * Defines raw calls specific for key connectors that use custom urls.
 */
@Keep
internal interface UnauthenticatedKeyConnectorApi {
    @POST
    suspend fun storeMasterKeyToKeyConnector(
        @Url url: String,
        @Header(HEADER_KEY_AUTHORIZATION) bearerToken: String,
        @Body body: KeyConnectorMasterKeyRequestJson,
    ): NetworkResult<Unit>
}
