package com.work.service

import com.work.api.AuthenticatedOrganizationApi
import com.work.api.UnauthenticatedOrganizationApi
import com.work.model.OrganizationAutoEnrollStatusResponseJson
import com.work.model.OrganizationKeysResponseJson
import com.work.model.OrganizationResetPasswordEnrollRequestJson
import com.work.model.VerifiedOrganizationDomainSsoDetailsRequest
import com.work.model.VerifiedOrganizationDomainSsoDetailsResponse
import com.work.util.toResult

/**
 * Default implementation of [OrganizationService].
 */
internal class OrganizationServiceImpl(
    private val authenticatedOrganizationApi: AuthenticatedOrganizationApi,
    private val unauthenticatedOrganizationApi: UnauthenticatedOrganizationApi,
) : OrganizationService {
    override suspend fun organizationResetPasswordEnroll(
        organizationId: String,
        userId: String,
        passwordHash: String?,
        resetPasswordKey: String,
    ): Result<Unit> = authenticatedOrganizationApi
        .organizationResetPasswordEnroll(
            organizationId = organizationId,
            userId = userId,
            body = OrganizationResetPasswordEnrollRequestJson(
                passwordHash = passwordHash,
                resetPasswordKey = resetPasswordKey,
            ),
        )
        .toResult()

    override suspend fun getOrganizationAutoEnrollStatus(
        organizationIdentifier: String,
    ): Result<OrganizationAutoEnrollStatusResponseJson> = authenticatedOrganizationApi
        .getOrganizationAutoEnrollResponse(
            organizationIdentifier = organizationIdentifier,
        )
        .toResult()

    override suspend fun getOrganizationKeys(
        organizationId: String,
    ): Result<OrganizationKeysResponseJson> = authenticatedOrganizationApi
        .getOrganizationKeys(
            organizationId = organizationId,
        )
        .toResult()

    override suspend fun getVerifiedOrganizationDomainSsoDetails(
        email: String,
    ): Result<VerifiedOrganizationDomainSsoDetailsResponse> = unauthenticatedOrganizationApi
        .getVerifiedOrganizationDomainsByEmail(
            body = VerifiedOrganizationDomainSsoDetailsRequest(
                email = email,
            ),
        )
        .toResult()

    override suspend fun leaveOrganization(organizationId: String): Result<Unit> =
        authenticatedOrganizationApi
            .leaveOrganization(organizationId = organizationId)
            .toResult()

    override suspend fun revokeFromOrganization(organizationId: String): Result<Unit> =
        authenticatedOrganizationApi
            .revokeFromOrganization(organizationId = organizationId)
            .toResult()
}
