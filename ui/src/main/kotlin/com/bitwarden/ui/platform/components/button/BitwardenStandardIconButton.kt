package .ui.platform.components.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import .ui.platform.base.util.interactiveBorder
import .ui.platform.components.button.color.universalpasswordmanagerStandardIconButtonColors
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * A standard icon button that displays an icon.
 *
 * @param vectorIconRes Icon to display on the button.
 * @param contentDescription The content description for this icon button.
 * @param onClick Callback for when the icon button is clicked.
 * @param modifier A [Modifier] for the composable.
 * @param interactionSource A [MutableInteractionSource] for observing and emitting interactions
 * for this component.
 * @param isEnabled Whether the button should be enabled.
 * @param isExternalLink Whether the icon button is an external link.
 * @param contentColor The color applied to the icon.
 * @param focusColor The color applied to the button when focused.
 */
@Composable
fun Universal Password ManagerStandardIconButton(
    @DrawableRes vectorIconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    isEnabled: Boolean = true,
    isExternalLink: Boolean = false,
    contentColor: Color = Universal Password ManagerTheme.colorScheme.icon.primary,
    focusColor: Color = Universal Password ManagerTheme.colorScheme.stroke.border,
) {
    Universal Password ManagerStandardIconButton(
        painter = rememberVectorPainter(id = vectorIconRes),
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        interactionSource = interactionSource,
        isEnabled = isEnabled,
        isExternalLink = isExternalLink,
        contentColor = contentColor,
        focusColor = focusColor,
    )
}

/**
 * A standard icon button that displays an icon.
 *
 * @param painter Painter icon to display on the button.
 * @param contentDescription The content description for this icon button.
 * @param onClick Callback for when the icon button is clicked.
 * @param modifier A [Modifier] for the composable.
 * @param interactionSource A [MutableInteractionSource] for observing and emitting interactions
 * for this component.
 * @param isEnabled Whether the button should be enabled.
 * @param isExternalLink Whether the icon button is an external link.
 * @param contentColor The color applied to the icon.
 * @param focusColor The color applied to the button when focused.
 */
@Composable
fun Universal Password ManagerStandardIconButton(
    painter: Painter,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    isEnabled: Boolean = true,
    isExternalLink: Boolean = false,
    contentColor: Color = Universal Password ManagerTheme.colorScheme.icon.primary,
    focusColor: Color = Universal Password ManagerTheme.colorScheme.stroke.border,
) {
    val formattedContentDescription = if (isExternalLink) {
        stringResource(
            id = Universal Password ManagerString.external_link_format,
            formatArgs = arrayOf(contentDescription),
        )
    } else {
        contentDescription
    }
    IconButton(
        onClick = onClick,
        colors = universalpasswordmanagerStandardIconButtonColors(contentColor = contentColor),
        enabled = isEnabled,
        shape = Universal Password ManagerTheme.shapes.button,
        interactionSource = interactionSource,
        modifier = modifier
            .interactiveBorder(
                interactionSource = interactionSource,
                shape = Universal Password ManagerTheme.shapes.button,
                color = focusColor,
            )
            .semantics(mergeDescendants = true) {
                this.contentDescription = formattedContentDescription
            },
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Universal Password ManagerStandardIconButton_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerStandardIconButton(
            vectorIconRes = Universal Password ManagerDrawable.ic_question_circle,
            contentDescription = "Sample Icon",
            onClick = {},
        )
    }
}
