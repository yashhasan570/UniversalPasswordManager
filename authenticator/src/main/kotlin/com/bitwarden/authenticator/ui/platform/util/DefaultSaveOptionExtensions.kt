package .authenticator.ui.platform.util

import .authenticator.ui.platform.feature.settings.data.model.DefaultSaveOption
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText

/**
 * Returns a human-readable display label for the given [DefaultSaveOption].
 */
val DefaultSaveOption.displayLabel: Text
    get() = when (this) {
        DefaultSaveOption.NONE -> Universal Password ManagerString.none.asText()
        DefaultSaveOption.LOCAL -> Universal Password ManagerString.save_here.asText()
        DefaultSaveOption.UNIVERSAL PASSWORD MANAGER_APP -> Universal Password ManagerString.save_to_universalpasswordmanager.asText()
    }
