package com.universalpasswordmanager.ui.platform.theme

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
import com.universalpasswordmanager.core.util.isBuildVersionAtLeast
import com.universalpasswordmanager.ui.platform.components.field.interceptor.IncognitoInput
import com.universalpasswordmanager.ui.platform.feature.settings.appearance.model.AppTheme
import com.universalpasswordmanager.ui.platform.theme.color.UniversalColorScheme
import com.universalpasswordmanager.ui.platform.theme.color.darkUniversalColorScheme
import com.universalpasswordmanager.ui.platform.theme.color.dynamicUniversalColorScheme
import com.universalpasswordmanager.ui.platform.theme.color.lightUniversalColorScheme
import com.universalpasswordmanager.ui.platform.theme.color.toMaterialColorScheme
import com.universalpasswordmanager.ui.platform.theme.ripple.toRippleConfig
import com.universalpasswordmanager.ui.platform.theme.shape.UniversalShapes
import com.universalpasswordmanager.ui.platform.theme.shape.universalpasswordmanagerShapes
import com.universalpasswordmanager.ui.platform.theme.type.UniversalTypography
import com.universalpasswordmanager.ui.platform.theme.type.universalpasswordmanagerTypography
import com.universalpasswordmanager.ui.platform.theme.type.toMaterialTypography
import com.universalpasswordmanager.ui.platform.util.isDarkMode

/**
 * Static wrapper to make accessing the theme components easier.
 */
object UniversalTheme {
    /**
     * Retrieves the current [UniversalColorScheme].
     */
    val colorScheme: UniversalColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalUniversalColorScheme.current

    /**
     * Retrieves the current [UniversalShapes].
     */
    val shapes: UniversalShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalUniversalShapes.current

    /**
     * Retrieves the current [UniversalTypography].
     */
    val typography: UniversalTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalUniversalTypography.current
}

/**
 * The overall application theme. This can be configured to support a [theme] and [dynamicColor].
 */
@Composable
fun UniversalTheme(
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
    // Convert the material color schemes to Universal color schemes.
    val lightDynamicColorScheme = dynamicUniversalColorScheme(
        materialColorScheme = lightMaterialColorScheme,
        isDarkTheme = false,
    )
    val darkDynamicColorScheme = dynamicUniversalColorScheme(
        materialColorScheme = darkMaterialColorScheme,
        isDarkTheme = true,
    )
    val universalpasswordmanagerColorScheme = when {
        isDynamicEnabled -> if (isDarkTheme) darkDynamicColorScheme else lightDynamicColorScheme
        isDarkTheme -> darkUniversalColorScheme
        else -> lightUniversalColorScheme
    }
    // Provide the selected color scheme as well as the dynamic ones for override support.
    CompositionLocalProvider(
        LocalUniversalColorScheme provides universalpasswordmanagerColorScheme,
        LocalUniversalDynamicLightColorScheme provides lightDynamicColorScheme,
        LocalUniversalDynamicDarkColorScheme provides darkDynamicColorScheme,
        LocalUniversalShapes provides universalpasswordmanagerShapes,
        LocalUniversalTypography provides universalpasswordmanagerTypography,
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
 * Provides access to the Universal colors throughout the app.
 */
val LocalUniversalColorScheme: ProvidableCompositionLocal<UniversalColorScheme> =
    compositionLocalOf { lightUniversalColorScheme }

/**
 * Provides access to the Universal dynamic light-mode colors throughout the app.
 * This is only to be used for scenarios that require the UI to have its colors overridden.
 */
val LocalUniversalDynamicLightColorScheme: ProvidableCompositionLocal<UniversalColorScheme> =
    compositionLocalOf { lightUniversalColorScheme }

/**
 * Provides access to the Universal default dark-mode colors throughout the app.
 * This is only to be used for dynamic that require the UI to have its colors overridden.
 */
val LocalUniversalDynamicDarkColorScheme: ProvidableCompositionLocal<UniversalColorScheme> =
    compositionLocalOf { darkUniversalColorScheme }

/**
 * Provides access to the Universal shapes throughout the app.
 */
val LocalUniversalShapes: ProvidableCompositionLocal<UniversalShapes> =
    compositionLocalOf { universalpasswordmanagerShapes }

/**
 * Provides access to the Universal typography throughout the app.
 */
val LocalUniversalTypography: ProvidableCompositionLocal<UniversalTypography> =
    compositionLocalOf { universalpasswordmanagerTypography }
