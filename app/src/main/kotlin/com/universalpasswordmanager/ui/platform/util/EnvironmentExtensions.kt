package com.universalpasswordmanager.ui.platform.util

import com.universalpasswordmanager.data.repository.model.Environment
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText

/**
 * Returns a human-readable display label for the given [Environment.Type].
 */
val Environment.Type.displayLabel: Text
    get() = when (this) {
        Environment.Type.US -> Environment.Prod.Us.label.asText()
        Environment.Type.EU -> Environment.Prod.Eu.label.asText()
        Environment.Type.SELF_HOSTED -> UniversalString.self_hosted.asText()
        Environment.Type.FED_RAMP -> Environment.Prod.FedRamp.label.asText()
    }
