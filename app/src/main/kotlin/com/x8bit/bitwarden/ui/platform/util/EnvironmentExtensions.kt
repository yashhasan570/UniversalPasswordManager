package com.x8bit.universalpasswordmanager.ui.platform.util

import .data.repository.model.Environment
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText

/**
 * Returns a human-readable display label for the given [Environment.Type].
 */
val Environment.Type.displayLabel: Text
    get() = when (this) {
        Environment.Type.US -> Environment.Prod.Us.label.asText()
        Environment.Type.EU -> Environment.Prod.Eu.label.asText()
        Environment.Type.SELF_HOSTED -> Universal Password ManagerString.self_hosted.asText()
        Environment.Type.FED_RAMP -> Environment.Prod.FedRamp.label.asText()
    }
