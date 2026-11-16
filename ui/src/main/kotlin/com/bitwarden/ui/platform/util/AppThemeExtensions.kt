package .ui.platform.util

import .ui.platform.feature.settings.appearance.model.AppTheme
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText

/**
 * Returns a human-readable display label for the given [AppTheme].
 */
val AppTheme.displayLabel: Text
    get() = when (this) {
        AppTheme.DEFAULT -> Universal Password ManagerString.default_system.asText()
        AppTheme.DARK -> Universal Password ManagerString.dark.asText()
        AppTheme.LIGHT -> Universal Password ManagerString.light.asText()
    }

/**
 * Returns `true` if the app is currently using dark mode.
 */
fun AppTheme.isDarkMode(
    isSystemDarkMode: Boolean,
): Boolean =
    when (this) {
        AppTheme.DEFAULT -> isSystemDarkMode
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
    }
