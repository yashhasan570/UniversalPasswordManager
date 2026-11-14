package com.universalpasswordmanager.ui.platform.components.radio.color

import androidx.compose.material3.RadioButtonColors
import androidx.compose.runtime.Composable
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Provides a default set of Universal-styled colors for radio buttons.
 */
@Composable
fun bitwardenRadioButtonColors(): RadioButtonColors = RadioButtonColors(
    selectedColor = UniversalTheme.colorScheme.filledButton.background,
    unselectedColor = UniversalTheme.colorScheme.icon.primary,
    disabledSelectedColor = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
    disabledUnselectedColor = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
)
