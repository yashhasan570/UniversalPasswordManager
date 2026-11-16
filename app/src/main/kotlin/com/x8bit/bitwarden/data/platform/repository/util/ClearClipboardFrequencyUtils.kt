package com.x8bit.universalpasswordmanager.data.platform.repository.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText
import com.x8bit.universalpasswordmanager.data.platform.repository.model.ClearClipboardFrequency

/**
 * Provides a human-readable display label for the given [ClearClipboardFrequency].
 */
val ClearClipboardFrequency.displayLabel: Text
    get() = when (this) {
        ClearClipboardFrequency.NEVER -> Universal Password ManagerString.never
        ClearClipboardFrequency.TEN_SECONDS -> Universal Password ManagerString.ten_seconds
        ClearClipboardFrequency.TWENTY_SECONDS -> Universal Password ManagerString.twenty_seconds
        ClearClipboardFrequency.THIRTY_SECONDS -> Universal Password ManagerString.thirty_seconds
        ClearClipboardFrequency.ONE_MINUTE -> Universal Password ManagerString.one_minute
        ClearClipboardFrequency.TWO_MINUTES -> Universal Password ManagerString.two_minutes
        ClearClipboardFrequency.FIVE_MINUTES -> Universal Password ManagerString.five_minutes
    }
        .asText()
