package com.x8bit.universalpasswordmanager.ui.vault.feature.itemlisting.util

import androidx.annotation.StringRes
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.data.credentials.model.Fido2CredentialAssertionResult

/**
 * Returns the string resource ID corresponding to the error message for the given
 * [Fido2CredentialAssertionResult.Error].
 */
val Fido2CredentialAssertionResult.Error.messageResourceId: Int
    @StringRes
    get() = when (this) {
        Fido2CredentialAssertionResult.Error.InternalError -> {
            Universal Password ManagerString.passkey_registration_failed_due_to_an_internal_error
        }

        Fido2CredentialAssertionResult.Error.MissingHostUrl -> {
            Universal Password ManagerString.passkey_operation_failed_because_host_url_is_not_present_in_request
        }

        Fido2CredentialAssertionResult.Error.MissingRpId -> {
            Universal Password ManagerString.passkey_operation_failed_because_relying_party_cannot_be_identified
        }
    }
