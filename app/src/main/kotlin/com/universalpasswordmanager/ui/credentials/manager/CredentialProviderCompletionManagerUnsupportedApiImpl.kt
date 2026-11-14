package com.universalpasswordmanager.ui.credentials.manager

import androidx.credentials.CredentialProvider
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.ui.credentials.manager.model.AssertFido2CredentialResult
import com.universalpasswordmanager.ui.credentials.manager.model.CreateCredentialResult
import com.universalpasswordmanager.ui.credentials.manager.model.GetCredentialsResult
import com.universalpasswordmanager.ui.credentials.manager.model.GetPasswordCredentialResult

/**
 * A no-op implementation of [CredentialProviderCompletionManagerImpl] provided when the build
 * version is below UPSIDE_DOWN_CAKE (34). These versions do not support [CredentialProvider].
 */
@OmitFromCoverage
object CredentialProviderCompletionManagerUnsupportedApiImpl : CredentialProviderCompletionManager {
    override fun completeCredentialRegistration(result: CreateCredentialResult) = Unit

    override fun completeFido2Assertion(result: AssertFido2CredentialResult) = Unit

    override fun completePasswordGet(result: GetPasswordCredentialResult) = Unit

    override fun completeProviderGetCredentialsRequest(result: GetCredentialsResult) = Unit
}
