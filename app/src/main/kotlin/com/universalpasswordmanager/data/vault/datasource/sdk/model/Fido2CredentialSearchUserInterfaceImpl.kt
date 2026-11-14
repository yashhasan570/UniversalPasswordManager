package com.universalpasswordmanager.data.vault.datasource.sdk.model

import com.universalpasswordmanager.fido.CheckUserOptions
import com.universalpasswordmanager.sdk.CheckUserAndPickCredentialForCreationResult
import com.universalpasswordmanager.sdk.CheckUserResult
import com.universalpasswordmanager.sdk.CipherViewWrapper
import com.universalpasswordmanager.sdk.Fido2UserInterface
import com.universalpasswordmanager.sdk.UiHint
import com.universalpasswordmanager.vault.CipherView
import com.universalpasswordmanager.vault.Fido2CredentialNewView

/**
 * Implementation of [Fido2UserInterface] for searching for matching FIDO 2 credentials.
 */
class Fido2CredentialSearchUserInterfaceImpl : Fido2UserInterface {
    override suspend fun checkUser(
        options: CheckUserOptions,
        hint: UiHint,
    ): CheckUserResult =
        CheckUserResult(
            userPresent = true,
            userVerified = true,
        )

    override suspend fun checkUserAndPickCredentialForCreation(
        options: CheckUserOptions,
        newCredential: Fido2CredentialNewView,
    ): CheckUserAndPickCredentialForCreationResult = throw IllegalStateException()

    // Always return true for this property because any problems with verification should
    // be handled downstream where the app can actually offer verification methods.
    override fun isVerificationEnabled(): Boolean = true

    override suspend fun pickCredentialForAuthentication(
        availableCredentials: List<CipherView>,
    ): CipherViewWrapper = throw IllegalStateException()
}
