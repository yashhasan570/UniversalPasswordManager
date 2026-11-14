package com.universalpasswordmanager.ui.platform.components.field.color

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Provides a default set of Universal-styled colors for a read-only text field button.
 */
@Composable
fun bitwardenTextFieldButtonColors(): TextFieldColors = bitwardenTextFieldColors(
    disabledTextColor = UniversalTheme.colorScheme.text.primary,
    disabledLeadingIconColor = UniversalTheme.colorScheme.icon.primary,
    disabledTrailingIconColor = UniversalTheme.colorScheme.icon.primary,
    disabledLabelColor = UniversalTheme.colorScheme.text.secondary,
    disabledPlaceholderColor = UniversalTheme.colorScheme.text.secondary,
    disabledSupportingTextColor = UniversalTheme.colorScheme.text.secondary,
)

/**
 * Provides a default set of Universal-styled colors for text fields.
 */
@Composable
fun bitwardenTextFieldColors(
    textColor: Color = UniversalTheme.colorScheme.text.primary,
    disabledTextColor: Color = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
    disabledLeadingIconColor: Color = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
    disabledTrailingIconColor: Color = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
    disabledLabelColor: Color = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
    disabledPlaceholderColor: Color = UniversalTheme.colorScheme.text.secondary,
    disabledSupportingTextColor: Color = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
    focusedIndicatorColor: Color = Color.Transparent,
    unfocusedIndicatorColor: Color = Color.Transparent,
    disabledIndicatorColor: Color = Color.Transparent,
): TextFieldColors = TextFieldColors(
    focusedTextColor = textColor,
    unfocusedTextColor = textColor,
    disabledTextColor = disabledTextColor,
    errorTextColor = UniversalTheme.colorScheme.text.primary,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    errorContainerColor = Color.Transparent,
    cursorColor = UniversalTheme.colorScheme.text.interaction,
    errorCursorColor = UniversalTheme.colorScheme.text.interaction,
    textSelectionColors = TextSelectionColors(
        handleColor = UniversalTheme.colorScheme.stroke.border,
        backgroundColor = UniversalTheme.colorScheme.stroke.border.copy(alpha = 0.4f),
    ),
    focusedIndicatorColor = focusedIndicatorColor,
    unfocusedIndicatorColor = unfocusedIndicatorColor,
    disabledIndicatorColor = disabledIndicatorColor,
    errorIndicatorColor = UniversalTheme.colorScheme.status.error,
    focusedLeadingIconColor = UniversalTheme.colorScheme.icon.primary,
    unfocusedLeadingIconColor = UniversalTheme.colorScheme.icon.primary,
    disabledLeadingIconColor = disabledLeadingIconColor,
    errorLeadingIconColor = UniversalTheme.colorScheme.icon.primary,
    focusedTrailingIconColor = UniversalTheme.colorScheme.icon.primary,
    unfocusedTrailingIconColor = UniversalTheme.colorScheme.icon.primary,
    disabledTrailingIconColor = disabledTrailingIconColor,
    errorTrailingIconColor = UniversalTheme.colorScheme.status.error,
    focusedLabelColor = UniversalTheme.colorScheme.text.secondary,
    unfocusedLabelColor = UniversalTheme.colorScheme.text.secondary,
    disabledLabelColor = disabledLabelColor,
    errorLabelColor = UniversalTheme.colorScheme.status.error,
    focusedPlaceholderColor = UniversalTheme.colorScheme.text.secondary,
    unfocusedPlaceholderColor = UniversalTheme.colorScheme.text.secondary,
    disabledPlaceholderColor = disabledPlaceholderColor,
    errorPlaceholderColor = UniversalTheme.colorScheme.text.secondary,
    focusedSupportingTextColor = UniversalTheme.colorScheme.text.secondary,
    unfocusedSupportingTextColor = UniversalTheme.colorScheme.text.secondary,
    disabledSupportingTextColor = disabledSupportingTextColor,
    errorSupportingTextColor = UniversalTheme.colorScheme.text.secondary,
    focusedPrefixColor = UniversalTheme.colorScheme.text.secondary,
    unfocusedPrefixColor = UniversalTheme.colorScheme.text.secondary,
    disabledPrefixColor = UniversalTheme.colorScheme.outlineButton.foregroundDisabled,
    errorPrefixColor = UniversalTheme.colorScheme.status.error,
    focusedSuffixColor = UniversalTheme.colorScheme.text.secondary,
    unfocusedSuffixColor = UniversalTheme.colorScheme.text.secondary,
    disabledSuffixColor = UniversalTheme.colorScheme.outlineButton.foregroundDisabled,
    errorSuffixColor = UniversalTheme.colorScheme.status.error,
)
