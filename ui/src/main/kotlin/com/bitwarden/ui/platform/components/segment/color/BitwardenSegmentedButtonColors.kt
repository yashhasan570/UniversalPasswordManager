package .ui.platform.components.segment.color

import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Provides a default set of Universal Password Manager-styled colors for segmented buttons.
 */
@Composable
fun universalpasswordmanagerSegmentedButtonColors(): SegmentedButtonColors = SegmentedButtonColors(
    activeContainerColor = Universal Password ManagerTheme.colorScheme.filledButton.backgroundReversed,
    activeContentColor = Universal Password ManagerTheme.colorScheme.filledButton.foregroundReversed,
    activeBorderColor = Color.Transparent,
    inactiveContainerColor = Universal Password ManagerTheme.colorScheme.background.primary,
    inactiveContentColor = Universal Password ManagerTheme.colorScheme.text.secondary,
    inactiveBorderColor = Color.Transparent,
    disabledActiveContainerColor = Universal Password ManagerTheme.colorScheme.background.primary,
    disabledActiveContentColor = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
    disabledActiveBorderColor = Color.Transparent,
    disabledInactiveContainerColor = Universal Password ManagerTheme.colorScheme.background.primary,
    disabledInactiveContentColor = Universal Password ManagerTheme.colorScheme.stroke.divider,
    disabledInactiveBorderColor = Color.Transparent,
)
