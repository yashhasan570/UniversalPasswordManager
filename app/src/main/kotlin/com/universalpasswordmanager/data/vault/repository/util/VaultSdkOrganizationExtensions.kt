package com.universalpasswordmanager.data.vault.repository.util

import com.universalpasswordmanager.network.model.OrganizationStatusType
import com.universalpasswordmanager.network.model.OrganizationType
import com.universalpasswordmanager.network.model.SyncResponseJson
import com.universalpasswordmanager.organizations.OrganizationUserStatusType
import com.universalpasswordmanager.organizations.OrganizationUserType
import com.universalpasswordmanager.policies.OrganizationUserPolicyContext

/**
 * Converts a list of network [SyncResponseJson.Profile.Organization] models to a list of SDK
 * [OrganizationUserPolicyContext].
 */
@Suppress("MaxLineLength")
fun List<SyncResponseJson.Profile.Organization>.toSdkOrganizationPolicyContexts(): List<OrganizationUserPolicyContext> =
    this.map { it.toSdkOrganizationPolicyContext() }

/**
 * Converts a network [SyncResponseJson.Profile.Organization] model to an SDK
 * [OrganizationUserPolicyContext].
 */
@Suppress("MaxLineLength")
fun SyncResponseJson.Profile.Organization.toSdkOrganizationPolicyContext(): OrganizationUserPolicyContext =
    OrganizationUserPolicyContext(
        id = this.id,
        status = this.status.toSdkOrganizationUserStatusType,
        role = this.type.toSdkOrganizationUserType,
        enabled = this.isEnabled,
        usePolicies = this.shouldUsePolicies,
        isProviderUser = this.isProviderUser,
    )

private val OrganizationStatusType.toSdkOrganizationUserStatusType: OrganizationUserStatusType
    get() = when (this) {
        OrganizationStatusType.REVOKED -> OrganizationUserStatusType.REVOKED
        OrganizationStatusType.INVITED -> OrganizationUserStatusType.INVITED
        OrganizationStatusType.ACCEPTED -> OrganizationUserStatusType.ACCEPTED
        OrganizationStatusType.CONFIRMED -> OrganizationUserStatusType.CONFIRMED
        OrganizationStatusType.STAGED -> OrganizationUserStatusType.STAGED
    }

private val OrganizationType.toSdkOrganizationUserType: OrganizationUserType
    get() = when (this) {
        OrganizationType.OWNER -> OrganizationUserType.OWNER
        OrganizationType.ADMIN -> OrganizationUserType.ADMIN
        OrganizationType.USER -> OrganizationUserType.USER
        OrganizationType.CUSTOM -> OrganizationUserType.CUSTOM
    }
