package com.work.api

import androidx.annotation.Keep
import com.work.model.KeyConnectorMasterKeyRequestJson
import com.work.model.NetworkResult
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * Defines raw calls specific for key connectors that use custom urls.
 */
@Keep
internal interface AuthenticatedKeyConnectorApi {
    @POST
    suspend fun storeMasterKeyToKeyConnector(
        @Url url: String,
        @Body body: KeyConnectorMasterKeyRequestJson,
    ): NetworkResult<Unit>
}
