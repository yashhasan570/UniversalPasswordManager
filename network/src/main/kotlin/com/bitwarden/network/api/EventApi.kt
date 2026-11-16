package com.work.api

import com.work.model.NetworkResult
import com.work.model.OrganizationEventJson
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * This interface defines the API service for posting event data.
 */
internal interface EventApi {
    @POST("/collect")
    suspend fun collectOrganizationEvents(
        @Body events: List<OrganizationEventJson>,
    ): NetworkResult<Unit>
}
