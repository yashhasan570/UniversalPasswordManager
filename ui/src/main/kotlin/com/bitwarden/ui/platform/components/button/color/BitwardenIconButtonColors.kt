package .ui.platform.components.button.color

import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Provides a default set of Universal Password Manager-styled colors for a filled icon button.
 */
@Composable
fun universalpasswordmanagerFilledIconButtonColors(): IconButtonColors = IconButtonColors(
    containerColor = Universal Password ManagerTheme.colorScheme.filledButton.background,
    contentColor = Universal Password ManagerTheme.colorScheme.filledButton.foreground,
    disabledContainerColor = Universal Password ManagerTheme.colorScheme.filledButton.backgroundDisabled,
    disabledContentColor = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
)

/**
 * Provides a default set of Universal Password Manager-styled colors for a standard icon button.
 */
@Composable
fun universalpasswordmanagerStandardIconButtonColors(
    contentColor: Color = Universal Password ManagerTheme.colorScheme.icon.primary,
): IconButtonColors = IconButtonColors(
    containerColor = Color.Transparent,
    contentColor = contentColor,
    disabledContainerColor = Color.Transparent,
    disabledContentColor = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
)

/**
 * Provides a default set of Universal Password Manager-styled colors for a filled icon button.
 */
@Composable
fun universalpasswordmanagerTonalIconButtonColors(): IconButtonColors = IconButtonColors(
    containerColor = Universal Password ManagerTheme.colorScheme.background.tertiary,
    contentColor = Universal Password ManagerTheme.colorScheme.filledButton.foregroundReversed,
    disabledContainerColor = Universal Password ManagerTheme.colorScheme.filledButton.backgroundDisabled,
    disabledContentColor = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
)
