package com.universalpasswordmanager.network.api

import com.universalpasswordmanager.network.model.NetworkResult
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Defines endpoints for the "have I been pwned" API. For docs see
 * https://haveibeenpwned.com/API/v2.
 */
internal interface HaveIBeenPwnedApi {

    @GET("/range/{hashPrefix}")
    suspend fun fetchBreachedPasswords(
        @Path("hashPrefix")
        hashPrefix: String,
    ): NetworkResult<ResponseBody>
}
