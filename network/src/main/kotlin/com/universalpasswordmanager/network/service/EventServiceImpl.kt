package com.universalpasswordmanager.network.service

import com.universalpasswordmanager.network.api.EventApi
import com.universalpasswordmanager.network.model.OrganizationEventJson
import com.universalpasswordmanager.network.util.toResult

/**
 * The default implementation of the [EventService].
 */
internal class EventServiceImpl(
    private val eventApi: EventApi,
) : EventService {
    override suspend fun sendOrganizationEvents(
        events: List<OrganizationEventJson>,
    ): Result<Unit> = eventApi.collectOrganizationEvents(events = events).toResult()
}
