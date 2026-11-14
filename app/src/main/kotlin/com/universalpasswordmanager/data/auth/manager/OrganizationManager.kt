package com.universalpasswordmanager.data.auth.manager

import com.universalpasswordmanager.data.auth.repository.model.LeaveOrganizationResult
import com.universalpasswordmanager.data.auth.repository.model.Organization
import com.universalpasswordmanager.data.auth.repository.model.RevokeFromOrganizationResult

/**
 * Manager used to manage organizations.
 */
interface OrganizationManager {
    /**
     * The organization for the active user.
     */
    val organizations: List<Organization>

    /**
     * Leaves the organization that matches the given [organizationId]
     */
    suspend fun leaveOrganization(organizationId: String): LeaveOrganizationResult

    /**
     * Revokes self from the organization that matches the given [organizationId]
     */
    suspend fun revokeFromOrganization(organizationId: String): RevokeFromOrganizationResult
}
