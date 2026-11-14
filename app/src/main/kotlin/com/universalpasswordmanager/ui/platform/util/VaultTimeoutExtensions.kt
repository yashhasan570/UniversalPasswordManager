package com.universalpasswordmanager.ui.platform.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.data.platform.repository.model.VaultTimeout

/**
 * Provides a human-readable display label for the given [VaultTimeout.Type].
 */
val VaultTimeout.Type.displayLabel: Text
    get() = when (this) {
        VaultTimeout.Type.IMMEDIATELY -> UniversalString.immediately
        VaultTimeout.Type.ONE_MINUTE -> UniversalString.one_minute
        VaultTimeout.Type.FIVE_MINUTES -> UniversalString.five_minutes
        VaultTimeout.Type.FIFTEEN_MINUTES -> UniversalString.fifteen_minutes
        VaultTimeout.Type.THIRTY_MINUTES -> UniversalString.thirty_minutes
        VaultTimeout.Type.ONE_HOUR -> UniversalString.one_hour
        VaultTimeout.Type.FOUR_HOURS -> UniversalString.four_hours
        VaultTimeout.Type.ON_APP_RESTART -> UniversalString.on_restart
        VaultTimeout.Type.NEVER -> UniversalString.never
        VaultTimeout.Type.CUSTOM -> UniversalString.custom
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
