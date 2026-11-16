package .ui.platform.components.button.color

import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import .ui.platform.components.button.model.Universal Password ManagerOutlinedButtonColors
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Provides a default set of Universal Password Manager-styled colors for a filled button.
 */
@Composable
fun universalpasswordmanagerFilledButtonColors(): ButtonColors = ButtonColors(
    containerColor = Universal Password ManagerTheme.colorScheme.filledButton.background,
    contentColor = Universal Password ManagerTheme.colorScheme.filledButton.foreground,
    disabledContainerColor = Universal Password ManagerTheme.colorScheme.filledButton.backgroundDisabled,
    disabledContentColor = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
)

/**
 * Provides a default set of Universal Password Manager-styled colors for a filled error button.
 */
@Composable
fun universalpasswordmanagerFilledErrorButtonColors() = ButtonColors(
    containerColor = Universal Password ManagerTheme.colorScheme.status.weak1,
    contentColor = Universal Password ManagerTheme.colorScheme.filledButton.foreground,
    disabledContainerColor = Universal Password ManagerTheme.colorScheme.filledButton.backgroundDisabled,
    disabledContentColor = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
)

/**
 * Provides a default set of Universal Password Manager-styled colors for an outlined button.
 */
@Composable
fun universalpasswordmanagerOutlinedButtonColors(
    contentColor: Color = Universal Password ManagerTheme.colorScheme.outlineButton.foreground,
    outlineColor: Color = Universal Password ManagerTheme.colorScheme.outlineButton.border,
    outlineColorDisabled: Color = Universal Password ManagerTheme.colorScheme.outlineButton.borderDisabled,
): Universal Password ManagerOutlinedButtonColors =
    Universal Password ManagerOutlinedButtonColors(
        materialButtonColors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = contentColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Universal Password ManagerTheme.colorScheme.outlineButton.foregroundDisabled,
        ),
        outlineBorderColor = outlineColor,
        outlinedDisabledBorderColor = outlineColorDisabled,
    )

/**
 * Provides a default set of Universal Password Manager-styled colors for a text button.
 */
@Composable
fun universalpasswordmanagerTextButtonColors(
    contentColor: Color = Universal Password ManagerTheme.colorScheme.outlineButton.foreground,
): ButtonColors = ButtonColors(
    containerColor = Color.Transparent,
    contentColor = contentColor,
    disabledContainerColor = Color.Transparent,
    disabledContentColor = Universal Password ManagerTheme.colorScheme.outlineButton.foregroundDisabled,
)
