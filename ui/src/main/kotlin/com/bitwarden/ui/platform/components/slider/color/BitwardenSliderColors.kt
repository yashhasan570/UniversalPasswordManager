package .ui.platform.components.slider.color

import androidx.compose.material3.SliderColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Provides a default set of Universal Password Manager-styled colors for sliders.
 */
@Composable
fun universalpasswordmanagerSliderColors(): SliderColors = SliderColors(
    thumbColor = Universal Password ManagerTheme.colorScheme.sliderButton.knobBackground,
    activeTrackColor = Universal Password ManagerTheme.colorScheme.sliderButton.filled,
    activeTickColor = Color.Transparent,
    inactiveTrackColor = Universal Password ManagerTheme.colorScheme.sliderButton.unfilled,
    inactiveTickColor = Color.Transparent,
    disabledThumbColor = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
    disabledActiveTrackColor = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
    disabledActiveTickColor = Color.Transparent,
    disabledInactiveTrackColor = Universal Password ManagerTheme.colorScheme.filledButton.backgroundDisabled,
    disabledInactiveTickColor = Color.Transparent,
)
