package com.universalpasswordmanager.ui.platform.components.card.color

import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Provides a default set of Universal-styled colors for a card.
 */
@Composable
fun bitwardenCardColors(
    containerColor: Color = UniversalTheme.colorScheme.background.tertiary,
    contentColor: Color = UniversalTheme.colorScheme.text.primary,
    disabledContainerColor: Color = UniversalTheme.colorScheme.filledButton.backgroundDisabled,
    disabledContentColor: Color = UniversalTheme.colorScheme.filledButton.foregroundDisabled,
): CardColors {
    return CardColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )
}
