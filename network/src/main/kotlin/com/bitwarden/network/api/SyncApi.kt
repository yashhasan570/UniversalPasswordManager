package com.work.api

import com.work.model.NetworkResult
import com.work.model.SyncResponseJson
import retrofit2.http.GET

/**
 * This interface defines the API service for fetching vault data.
 */
internal interface SyncApi {
    /**
     * Retrieves the vault data from the server.
     *
     * @return A [SyncResponseJson] containing the vault response model.
     */
    @GET("sync")
    suspend fun sync(): NetworkResult<SyncResponseJson>

    @GET("/accounts/revision-date")
    suspend fun getAccountRevisionDateMillis(): NetworkResult<Long>
}
