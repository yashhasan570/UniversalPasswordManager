package com.universalpasswordmanager.ui.platform.base.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.universalpasswordmanager.ui.platform.feature.settings.appearance.model.AppTheme
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.platform.theme.LocalUniversalColorScheme
import com.universalpasswordmanager.ui.platform.theme.LocalUniversalDynamicDarkColorScheme
import com.universalpasswordmanager.ui.platform.theme.LocalUniversalDynamicLightColorScheme
import com.universalpasswordmanager.ui.platform.theme.color.UniversalColorScheme
import com.universalpasswordmanager.ui.platform.theme.color.darkUniversalColorScheme
import com.universalpasswordmanager.ui.platform.theme.color.lightUniversalColorScheme

/**
 * Overrides the [UniversalColorScheme] for the [content] based on the provided [AppTheme].
 */
@Composable
fun ColorSchemeOverride(
    appTheme: AppTheme,
    content: @Composable () -> Unit,
) {
    val colorSchemeOverride = when (appTheme) {
        AppTheme.DEFAULT -> UniversalTheme.colorScheme
        AppTheme.DARK -> {
            if (UniversalTheme.colorScheme.isDynamicTheme) {
                LocalUniversalDynamicDarkColorScheme.current
            } else {
                darkUniversalColorScheme
            }
        }

        AppTheme.LIGHT -> {
            if (UniversalTheme.colorScheme.isDynamicTheme) {
                LocalUniversalDynamicLightColorScheme.current
            } else {
                lightUniversalColorScheme
            }
        }
    }
    CompositionLocalProvider(
        value = LocalUniversalColorScheme provides colorSchemeOverride,
        content = content,
    )
}
