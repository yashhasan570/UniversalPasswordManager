package com.universalpasswordmanager.ui.platform.util

import com.universalpasswordmanager.ui.platform.feature.settings.appearance.model.AppTheme
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText

/**
 * Returns a human-readable display label for the given [AppTheme].
 */
val AppTheme.displayLabel: Text
    get() = when (this) {
        AppTheme.DEFAULT -> UniversalString.default_system.asText()
        AppTheme.DARK -> UniversalString.dark.asText()
        AppTheme.LIGHT -> UniversalString.light.asText()
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
