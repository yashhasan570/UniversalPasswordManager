package com.universalpasswordmanager.data.auth.datasource.sdk

import com.universalpasswordmanager.auth.JitMasterPasswordRegistrationRequest
import com.universalpasswordmanager.auth.JitMasterPasswordRegistrationResponse
import com.universalpasswordmanager.auth.KeyConnectorRegistrationResult
import com.universalpasswordmanager.auth.PasswordPreloginResponse
import com.universalpasswordmanager.auth.TdeRegistrationRequest
import com.universalpasswordmanager.auth.TdeRegistrationResponse
import com.universalpasswordmanager.auth.UserMasterPasswordRegistrationRequest
import com.universalpasswordmanager.auth.UserMasterPasswordRegistrationResponse
import com.universalpasswordmanager.core.AuthRequestResponse
import com.universalpasswordmanager.core.FingerprintRequest
import com.universalpasswordmanager.core.KeyConnectorResponse
import com.universalpasswordmanager.core.MasterPasswordPolicyOptions
import com.universalpasswordmanager.core.RegisterKeyResponse
import com.universalpasswordmanager.core.RegisterTdeKeyResponse
import com.universalpasswordmanager.core.data.manager.dispatcher.DispatcherManager
import com.universalpasswordmanager.crypto.HashPurpose
import com.universalpasswordmanager.crypto.Kdf
import com.universalpasswordmanager.policies.OrganizationUserPolicyContext
import com.universalpasswordmanager.policies.Policy
import com.universalpasswordmanager.policies.PolicyType
import com.universalpasswordmanager.sdk.AuthClient
import com.universalpasswordmanager.data.auth.datasource.sdk.model.PasswordStrength
import com.universalpasswordmanager.data.auth.datasource.sdk.util.toPasswordStrengthOrNull
import com.universalpasswordmanager.data.auth.datasource.sdk.util.toUByte
import com.universalpasswordmanager.data.platform.datasource.sdk.BaseSdkSource
import com.universalpasswordmanager.data.platform.manager.SdkClientManager
import kotlinx.coroutines.withContext

/**
 * Primary implementation of [AuthSdkSource] that serves as a convenience wrapper around a
 * [AuthClient].
 */
@Suppress("TooManyFunctions")
class AuthSdkSourceImpl(
    private val dispatcherManager: DispatcherManager,
    sdkClientManager: SdkClientManager,
) : BaseSdkSource(sdkClientManager = sdkClientManager),
    AuthSdkSource {

    override suspend fun preLogin(
        email: String,
    ): Result<PasswordPreloginResponse> = runCatchingWithLogs {
        withContext(context = dispatcherManager.io) {
            useClient { auth().login().getPasswordPrelogin(email = email) }
        }
    }

    override suspend fun postKeysForJitPasswordRegistration(
        userId: String,
        organizationId: String,
        organizationPublicKey: String,
        organizationSsoIdentifier: String,
        salt: String,
        masterPassword: String,
        masterPasswordHint: String?,
        shouldResetPasswordEnroll: Boolean,
    ): Result<JitMasterPasswordRegistrationResponse> = runCatchingWithLogs {
        withContext(context = dispatcherManager.io) {
            getClient(userId = userId).auth().registration().postKeysForJitPasswordRegistration(
                request = JitMasterPasswordRegistrationRequest(
                    orgId = organizationId,
                    orgPublicKey = organizationPublicKey,
                    userId = userId,
                    organizationSsoIdentifier = organizationSsoIdentifier,
                    salt = salt,
                    masterPassword = masterPassword,
                    masterPasswordHint = masterPasswordHint,
                    resetPasswordEnroll = shouldResetPasswordEnroll,
                ),
            )
        }
    }

    override suspend fun postKeysForKeyConnectorRegistration(
        userId: String,
        accessToken: String,
        keyConnectorUrl: String,
        ssoOrganizationIdentifier: String,
    ): Result<KeyConnectorRegistrationResult> = runCatchingWithLogs {
        withContext(context = dispatcherManager.io) {
            useClient(userId = userId, accessToken = accessToken) {
                auth().registration().postKeysForKeyConnectorRegistration(
                    keyConnectorUrl = keyConnectorUrl,
                    ssoOrgIdentifier = ssoOrganizationIdentifier,
                )
            }
        }
    }

    override suspend fun postKeysForTdeRegistration(
        userId: String,
        organizationId: String,
        organizationPublicKey: String,
        deviceIdentifier: String,
        shouldTrustDevice: Boolean,
    ): Result<TdeRegistrationResponse> = runCatchingWithLogs {
        withContext(context = dispatcherManager.io) {
            getClient(userId = userId).auth().registration().postKeysForTdeRegistration(
                request = TdeRegistrationRequest(
                    orgId = organizationId,
                    orgPublicKey = organizationPublicKey,
                    userId = userId,
                    deviceIdentifier = deviceIdentifier,
                    trustDevice = shouldTrustDevice,
                ),
            )
        }
    }

    override suspend fun postKeysForUserPasswordRegistration(
        email: String,
        salt: String,
        masterPassword: String,
        masterPasswordHint: String?,
        emailVerificationToken: String,
    ): Result<UserMasterPasswordRegistrationResponse> = runCatchingWithLogs {
        withContext(context = dispatcherManager.io) {
            useClient {
                auth().registration().postKeysForUserPasswordRegistration(
                    request = UserMasterPasswordRegistrationRequest(
                        email = email,
                        salt = salt,
                        masterPassword = masterPassword,
                        masterPasswordHint = masterPasswordHint,
                        emailVerificationToken = emailVerificationToken,
                        organizationUserId = null,
                        orgInviteToken = null,
                        orgSponsoredFreeFamilyPlanToken = null,
                        acceptEmergencyAccessInviteToken = null,
                        acceptEmergencyAccessId = null,
                        providerInviteToken = null,
                        providerUserId = null,
                        salesAssistedToken = null,
                        openOrgInvite = null,
                    ),
                )
            }
        }
    }

    override suspend fun getNewAuthRequest(
        email: String,
    ): Result<AuthRequestResponse> = runCatchingWithLogs {
        useClient { auth().newAuthRequest(email = email.lowercase()) }
    }

    override suspend fun getUserFingerprint(
        email: String,
        publicKey: String,
    ): Result<String> = runCatchingWithLogs {
        useClient {
            platform().fingerprint(
                req = FingerprintRequest(
                    fingerprintMaterial = email.lowercase(),
                    publicKey = publicKey,
                ),
            )
        }
    }

    override suspend fun hashPassword(
        salt: String,
        password: String,
        kdf: Kdf,
        purpose: HashPurpose,
    ): Result<String> = runCatchingWithLogs {
        useClient {
            // Under the hood, the "email" parameter is the salt.
            auth().hashPassword(
                email = salt,
                password = password,
                kdfParams = kdf,
                purpose = purpose,
            )
        }
    }

    override suspend fun makeKeyConnectorKeys(): Result<KeyConnectorResponse> =
        runCatchingWithLogs {
            useClient { auth().makeKeyConnectorKeys() }
        }

    override suspend fun makeRegisterKeys(
        email: String,
        password: String,
        kdf: Kdf,
    ): Result<RegisterKeyResponse> = runCatchingWithLogs {
        useClient {
            auth().makeRegisterKeys(
                email = email,
                password = password,
                kdf = kdf,
            )
        }
    }

    override suspend fun makeRegisterTdeKeysAndUnlockVault(
        userId: String,
        email: String,
        orgPublicKey: String,
        rememberDevice: Boolean,
    ): Result<RegisterTdeKeyResponse> = runCatchingWithLogs {
        getClient(userId = userId)
            .auth()
            .makeRegisterTdeKeys(
                email = email,
                orgPublicKey = orgPublicKey,
                rememberDevice = rememberDevice,
            )
    }

    override fun passwordStrength(
        email: String,
        password: String,
        additionalInputs: List<String>,
    ): Result<PasswordStrength> = runCatchingWithLogs {
        requireNotNull(
            globalClient
                .auth()
                .passwordStrength(
                    password = password,
                    email = email,
                    additionalInputs = additionalInputs,
                )
                .toPasswordStrengthOrNull(),
        )
    }

    override fun satisfiesPolicy(
        password: String,
        passwordStrength: PasswordStrength,
        policy: MasterPasswordPolicyOptions,
    ): Result<Boolean> = runCatchingWithLogs {
        globalClient.auth().satisfiesPolicy(
            password = password,
            strength = passwordStrength.toUByte(),
            policy = policy,
        )
    }

    override fun filterPolicies(
        policies: List<Policy>,
        organizations: List<OrganizationUserPolicyContext>,
        policyType: PolicyType,
    ): Result<List<Policy>> = runCatchingWithLogs {
        globalClient.policies().filterByType(
            policies = policies,
            organizationUserPolicyContexts = organizations,
            policyType = policyType,
        )
    }
}
