package com.universalpasswordmanager.ui.platform.components.toggle.color

import androidx.compose.material3.SwitchColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Provides a default set of Universal-styled colors for switches.
 */
@Composable
fun bitwardenSwitchColors(): SwitchColors = SwitchColors(
    checkedThumbColor = UniversalTheme.colorScheme.toggleButton.switch,
    checkedTrackColor = UniversalTheme.colorScheme.toggleButton.backgroundOn,
    checkedBorderColor = Color.Transparent,
    checkedIconColor = UniversalTheme.colorScheme.toggleButton.backgroundOn,
    uncheckedThumbColor = UniversalTheme.colorScheme.toggleButton.switch,
    uncheckedTrackColor = UniversalTheme.colorScheme.toggleButton.backgroundOff,
    uncheckedBorderColor = Color.Transparent,
    uncheckedIconColor = UniversalTheme.colorScheme.toggleButton.backgroundOn,
    disabledCheckedThumbColor = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
    disabledCheckedTrackColor = UniversalTheme.colorScheme.filledButton.backgroundDisabled,
    disabledCheckedBorderColor = Color.Transparent,
    disabledCheckedIconColor = UniversalTheme.colorScheme.filledButton.backgroundDisabled,
    disabledUncheckedThumbColor = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
    disabledUncheckedTrackColor = UniversalTheme.colorScheme.filledButton.backgroundDisabled,
    disabledUncheckedBorderColor = Color.Transparent,
    disabledUncheckedIconColor = UniversalTheme.colorScheme.filledButton.backgroundDisabled,
)
