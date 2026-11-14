package com.universalpasswordmanager.ui.credentials.manager

import com.universalpasswordmanager.ui.credentials.manager.model.AssertFido2CredentialResult
import com.universalpasswordmanager.ui.credentials.manager.model.CreateCredentialResult
import com.universalpasswordmanager.ui.credentials.manager.model.GetCredentialsResult
import com.universalpasswordmanager.ui.credentials.manager.model.GetPasswordCredentialResult

/**
 * A manager for completing the credential creation process.
 */
interface CredentialProviderCompletionManager {

    /**
     * Completes the credential registration process with the provided [result].
     */
    fun completeCredentialRegistration(result: CreateCredentialResult)

    /**
     * Complete the FIDO 2 credential assertion process with the provided [result].
     */
    fun completeFido2Assertion(result: AssertFido2CredentialResult)

    /**
     * Complete the Password credential retrieval process with the provided [result].
     */
    fun completePasswordGet(result: GetPasswordCredentialResult)

    /**
     * Complete the CredentialManager "Get credentials" process with the provided [result].
     */
    fun completeProviderGetCredentialsRequest(result: GetCredentialsResult)
}
