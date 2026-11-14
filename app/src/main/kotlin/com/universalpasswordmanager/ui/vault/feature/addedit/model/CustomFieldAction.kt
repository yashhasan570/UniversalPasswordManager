package com.universalpasswordmanager.ui.vault.feature.addedit.model

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText

/**
 * Represents the different actions that can be taken in a custom
 * item edit menu.
 */
enum class CustomFieldAction(val actionText: Text) {
    EDIT(UniversalString.edit.asText()),
    MOVE_UP(UniversalString.move_up.asText()),
    MOVE_DOWN(UniversalString.move_down.asText()),
    REMOVE(UniversalString.remove.asText()),
}
