package .ui.platform.components.account

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import .ui.platform.base.util.interactiveBorder
import .ui.platform.components.button.color.universalpasswordmanagerStandardIconButtonColors
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.feature.settings.appearance.model.AppTheme
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * A placeholder item to be used to represent an account.
 *
 * @param onClick An action to be invoked when the icon is clicked.
 * @param modifier A [Modifier] for the composable.
 * @param interactionSource A [MutableInteractionSource] for observing and emitting interactions
 * for this component.
 */
@Composable
fun Universal Password ManagerPlaceholderAccountActionItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    IconButton(
        onClick = onClick,
        colors = universalpasswordmanagerStandardIconButtonColors(),
        shape = Universal Password ManagerTheme.shapes.button,
        interactionSource = interactionSource,
        modifier = modifier
            .interactiveBorder(
                interactionSource = interactionSource,
                shape = Universal Password ManagerTheme.shapes.button,
            )
            .semantics(mergeDescendants = true) { testTag = "CurrentActiveAccount" },
    ) {
        Icon(
            painter = rememberVectorPainter(
                id = Universal Password ManagerDrawable.ic_account_initials_container,
            ),
            contentDescription = null,
            tint = Universal Password ManagerTheme.colorScheme.background.tertiary,
        )
        Icon(
            painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_dots),
            contentDescription = stringResource(id = Universal Password ManagerString.account),
            tint = Universal Password ManagerTheme.colorScheme.text.interaction,
        )
    }
}

@Preview
@Composable
private fun Universal Password ManagerPlaceholderAccountActionItem_preview_light() {
    Universal Password ManagerTheme(theme = AppTheme.LIGHT) {
        Universal Password ManagerPlaceholderAccountActionItem(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun Universal Password ManagerPlaceholderAccountActionItem_preview_dark() {
    Universal Password ManagerTheme(theme = AppTheme.DARK) {
        Universal Password ManagerPlaceholderAccountActionItem(
            onClick = {},
        )
    }
}
