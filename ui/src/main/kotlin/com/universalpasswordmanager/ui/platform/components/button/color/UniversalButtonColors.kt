package com.universalpasswordmanager.ui.platform.components.button.color

import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalOutlinedButtonColors
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Provides a default set of Universal-styled colors for a filled button.
 */
@Composable
fun bitwardenFilledButtonColors(): ButtonColors = ButtonColors(
    containerColor = UniversalTheme.colorScheme.filledButton.background,
    contentColor = UniversalTheme.colorScheme.filledButton.foreground,
    disabledContainerColor = UniversalTheme.colorScheme.filledButton.backgroundDisabled,
    disabledContentColor = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
)

/**
 * Provides a default set of Universal-styled colors for a filled error button.
 */
@Composable
fun bitwardenFilledErrorButtonColors() = ButtonColors(
    containerColor = UniversalTheme.colorScheme.status.weak1,
    contentColor = UniversalTheme.colorScheme.filledButton.foreground,
    disabledContainerColor = UniversalTheme.colorScheme.filledButton.backgroundDisabled,
    disabledContentColor = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
)

/**
 * Provides a default set of Universal-styled colors for an outlined button.
 */
@Composable
fun bitwardenOutlinedButtonColors(
    contentColor: Color = UniversalTheme.colorScheme.outlineButton.foreground,
    outlineColor: Color = UniversalTheme.colorScheme.outlineButton.border,
    outlineColorDisabled: Color = UniversalTheme.colorScheme.outlineButton.borderDisabled,
): UniversalOutlinedButtonColors =
    UniversalOutlinedButtonColors(
        materialButtonColors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = contentColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = UniversalTheme.colorScheme.outlineButton.foregroundDisabled,
        ),
        outlineBorderColor = outlineColor,
        outlinedDisabledBorderColor = outlineColorDisabled,
    )

/**
 * Provides a default set of Universal-styled colors for a text button.
 */
@Composable
fun bitwardenTextButtonColors(
    contentColor: Color = UniversalTheme.colorScheme.outlineButton.foreground,
): ButtonColors = ButtonColors(
    containerColor = Color.Transparent,
    contentColor = contentColor,
    disabledContainerColor = Color.Transparent,
    disabledContentColor = UniversalTheme.colorScheme.outlineButton.foregroundDisabled,
)
