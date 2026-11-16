package .ui.platform.components.toggle.color

import androidx.compose.material3.SwitchColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Provides a default set of Universal Password Manager-styled colors for switches.
 */
@Composable
fun universalpasswordmanagerSwitchColors(): SwitchColors = SwitchColors(
    checkedThumbColor = Universal Password ManagerTheme.colorScheme.toggleButton.switch,
    checkedTrackColor = Universal Password ManagerTheme.colorScheme.toggleButton.backgroundOn,
    checkedBorderColor = Color.Transparent,
    checkedIconColor = Universal Password ManagerTheme.colorScheme.toggleButton.backgroundOn,
    uncheckedThumbColor = Universal Password ManagerTheme.colorScheme.toggleButton.switch,
    uncheckedTrackColor = Universal Password ManagerTheme.colorScheme.toggleButton.backgroundOff,
    uncheckedBorderColor = Color.Transparent,
    uncheckedIconColor = Universal Password ManagerTheme.colorScheme.toggleButton.backgroundOn,
    disabledCheckedThumbColor = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
    disabledCheckedTrackColor = Universal Password ManagerTheme.colorScheme.filledButton.backgroundDisabled,
    disabledCheckedBorderColor = Color.Transparent,
    disabledCheckedIconColor = Universal Password ManagerTheme.colorScheme.filledButton.backgroundDisabled,
    disabledUncheckedThumbColor = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
    disabledUncheckedTrackColor = Universal Password ManagerTheme.colorScheme.filledButton.backgroundDisabled,
    disabledUncheckedBorderColor = Color.Transparent,
    disabledUncheckedIconColor = Universal Password ManagerTheme.colorScheme.filledButton.backgroundDisabled,
)
