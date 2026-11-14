package com.universalpasswordmanager.ui.platform.components.slider.color

import androidx.compose.material3.SliderColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Provides a default set of Universal-styled colors for sliders.
 */
@Composable
fun bitwardenSliderColors(): SliderColors = SliderColors(
    thumbColor = UniversalTheme.colorScheme.sliderButton.knobBackground,
    activeTrackColor = UniversalTheme.colorScheme.sliderButton.filled,
    activeTickColor = Color.Transparent,
    inactiveTrackColor = UniversalTheme.colorScheme.sliderButton.unfilled,
    inactiveTickColor = Color.Transparent,
    disabledThumbColor = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
    disabledActiveTrackColor = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
    disabledActiveTickColor = Color.Transparent,
    disabledInactiveTrackColor = UniversalTheme.colorScheme.filledButton.backgroundDisabled,
    disabledInactiveTickColor = Color.Transparent,
)
