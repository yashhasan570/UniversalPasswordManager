package com.x8bit.universalpasswordmanager.ui.tools.feature.send.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.model.SendItemType

/**
 * Returns the selection text based on the given [SendItemType].
 */
val SendItemType.selectionText: Text
    get() = when (this) {
        SendItemType.FILE -> Universal Password ManagerString.file.asText()
        SendItemType.TEXT -> Universal Password ManagerString.text.asText()
    }
