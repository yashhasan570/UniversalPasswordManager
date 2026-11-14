package com.universalpasswordmanager.ui.platform.components.appbar.color

import androidx.compose.material3.MenuItemColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Provides a default set of Universal-styled colors for menu items.
 */
@Composable
fun universalpasswordmanagerMenuItemColors(
    textColor: Color = UniversalTheme.colorScheme.text.primary,
): MenuItemColors = MenuItemColors(
    textColor = textColor,
    leadingIconColor = UniversalTheme.colorScheme.icon.primary,
    trailingIconColor = UniversalTheme.colorScheme.icon.primary,
    disabledTextColor = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
    disabledLeadingIconColor = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
    disabledTrailingIconColor = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
)
