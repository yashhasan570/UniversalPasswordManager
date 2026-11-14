package com.universalpasswordmanager.ui.platform.components.account

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
import com.universalpasswordmanager.ui.platform.base.util.interactiveBorder
import com.universalpasswordmanager.ui.platform.components.button.color.universalpasswordmanagerStandardIconButtonColors
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.feature.settings.appearance.model.AppTheme
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * A placeholder item to be used to represent an account.
 *
 * @param onClick An action to be invoked when the icon is clicked.
 * @param modifier A [Modifier] for the composable.
 * @param interactionSource A [MutableInteractionSource] for observing and emitting interactions
 * for this component.
 */
@Composable
fun UniversalPlaceholderAccountActionItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    IconButton(
        onClick = onClick,
        colors = universalpasswordmanagerStandardIconButtonColors(),
        shape = UniversalTheme.shapes.button,
        interactionSource = interactionSource,
        modifier = modifier
            .interactiveBorder(
                interactionSource = interactionSource,
                shape = UniversalTheme.shapes.button,
            )
            .semantics(mergeDescendants = true) { testTag = "CurrentActiveAccount" },
    ) {
        Icon(
            painter = rememberVectorPainter(
                id = UniversalDrawable.ic_account_initials_container,
            ),
            contentDescription = null,
            tint = UniversalTheme.colorScheme.background.tertiary,
        )
        Icon(
            painter = rememberVectorPainter(id = UniversalDrawable.ic_dots),
            contentDescription = stringResource(id = UniversalString.account),
            tint = UniversalTheme.colorScheme.text.interaction,
        )
    }
}

@Preview
@Composable
private fun UniversalPlaceholderAccountActionItem_preview_light() {
    UniversalTheme(theme = AppTheme.LIGHT) {
        UniversalPlaceholderAccountActionItem(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun UniversalPlaceholderAccountActionItem_preview_dark() {
    UniversalTheme(theme = AppTheme.DARK) {
        UniversalPlaceholderAccountActionItem(
            onClick = {},
        )
    }
}
