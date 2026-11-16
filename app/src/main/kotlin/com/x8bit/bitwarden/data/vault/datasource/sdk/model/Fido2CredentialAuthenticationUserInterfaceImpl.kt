package com.x8bit.universalpasswordmanager.data.vault.datasource.sdk.model

import .annotation.OmitFromCoverage
import .fido.CheckUserOptions
import .sdk.CheckUserAndPickCredentialForCreationResult
import .sdk.CheckUserResult
import .sdk.CipherViewWrapper
import .sdk.Fido2UserInterface
import .sdk.UiHint
import .vault.CipherView
import .vault.Fido2CredentialNewView

/**
 * Implementation of [Fido2UserInterface] for authenticating with a FIDO 2 credential.
 */
@OmitFromCoverage
class Fido2CredentialAuthenticationUserInterfaceImpl(
    private val selectedCipherView: CipherView,
    private val isVerificationSupported: Boolean,
) : Fido2UserInterface {
    override suspend fun checkUser(
        options: CheckUserOptions,
        hint: UiHint,
    ): CheckUserResult = CheckUserResult(userPresent = true, userVerified = true)

    override suspend fun checkUserAndPickCredentialForCreation(
        options: CheckUserOptions,
        newCredential: Fido2CredentialNewView,
    ): CheckUserAndPickCredentialForCreationResult = throw IllegalStateException()

    override fun isVerificationEnabled(): Boolean = isVerificationSupported

    override suspend fun pickCredentialForAuthentication(
        availableCredentials: List<CipherView>,
    ): CipherViewWrapper = CipherViewWrapper(selectedCipherView)
}
