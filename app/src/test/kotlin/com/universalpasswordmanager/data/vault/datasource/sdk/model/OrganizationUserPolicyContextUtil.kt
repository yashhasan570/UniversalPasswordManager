package com.universalpasswordmanager.data.vault.datasource.sdk.model

import com.universalpasswordmanager.organizations.OrganizationUserStatusType
import com.universalpasswordmanager.organizations.OrganizationUserType
import com.universalpasswordmanager.policies.OrganizationUserPolicyContext

/**
 * Create a mock [OrganizationUserPolicyContext] with a given [number].
 */
@Suppress("LongParameterList")
fun createMockOrganizationUserPolicyContext(
    number: Int = 1,
    id: String = "mockId-$number",
    status: OrganizationUserStatusType = OrganizationUserStatusType.CONFIRMED,
    role: OrganizationUserType = OrganizationUserType.ADMIN,
    enabled: Boolean = false,
    usePolicies: Boolean = false,
    isProviderUser: Boolean = false,
): OrganizationUserPolicyContext =
    OrganizationUserPolicyContext(
        id = id,
        status = status,
        role = role,
        enabled = enabled,
        usePolicies = usePolicies,
        isProviderUser = isProviderUser,
    )
