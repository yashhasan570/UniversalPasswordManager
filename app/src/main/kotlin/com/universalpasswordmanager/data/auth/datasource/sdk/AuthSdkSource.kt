package com.universalpasswordmanager.data.auth.datasource.sdk

import com.universalpasswordmanager.auth.JitMasterPasswordRegistrationResponse
import com.universalpasswordmanager.auth.KeyConnectorRegistrationResult
import com.universalpasswordmanager.auth.PasswordPreloginResponse
import com.universalpasswordmanager.auth.TdeRegistrationResponse
import com.universalpasswordmanager.auth.UserMasterPasswordRegistrationResponse
import com.universalpasswordmanager.core.AuthRequestResponse
import com.universalpasswordmanager.core.KeyConnectorResponse
import com.universalpasswordmanager.core.MasterPasswordPolicyOptions
import com.universalpasswordmanager.core.RegisterKeyResponse
import com.universalpasswordmanager.core.RegisterTdeKeyResponse
import com.universalpasswordmanager.crypto.HashPurpose
import com.universalpasswordmanager.crypto.Kdf
import com.universalpasswordmanager.policies.OrganizationUserPolicyContext
import com.universalpasswordmanager.policies.Policy
import com.universalpasswordmanager.policies.PolicyType
import com.universalpasswordmanager.data.auth.datasource.sdk.model.PasswordStrength

/**
 * Source of authentication information and functionality from the Universal SDK.
 */
@Suppress("TooManyFunctions")
interface AuthSdkSource {
    /**
     * Performs the pre-login request for the given email address.
     */
    suspend fun preLogin(email: String): Result<PasswordPreloginResponse>

    /**
     * Enrolls the user to master password unlock.
     */
    @Suppress("LongParameterList")
    suspend fun postKeysForJitPasswordRegistration(
        userId: String,
        organizationId: String,
        organizationPublicKey: String,
        organizationSsoIdentifier: String,
        salt: String,
        masterPassword: String,
        masterPasswordHint: String?,
        shouldResetPasswordEnroll: Boolean,
    ): Result<JitMasterPasswordRegistrationResponse>

    /**
     * Enrolls the user to key connector unlock.
     */
    suspend fun postKeysForKeyConnectorRegistration(
        userId: String,
        accessToken: String,
        keyConnectorUrl: String,
        ssoOrganizationIdentifier: String,
    ): Result<KeyConnectorRegistrationResult>

    /**
     * Enrolls the user to TDE unlock.
     */
    suspend fun postKeysForTdeRegistration(
        userId: String,
        organizationId: String,
        organizationPublicKey: String,
        deviceIdentifier: String,
        shouldTrustDevice: Boolean,
    ): Result<TdeRegistrationResponse>

    /**
     * Enrolls the user for password unlock.
     */
    suspend fun postKeysForUserPasswordRegistration(
        email: String,
        salt: String,
        masterPassword: String,
        masterPasswordHint: String?,
        emailVerificationToken: String,
    ): Result<UserMasterPasswordRegistrationResponse>

    /**
     * Gets the data needed to create a new auth request.
     */
    suspend fun getNewAuthRequest(
        email: String,
    ): Result<AuthRequestResponse>

    /**
     * Gets the fingerprint phrase for this [email] and [publicKey].
     */
    suspend fun getUserFingerprint(
        email: String,
        publicKey: String,
    ): Result<String>

    /**
     * Creates a hashed password provided the given [salt], [password], [kdf], and [purpose].
     */
    suspend fun hashPassword(
        salt: String,
        password: String,
        kdf: Kdf,
        purpose: HashPurpose,
    ): Result<String>

    /**
     * Creates a set of encryption key information for use with a key connector.
     */
    suspend fun makeKeyConnectorKeys(): Result<KeyConnectorResponse>

    /**
     * Creates a set of encryption key information for registration.
     */
    suspend fun makeRegisterKeys(
        email: String,
        password: String,
        kdf: Kdf,
    ): Result<RegisterKeyResponse>

    /**
     * Creates a set of encryption key information for registration of a trusted device and unlocks
     * the vault for the user.
     */
    suspend fun makeRegisterTdeKeysAndUnlockVault(
        userId: String,
        email: String,
        orgPublicKey: String,
        rememberDevice: Boolean,
    ): Result<RegisterTdeKeyResponse>

    /**
     * Checks the password strength for the given [email] and [password] combination, along with
     * some [additionalInputs].
     */
    fun passwordStrength(
        email: String,
        password: String,
        additionalInputs: List<String> = emptyList(),
    ): Result<PasswordStrength>

    /**
     * Checks that the given [password] with the given [passwordStrength] satisfies the given
     * [policy]. Returns `true` if so and `false` otherwise.
     */
    fun satisfiesPolicy(
        password: String,
        passwordStrength: PasswordStrength,
        policy: MasterPasswordPolicyOptions,
    ): Result<Boolean>

    /**
     * Applies the appropriate filters for determining what policies apply to the user.
     */
    fun filterPolicies(
        policies: List<Policy>,
        organizations: List<OrganizationUserPolicyContext>,
        policyType: PolicyType,
    ): Result<List<Policy>>
}
