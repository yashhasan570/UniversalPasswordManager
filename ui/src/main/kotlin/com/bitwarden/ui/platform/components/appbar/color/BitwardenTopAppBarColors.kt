package .ui.platform.components.appbar.color

import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Provides a default set of Universal Password Manager-styled colors for top app bars.
 */
@Composable
fun universalpasswordmanagerTopAppBarColors(): TopAppBarColors = TopAppBarColors(
    containerColor = Universal Password ManagerTheme.colorScheme.background.secondary,
    scrolledContainerColor = Universal Password ManagerTheme.colorScheme.background.secondary,
    navigationIconContentColor = Universal Password ManagerTheme.colorScheme.icon.primary,
    titleContentColor = Universal Password ManagerTheme.colorScheme.text.primary,
    actionIconContentColor = Universal Password ManagerTheme.colorScheme.icon.primary,
    subtitleContentColor = Universal Password ManagerTheme.colorScheme.text.primary,
)
