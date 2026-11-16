package com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.model

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText

/**
 * Represents the different actions that can be taken in a custom
 * item edit menu.
 */
enum class CustomFieldAction(val actionText: Text) {
    EDIT(Universal Password ManagerString.edit.asText()),
    MOVE_UP(Universal Password ManagerString.move_up.asText()),
    MOVE_DOWN(Universal Password ManagerString.move_down.asText()),
    REMOVE(Universal Password ManagerString.remove.asText()),
}
