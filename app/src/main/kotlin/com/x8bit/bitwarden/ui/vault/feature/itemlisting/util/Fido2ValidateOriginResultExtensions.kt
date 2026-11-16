package com.x8bit.universalpasswordmanager.ui.vault.feature.itemlisting.util

import androidx.annotation.StringRes
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.data.credentials.model.ValidateOriginResult

/**
 * Returns the string resource ID corresponding to the error message for the given
 * [ValidateOriginResult.Error].
 */
val ValidateOriginResult.Error.messageResourceId: Int
    @StringRes
    get() = when (this) {
        ValidateOriginResult.Error.AssetLinkNotFound -> {
            Universal Password ManagerString.passkey_operation_failed_because_of_missing_asset_links
        }

        ValidateOriginResult.Error.PasskeyNotSupportedForApp -> {
            Universal Password ManagerString.passkeys_not_supported_for_this_app
        }

        ValidateOriginResult.Error.PrivilegedAppNotAllowed -> {
            Universal Password ManagerString.passkey_operation_failed_because_browser_is_not_privileged
        }

        ValidateOriginResult.Error.PrivilegedAppSignatureNotFound -> {
            Universal Password ManagerString.passkey_operation_failed_because_browser_signature_does_not_match
        }

        ValidateOriginResult.Error.Unknown -> {
            Universal Password ManagerString.generic_error_message
        }
    }
