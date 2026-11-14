package com.universalpasswordmanager.data.vault.datasource.sdk.model

import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.fido.CheckUserOptions
import com.universalpasswordmanager.sdk.CheckUserAndPickCredentialForCreationResult
import com.universalpasswordmanager.sdk.CheckUserResult
import com.universalpasswordmanager.sdk.CipherViewWrapper
import com.universalpasswordmanager.sdk.Fido2UserInterface
import com.universalpasswordmanager.sdk.UiHint
import com.universalpasswordmanager.vault.CipherView
import com.universalpasswordmanager.vault.Fido2CredentialNewView

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
