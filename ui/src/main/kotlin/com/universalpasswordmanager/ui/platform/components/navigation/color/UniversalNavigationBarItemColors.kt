package com.universalpasswordmanager.ui.platform.components.navigation.color

import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Provides a default set of Universal-styled colors for navigation bar items.
 */
@Composable
fun universalpasswordmanagerNavigationBarItemColors(): NavigationBarItemColors = NavigationBarItemColors(
    selectedIconColor = UniversalTheme.colorScheme.icon.secondary,
    unselectedIconColor = UniversalTheme.colorScheme.icon.primary,
    disabledIconColor = UniversalTheme.colorScheme.outlineButton.foregroundDisabled,
    selectedTextColor = UniversalTheme.colorScheme.icon.secondary,
    unselectedTextColor = UniversalTheme.colorScheme.icon.primary,
    disabledTextColor = UniversalTheme.colorScheme.outlineButton.foregroundDisabled,
    selectedIndicatorColor = Color.Transparent,
)
