package com.x8bit.universalpasswordmanager.data.autofill.accessibility.util

import android.net.Uri
import androidx.core.net.toUri
import .annotation.OmitFromCoverage
import java.net.URISyntaxException

/**
 * Attempts to parse a [Uri] from a string and returns null if an error occurs.
 */
@OmitFromCoverage
fun String.toUriOrNull(): Uri? =
    try {
        this.toUri()
    } catch (_: URISyntaxException) {
        null
    }
