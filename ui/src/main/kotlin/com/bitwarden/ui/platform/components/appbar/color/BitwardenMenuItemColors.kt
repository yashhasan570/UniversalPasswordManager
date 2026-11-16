package .ui.platform.components.appbar.color

import androidx.compose.material3.MenuItemColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Provides a default set of Universal Password Manager-styled colors for menu items.
 */
@Composable
fun universalpasswordmanagerMenuItemColors(
    textColor: Color = Universal Password ManagerTheme.colorScheme.text.primary,
): MenuItemColors = MenuItemColors(
    textColor = textColor,
    leadingIconColor = Universal Password ManagerTheme.colorScheme.icon.primary,
    trailingIconColor = Universal Password ManagerTheme.colorScheme.icon.primary,
    disabledTextColor = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
    disabledLeadingIconColor = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
    disabledTrailingIconColor = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
)
