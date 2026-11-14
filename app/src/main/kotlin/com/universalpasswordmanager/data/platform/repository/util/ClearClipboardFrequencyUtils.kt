package com.universalpasswordmanager.data.platform.repository.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.data.platform.repository.model.ClearClipboardFrequency

/**
 * Provides a human-readable display label for the given [ClearClipboardFrequency].
 */
val ClearClipboardFrequency.displayLabel: Text
    get() = when (this) {
        ClearClipboardFrequency.NEVER -> UniversalString.never
        ClearClipboardFrequency.TEN_SECONDS -> UniversalString.ten_seconds
        ClearClipboardFrequency.TWENTY_SECONDS -> UniversalString.twenty_seconds
        ClearClipboardFrequency.THIRTY_SECONDS -> UniversalString.thirty_seconds
        ClearClipboardFrequency.ONE_MINUTE -> UniversalString.one_minute
        ClearClipboardFrequency.TWO_MINUTES -> UniversalString.two_minutes
        ClearClipboardFrequency.FIVE_MINUTES -> UniversalString.five_minutes
    }
        .asText()
