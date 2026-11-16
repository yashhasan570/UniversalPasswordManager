package com.x8bit.universalpasswordmanager.ui.platform.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText
import com.x8bit.universalpasswordmanager.data.platform.repository.model.VaultTimeout

/**
 * Provides a human-readable display label for the given [VaultTimeout.Type].
 */
val VaultTimeout.Type.displayLabel: Text
    get() = when (this) {
        VaultTimeout.Type.IMMEDIATELY -> Universal Password ManagerString.immediately
        VaultTimeout.Type.ONE_MINUTE -> Universal Password ManagerString.one_minute
        VaultTimeout.Type.FIVE_MINUTES -> Universal Password ManagerString.five_minutes
        VaultTimeout.Type.FIFTEEN_MINUTES -> Universal Password ManagerString.fifteen_minutes
        VaultTimeout.Type.THIRTY_MINUTES -> Universal Password ManagerString.thirty_minutes
        VaultTimeout.Type.ONE_HOUR -> Universal Password ManagerString.one_hour
        VaultTimeout.Type.FOUR_HOURS -> Universal Password ManagerString.four_hours
        VaultTimeout.Type.ON_APP_RESTART -> Universal Password ManagerString.on_restart
        VaultTimeout.Type.NEVER -> Universal Password ManagerString.never
        VaultTimeout.Type.CUSTOM -> Universal Password ManagerString.custom
    }
        .asText()

/**
 * The value in minutes for the given [VaultTimeout.Type], used as a comparison
 * against the maximum timeout allowed by the organization's policy.
 */
@Suppress("MagicNumber")
val VaultTimeout.Type.minutes: Int
    get() = when (this) {
        VaultTimeout.Type.IMMEDIATELY -> 0
        VaultTimeout.Type.ONE_MINUTE -> 1
        VaultTimeout.Type.FIVE_MINUTES -> 5
        VaultTimeout.Type.FIFTEEN_MINUTES -> 15
        VaultTimeout.Type.THIRTY_MINUTES -> 30
        VaultTimeout.Type.ONE_HOUR -> 60
        VaultTimeout.Type.FOUR_HOURS -> 240

        VaultTimeout.Type.ON_APP_RESTART,
        VaultTimeout.Type.NEVER,
            -> Int.MAX_VALUE

        VaultTimeout.Type.CUSTOM -> 0
    }
