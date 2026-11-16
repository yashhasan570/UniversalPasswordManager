package com.work.service

import com.work.api.EventApi
import com.work.model.OrganizationEventJson
import com.work.util.toResult

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
