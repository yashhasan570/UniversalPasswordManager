package .ui.platform.components.card.color

import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Provides a default set of Universal Password Manager-styled colors for a card.
 */
@Composable
fun universalpasswordmanagerCardColors(
    containerColor: Color = Universal Password ManagerTheme.colorScheme.background.tertiary,
    contentColor: Color = Universal Password ManagerTheme.colorScheme.text.primary,
    disabledContainerColor: Color = Universal Password ManagerTheme.colorScheme.filledButton.backgroundDisabled,
    disabledContentColor: Color = Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled,
): CardColors {
    return CardColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )
}
