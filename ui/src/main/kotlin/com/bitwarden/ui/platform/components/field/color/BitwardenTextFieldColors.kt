package .ui.platform.components.field.color

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Provides a default set of Universal Password Manager-styled colors for a read-only text field button.
 */
@Composable
fun universalpasswordmanagerTextFieldButtonColors(): TextFieldColors = universalpasswordmanagerTextFieldColors(
    disabledTextColor = Universal Password ManagerTheme.colorScheme.text.primary,
    disabledLeadingIconColor = Universal Password ManagerTheme.colorScheme.icon.primary,
    disabledTrailingIconColor = Universal Password ManagerTheme.colorScheme.icon.primary,
    disabledLabelColor = Universal Password ManagerTheme.colorScheme.text.secondary,
    disabledPlaceholderColor = Universal Password ManagerTheme.colorScheme.text.secondary,
    disabledSupportingTextColor = Universal Password ManagerTheme.colorScheme.text.secondary,
)

/**
 * Provides a default set of Universal Password Manager-styled colors for text fields.
 */
@Composable
fun universalpasswordmanagerTextFieldColors(
    textColor: Color = Universal Password ManagerTheme.colorScheme.text.primary,
    disabledTextColor: Color = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
    disabledLeadingIconColor: Color = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
    disabledTrailingIconColor: Color = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
    disabledLabelColor: Color = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
    disabledPlaceholderColor: Color = Universal Password ManagerTheme.colorScheme.text.secondary,
    disabledSupportingTextColor: Color = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
    focusedIndicatorColor: Color = Color.Transparent,
    unfocusedIndicatorColor: Color = Color.Transparent,
    disabledIndicatorColor: Color = Color.Transparent,
): TextFieldColors = TextFieldColors(
    focusedTextColor = textColor,
    unfocusedTextColor = textColor,
    disabledTextColor = disabledTextColor,
    errorTextColor = Universal Password ManagerTheme.colorScheme.text.primary,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    errorContainerColor = Color.Transparent,
    cursorColor = Universal Password ManagerTheme.colorScheme.text.interaction,
    errorCursorColor = Universal Password ManagerTheme.colorScheme.text.interaction,
    textSelectionColors = TextSelectionColors(
        handleColor = Universal Password ManagerTheme.colorScheme.stroke.border,
        backgroundColor = Universal Password ManagerTheme.colorScheme.stroke.border.copy(alpha = 0.4f),
    ),
    focusedIndicatorColor = focusedIndicatorColor,
    unfocusedIndicatorColor = unfocusedIndicatorColor,
    disabledIndicatorColor = disabledIndicatorColor,
    errorIndicatorColor = Universal Password ManagerTheme.colorScheme.status.error,
    focusedLeadingIconColor = Universal Password ManagerTheme.colorScheme.icon.primary,
    unfocusedLeadingIconColor = Universal Password ManagerTheme.colorScheme.icon.primary,
    disabledLeadingIconColor = disabledLeadingIconColor,
    errorLeadingIconColor = Universal Password ManagerTheme.colorScheme.icon.primary,
    focusedTrailingIconColor = Universal Password ManagerTheme.colorScheme.icon.primary,
    unfocusedTrailingIconColor = Universal Password ManagerTheme.colorScheme.icon.primary,
    disabledTrailingIconColor = disabledTrailingIconColor,
    errorTrailingIconColor = Universal Password ManagerTheme.colorScheme.status.error,
    focusedLabelColor = Universal Password ManagerTheme.colorScheme.text.secondary,
    unfocusedLabelColor = Universal Password ManagerTheme.colorScheme.text.secondary,
    disabledLabelColor = disabledLabelColor,
    errorLabelColor = Universal Password ManagerTheme.colorScheme.status.error,
    focusedPlaceholderColor = Universal Password ManagerTheme.colorScheme.text.secondary,
    unfocusedPlaceholderColor = Universal Password ManagerTheme.colorScheme.text.secondary,
    disabledPlaceholderColor = disabledPlaceholderColor,
    errorPlaceholderColor = Universal Password ManagerTheme.colorScheme.text.secondary,
    focusedSupportingTextColor = Universal Password ManagerTheme.colorScheme.text.secondary,
    unfocusedSupportingTextColor = Universal Password ManagerTheme.colorScheme.text.secondary,
    disabledSupportingTextColor = disabledSupportingTextColor,
    errorSupportingTextColor = Universal Password ManagerTheme.colorScheme.text.secondary,
    focusedPrefixColor = Universal Password ManagerTheme.colorScheme.text.secondary,
    unfocusedPrefixColor = Universal Password ManagerTheme.colorScheme.text.secondary,
    disabledPrefixColor = Universal Password ManagerTheme.colorScheme.outlineButton.foregroundDisabled,
    errorPrefixColor = Universal Password ManagerTheme.colorScheme.status.error,
    focusedSuffixColor = Universal Password ManagerTheme.colorScheme.text.secondary,
    unfocusedSuffixColor = Universal Password ManagerTheme.colorScheme.text.secondary,
    disabledSuffixColor = Universal Password ManagerTheme.colorScheme.outlineButton.foregroundDisabled,
    errorSuffixColor = Universal Password ManagerTheme.colorScheme.status.error,
)
