package com.universalpasswordmanager.ui.credentials.manager.model

import com.universalpasswordmanager.ui.util.Text

/**
 * Represents the result of Password authentication.
 */
sealed class GetPasswordCredentialResult {
    /**
     * Represents a successful authentication of Password credentials.
     */
    data class Success(
        val username: String?,
        val password: String,
    ) : GetPasswordCredentialResult()

    /**
     * Indicates the user canceled authentication.
     */
    data object Cancelled : GetPasswordCredentialResult()

    /**
     * Represents an error during Password credential assertion.
     */
    data class Error(val message: Text) : GetPasswordCredentialResult()
}
