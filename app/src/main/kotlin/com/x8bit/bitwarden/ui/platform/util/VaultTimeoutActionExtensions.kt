package com.x8bit.universalpasswordmanager.ui.platform.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText
import com.x8bit.universalpasswordmanager.data.platform.repository.model.VaultTimeoutAction

/**
 * Provides a human-readable display label for the given [VaultTimeoutAction].
 */
val VaultTimeoutAction.displayLabel: Text
    get() = when (this) {
        VaultTimeoutAction.LOCK -> Universal Password ManagerString.lock
        VaultTimeoutAction.LOGOUT -> Universal Password ManagerString.log_out
    }
        .asText()
