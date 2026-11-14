package com.universalpasswordmanager.ui.platform.components.coachmark

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.universalpasswordmanager.ui.platform.components.text.UniversalClickableText
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

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
    UniversalClickableText(
        label = actionLabel,
        onClick = onActionClick,
        style = UniversalTheme.typography.labelLarge,
        modifier = modifier,
    )
}
