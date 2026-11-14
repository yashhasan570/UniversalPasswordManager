package com.universalpasswordmanager.data.platform.manager.event

import com.universalpasswordmanager.data.platform.manager.model.OrganizationEvent

/**
 * A manager for tracking events.
 */
interface OrganizationEventManager {
    /**
     * Tracks a specific event to be uploaded at a different time.
     */
    fun trackEvent(event: OrganizationEvent)
}
