package com.universalpasswordmanager.ui.vault.feature.itemlisting.util

import androidx.annotation.StringRes
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.data.credentials.model.ValidateOriginResult

/**
 * Returns the string resource ID corresponding to the error message for the given
 * [ValidateOriginResult.Error].
 */
val ValidateOriginResult.Error.messageResourceId: Int
    @StringRes
    get() = when (this) {
        ValidateOriginResult.Error.AssetLinkNotFound -> {
            UniversalString.passkey_operation_failed_because_of_missing_asset_links
        }

        ValidateOriginResult.Error.PasskeyNotSupportedForApp -> {
            UniversalString.passkeys_not_supported_for_this_app
        }

        ValidateOriginResult.Error.PrivilegedAppNotAllowed -> {
            UniversalString.passkey_operation_failed_because_browser_is_not_privileged
        }

        ValidateOriginResult.Error.PrivilegedAppSignatureNotFound -> {
            UniversalString.passkey_operation_failed_because_browser_signature_does_not_match
        }

        ValidateOriginResult.Error.Unknown -> {
            UniversalString.generic_error_message
        }
    }
