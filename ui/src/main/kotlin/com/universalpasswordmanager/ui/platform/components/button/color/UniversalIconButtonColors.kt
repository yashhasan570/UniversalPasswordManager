package com.universalpasswordmanager.ui.platform.components.button.color

import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Provides a default set of Universal-styled colors for a filled icon button.
 */
@Composable
fun bitwardenFilledIconButtonColors(): IconButtonColors = IconButtonColors(
    containerColor = UniversalTheme.colorScheme.filledButton.background,
    contentColor = UniversalTheme.colorScheme.filledButton.foreground,
    disabledContainerColor = UniversalTheme.colorScheme.filledButton.backgroundDisabled,
    disabledContentColor = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
)

/**
 * Provides a default set of Universal-styled colors for a standard icon button.
 */
@Composable
fun bitwardenStandardIconButtonColors(
    contentColor: Color = UniversalTheme.colorScheme.icon.primary,
): IconButtonColors = IconButtonColors(
    containerColor = Color.Transparent,
    contentColor = contentColor,
    disabledContainerColor = Color.Transparent,
    disabledContentColor = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
)

/**
 * Provides a default set of Universal-styled colors for a filled icon button.
 */
@Composable
fun bitwardenTonalIconButtonColors(): IconButtonColors = IconButtonColors(
    containerColor = UniversalTheme.colorScheme.background.tertiary,
    contentColor = UniversalTheme.colorScheme.filledButton.foregroundReversed,
    disabledContainerColor = UniversalTheme.colorScheme.filledButton.backgroundDisabled,
    disabledContentColor = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
)
