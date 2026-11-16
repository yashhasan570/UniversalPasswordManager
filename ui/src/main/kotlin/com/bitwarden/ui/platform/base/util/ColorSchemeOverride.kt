package .ui.platform.base.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import .ui.platform.feature.settings.appearance.model.AppTheme
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.platform.theme.LocalUniversal Password ManagerColorScheme
import .ui.platform.theme.LocalUniversal Password ManagerDynamicDarkColorScheme
import .ui.platform.theme.LocalUniversal Password ManagerDynamicLightColorScheme
import .ui.platform.theme.color.Universal Password ManagerColorScheme
import .ui.platform.theme.color.darkUniversal Password ManagerColorScheme
import .ui.platform.theme.color.lightUniversal Password ManagerColorScheme

/**
 * Overrides the [Universal Password ManagerColorScheme] for the [content] based on the provided [AppTheme].
 */
@Composable
fun ColorSchemeOverride(
    appTheme: AppTheme,
    content: @Composable () -> Unit,
) {
    val colorSchemeOverride = when (appTheme) {
        AppTheme.DEFAULT -> Universal Password ManagerTheme.colorScheme
        AppTheme.DARK -> {
            if (Universal Password ManagerTheme.colorScheme.isDynamicTheme) {
                LocalUniversal Password ManagerDynamicDarkColorScheme.current
            } else {
                darkUniversal Password ManagerColorScheme
            }
        }

        AppTheme.LIGHT -> {
            if (Universal Password ManagerTheme.colorScheme.isDynamicTheme) {
                LocalUniversal Password ManagerDynamicLightColorScheme.current
            } else {
                lightUniversal Password ManagerColorScheme
            }
        }
    }
    CompositionLocalProvider(
        value = LocalUniversal Password ManagerColorScheme provides colorSchemeOverride,
        content = content,
    )
}
