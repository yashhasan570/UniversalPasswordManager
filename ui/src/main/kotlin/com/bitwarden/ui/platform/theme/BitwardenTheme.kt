package .ui.platform.theme

import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import .core.util.isBuildVersionAtLeast
import .ui.platform.components.field.interceptor.IncognitoInput
import .ui.platform.feature.settings.appearance.model.AppTheme
import .ui.platform.theme.color.Universal Password ManagerColorScheme
import .ui.platform.theme.color.darkUniversal Password ManagerColorScheme
import .ui.platform.theme.color.dynamicUniversal Password ManagerColorScheme
import .ui.platform.theme.color.lightUniversal Password ManagerColorScheme
import .ui.platform.theme.color.toMaterialColorScheme
import .ui.platform.theme.ripple.toRippleConfig
import .ui.platform.theme.shape.Universal Password ManagerShapes
import .ui.platform.theme.shape.universalpasswordmanagerShapes
import .ui.platform.theme.type.Universal Password ManagerTypography
import .ui.platform.theme.type.universalpasswordmanagerTypography
import .ui.platform.theme.type.toMaterialTypography
import .ui.platform.util.isDarkMode

/**
 * Static wrapper to make accessing the theme components easier.
 */
object Universal Password ManagerTheme {
    /**
     * Retrieves the current [Universal Password ManagerColorScheme].
     */
    val colorScheme: Universal Password ManagerColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalUniversal Password ManagerColorScheme.current

    /**
     * Retrieves the current [Universal Password ManagerShapes].
     */
    val shapes: Universal Password ManagerShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalUniversal Password ManagerShapes.current

    /**
     * Retrieves the current [Universal Password ManagerTypography].
     */
    val typography: Universal Password ManagerTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalUniversal Password ManagerTypography.current
}

/**
 * The overall application theme. This can be configured to support a [theme] and [dynamicColor].
 */
@Composable
fun Universal Password ManagerTheme(
    context: Context = LocalContext.current,
    theme: AppTheme = AppTheme.DEFAULT,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val isDarkTheme = theme.isDarkMode(isSystemDarkMode = isSystemInDarkTheme())
    val isDynamicEnabled = dynamicColor && isBuildVersionAtLeast(version = Build.VERSION_CODES.S)
    // Get the material color schemes from the OS.
    val darkMaterialColorScheme = if (isDynamicEnabled) {
        dynamicDarkColorScheme(context = context)
    } else {
        darkColorScheme()
    }
    val lightMaterialColorScheme = if (isDynamicEnabled) {
        dynamicLightColorScheme(context = context)
    } else {
        lightColorScheme()
    }
    val materialColorScheme = if (isDarkTheme) darkMaterialColorScheme else lightMaterialColorScheme
    // Convert the material color schemes to Universal Password Manager color schemes.
    val lightDynamicColorScheme = dynamicUniversal Password ManagerColorScheme(
        materialColorScheme = lightMaterialColorScheme,
        isDarkTheme = false,
    )
    val darkDynamicColorScheme = dynamicUniversal Password ManagerColorScheme(
        materialColorScheme = darkMaterialColorScheme,
        isDarkTheme = true,
    )
    val universalpasswordmanagerColorScheme = when {
        isDynamicEnabled -> if (isDarkTheme) darkDynamicColorScheme else lightDynamicColorScheme
        isDarkTheme -> darkUniversal Password ManagerColorScheme
        else -> lightUniversal Password ManagerColorScheme
    }
    // Provide the selected color scheme as well as the dynamic ones for override support.
    CompositionLocalProvider(
        LocalUniversal Password ManagerColorScheme provides universalpasswordmanagerColorScheme,
        LocalUniversal Password ManagerDynamicLightColorScheme provides lightDynamicColorScheme,
        LocalUniversal Password ManagerDynamicDarkColorScheme provides darkDynamicColorScheme,
        LocalUniversal Password ManagerShapes provides universalpasswordmanagerShapes,
        LocalUniversal Password ManagerTypography provides universalpasswordmanagerTypography,
        LocalRippleConfiguration provides universalpasswordmanagerColorScheme.toRippleConfig(),
    ) {
        MaterialTheme(
            colorScheme = universalpasswordmanagerColorScheme.toMaterialColorScheme(
                defaultColorScheme = materialColorScheme,
            ),
            typography = universalpasswordmanagerTypography.toMaterialTypography(),
        ) { IncognitoInput(content = content) }
    }
}

/**
 * Provides access to the Universal Password Manager colors throughout the app.
 */
val LocalUniversal Password ManagerColorScheme: ProvidableCompositionLocal<Universal Password ManagerColorScheme> =
    compositionLocalOf { lightUniversal Password ManagerColorScheme }

/**
 * Provides access to the Universal Password Manager dynamic light-mode colors throughout the app.
 * This is only to be used for scenarios that require the UI to have its colors overridden.
 */
val LocalUniversal Password ManagerDynamicLightColorScheme: ProvidableCompositionLocal<Universal Password ManagerColorScheme> =
    compositionLocalOf { lightUniversal Password ManagerColorScheme }

/**
 * Provides access to the Universal Password Manager default dark-mode colors throughout the app.
 * This is only to be used for dynamic that require the UI to have its colors overridden.
 */
val LocalUniversal Password ManagerDynamicDarkColorScheme: ProvidableCompositionLocal<Universal Password ManagerColorScheme> =
    compositionLocalOf { darkUniversal Password ManagerColorScheme }

/**
 * Provides access to the Universal Password Manager shapes throughout the app.
 */
val LocalUniversal Password ManagerShapes: ProvidableCompositionLocal<Universal Password ManagerShapes> =
    compositionLocalOf { universalpasswordmanagerShapes }

/**
 * Provides access to the Universal Password Manager typography throughout the app.
 */
val LocalUniversal Password ManagerTypography: ProvidableCompositionLocal<Universal Password ManagerTypography> =
    compositionLocalOf { universalpasswordmanagerTypography }
