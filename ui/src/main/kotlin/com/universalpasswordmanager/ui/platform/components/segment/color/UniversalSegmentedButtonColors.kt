package com.universalpasswordmanager.ui.platform.components.segment.color

import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Provides a default set of Universal-styled colors for segmented buttons.
 */
@Composable
fun bitwardenSegmentedButtonColors(): SegmentedButtonColors = SegmentedButtonColors(
    activeContainerColor = UniversalTheme.colorScheme.filledButton.backgroundReversed,
    activeContentColor = UniversalTheme.colorScheme.filledButton.foregroundReversed,
    activeBorderColor = Color.Transparent,
    inactiveContainerColor = UniversalTheme.colorScheme.background.primary,
    inactiveContentColor = UniversalTheme.colorScheme.text.secondary,
    inactiveBorderColor = Color.Transparent,
    disabledActiveContainerColor = UniversalTheme.colorScheme.background.primary,
    disabledActiveContentColor = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
    disabledActiveBorderColor = Color.Transparent,
    disabledInactiveContainerColor = UniversalTheme.colorScheme.background.primary,
    disabledInactiveContentColor = UniversalTheme.colorScheme.stroke.divider,
    disabledInactiveBorderColor = Color.Transparent,
)
