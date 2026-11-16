package .ui.platform.components.radio.color

import androidx.compose.material3.RadioButtonColors
import androidx.compose.runtime.Composable
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Provides a default set of Universal Password Manager-styled colors for radio buttons.
 */
@Composable
fun universalpasswordmanagerRadioButtonColors(): RadioButtonColors = RadioButtonColors(
    selectedColor = Universal Password ManagerTheme.colorScheme.filledButton.background,
    unselectedColor = Universal Password ManagerTheme.colorScheme.icon.primary,
    disabledSelectedColor = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
    disabledUnselectedColor = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
)
