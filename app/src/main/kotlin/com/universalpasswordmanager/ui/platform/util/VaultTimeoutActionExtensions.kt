package com.universalpasswordmanager.ui.platform.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.data.platform.repository.model.VaultTimeoutAction

/**
 * Provides a human-readable display label for the given [VaultTimeoutAction].
 */
val VaultTimeoutAction.displayLabel: Text
    get() = when (this) {
        VaultTimeoutAction.LOCK -> UniversalString.lock
        VaultTimeoutAction.LOGOUT -> UniversalString.log_out
    }
        .asText()
