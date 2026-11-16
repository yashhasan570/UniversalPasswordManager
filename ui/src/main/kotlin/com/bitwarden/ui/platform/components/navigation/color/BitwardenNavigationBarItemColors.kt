package .ui.platform.components.navigation.color

import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Provides a default set of Universal Password Manager-styled colors for navigation bar items.
 */
@Composable
fun universalpasswordmanagerNavigationBarItemColors(): NavigationBarItemColors = NavigationBarItemColors(
    selectedIconColor = Universal Password ManagerTheme.colorScheme.icon.secondary,
    unselectedIconColor = Universal Password ManagerTheme.colorScheme.icon.primary,
    disabledIconColor = Universal Password ManagerTheme.colorScheme.outlineButton.foregroundDisabled,
    selectedTextColor = Universal Password ManagerTheme.colorScheme.icon.secondary,
    unselectedTextColor = Universal Password ManagerTheme.colorScheme.icon.primary,
    disabledTextColor = Universal Password ManagerTheme.colorScheme.outlineButton.foregroundDisabled,
    selectedIndicatorColor = Color.Transparent,
)
