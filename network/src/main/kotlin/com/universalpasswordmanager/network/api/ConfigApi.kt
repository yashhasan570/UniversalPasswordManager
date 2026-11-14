package com.universalpasswordmanager.network.api

import com.universalpasswordmanager.network.model.ConfigResponseJson
import com.universalpasswordmanager.network.model.NetworkResult
import retrofit2.http.GET

/**
 * This interface defines the API service for fetching configuration data.
 */
internal interface ConfigApi {

    @GET("config")
    suspend fun getConfig(): NetworkResult<ConfigResponseJson>
}
