package com.work.api

import com.work.model.ConfigResponseJson
import com.work.model.NetworkResult
import retrofit2.http.GET

/**
 * This interface defines the API service for fetching configuration data.
 */
internal interface ConfigApi {

    @GET("config")
    suspend fun getConfig(): NetworkResult<ConfigResponseJson>
}
