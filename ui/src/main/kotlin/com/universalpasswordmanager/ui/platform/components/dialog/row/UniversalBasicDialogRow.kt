package com.universalpasswordmanager.ui.platform.components.dialog.row

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.universalpasswordmanager.ui.platform.base.util.interactiveBorder
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalSelectionDialog
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * A simple clickable row for use within a [UniversalSelectionDialog] as an alternative to a
 * [UniversalSelectionRow].
 *
 * @param text The text to display in the row.
 * @param onClick A callback to be invoked when the row is clicked.
 * @param modifier A [Modifier] for the composable.
 * @param interactionSource A [MutableInteractionSource] for observing and emitting interactions
 * for this component.
 */
@Composable
fun UniversalBasicDialogRow(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Text(
        text = text,
        style = UniversalTheme.typography.bodyLarge,
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = UniversalTheme.colorScheme.background.pressed),
                onClick = onClick,
                role = Role.Button,
            )
            .interactiveBorder(
                interactionSource = interactionSource,
                shape = UniversalTheme.shapes.row,
            )
            .padding(
                vertical = 16.dp,
                horizontal = 24.dp,
            )
            .fillMaxWidth()
            .testTag("AlertSelectionOption"),
    )
}
