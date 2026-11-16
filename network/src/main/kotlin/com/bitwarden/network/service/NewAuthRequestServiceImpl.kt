package com.work.service

import .core.data.util.asFailure
import com.work.api.AuthenticatedAuthRequestsApi
import com.work.api.UnauthenticatedAuthRequestsApi
import com.work.model.AuthRequestRequestJson
import com.work.model.AuthRequestTypeJson
import com.work.model.AuthRequestsResponseJson
import com.work.util.toResult

/**
 * The default implementation of the [NewAuthRequestService].
 */
internal class NewAuthRequestServiceImpl(
    private val authenticatedAuthRequestsApi: AuthenticatedAuthRequestsApi,
    private val unauthenticatedAuthRequestsApi: UnauthenticatedAuthRequestsApi,
) : NewAuthRequestService {
    override suspend fun createAuthRequest(
        email: String,
        publicKey: String,
        deviceId: String,
        accessCode: String,
        fingerprint: String,
        authRequestType: AuthRequestTypeJson,
    ): Result<AuthRequestsResponseJson.AuthRequest> =
        when (authRequestType) {
            AuthRequestTypeJson.LOGIN_WITH_DEVICE -> {
                unauthenticatedAuthRequestsApi
                    .createAuthRequest(
                        body = AuthRequestRequestJson(
                            email = email,
                            publicKey = publicKey,
                            deviceId = deviceId,
                            accessCode = accessCode,
                            fingerprint = fingerprint,
                            type = authRequestType,
                        ),
                    )
                    .toResult()
            }

            AuthRequestTypeJson.UNLOCK -> {
                UnsupportedOperationException("Unlock AuthRequestType is currently unsupported")
                    .asFailure()
            }

            AuthRequestTypeJson.ADMIN_APPROVAL -> {
                authenticatedAuthRequestsApi
                    .createAdminAuthRequest(
                        deviceIdentifier = deviceId,
                        body = AuthRequestRequestJson(
                            email = email,
                            publicKey = publicKey,
                            deviceId = deviceId,
                            accessCode = accessCode,
                            fingerprint = fingerprint,
                            type = authRequestType,
                        ),
                    )
                    .toResult()
            }
        }

    override suspend fun getAuthRequestUpdate(
        requestId: String,
        accessCode: String,
        isSso: Boolean,
    ): Result<AuthRequestsResponseJson.AuthRequest> =
        if (isSso) {
            authenticatedAuthRequestsApi
                .getAuthRequest(requestId = requestId)
                .toResult()
        } else {
            unauthenticatedAuthRequestsApi
                .getAuthRequestUpdate(
                    requestId = requestId,
                    accessCode = accessCode,
                )
                .toResult()
        }
}
