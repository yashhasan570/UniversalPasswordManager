package .ui.platform.components.coachmark

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import .ui.platform.components.text.Universal Password ManagerClickableText
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Clickable text used for the standard action UI for a Coach Mark which applies
 * correct text style by default.
 */
@Composable
fun CoachMarkActionText(
    actionLabel: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Universal Password ManagerClickableText(
        label = actionLabel,
        onClick = onActionClick,
        style = Universal Password ManagerTheme.typography.labelLarge,
        modifier = modifier,
    )
}
