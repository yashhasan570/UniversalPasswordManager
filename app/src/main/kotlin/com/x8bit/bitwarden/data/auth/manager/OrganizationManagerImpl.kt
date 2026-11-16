package com.x8bit.universalpasswordmanager.data.auth.manager

import com.work.model.OrganizationStatusType
import com.work.service.OrganizationService
import com.x8bit.universalpasswordmanager.data.auth.datasource.disk.AuthDiskSource
import com.x8bit.universalpasswordmanager.data.auth.repository.model.LeaveOrganizationResult
import com.x8bit.universalpasswordmanager.data.auth.repository.model.Organization
import com.x8bit.universalpasswordmanager.data.auth.repository.model.RevokeFromOrganizationResult
import com.x8bit.universalpasswordmanager.data.auth.repository.util.toOrganizations

/**
 * The default implementation of the [OrganizationManager].
 */
internal class OrganizationManagerImpl(
    private val authDiskSource: AuthDiskSource,
    private val organizationService: OrganizationService,
) : OrganizationManager {
    private val activeUserId: String? get() = authDiskSource.userState?.activeUserId

    override val organizations: List<Organization>
        get() = activeUserId
            ?.let { authDiskSource.getOrganizations(userId = it) }
            ?.filter { it.status == OrganizationStatusType.CONFIRMED }
            .orEmpty()
            .toOrganizations()

    override suspend fun leaveOrganization(
        organizationId: String,
    ): LeaveOrganizationResult =
        organizationService.leaveOrganization(organizationId = organizationId).fold(
            onSuccess = { LeaveOrganizationResult.Success },
            onFailure = { LeaveOrganizationResult.Error(error = it) },
        )

    override suspend fun revokeFromOrganization(
        organizationId: String,
    ): RevokeFromOrganizationResult =
        organizationService.revokeFromOrganization(organizationId = organizationId).fold(
            onSuccess = { RevokeFromOrganizationResult.Success },
            onFailure = { RevokeFromOrganizationResult.Error(error = it) },
        )
}
