package com.universalpasswordmanager.ui.platform.components.button

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
import com.universalpasswordmanager.ui.platform.base.util.interactiveBorder
import com.universalpasswordmanager.ui.platform.components.button.color.universalpasswordmanagerStandardIconButtonColors
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

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
fun UniversalStandardIconButton(
    @DrawableRes vectorIconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    isEnabled: Boolean = true,
    isExternalLink: Boolean = false,
    contentColor: Color = UniversalTheme.colorScheme.icon.primary,
    focusColor: Color = UniversalTheme.colorScheme.stroke.border,
) {
    UniversalStandardIconButton(
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
fun UniversalStandardIconButton(
    painter: Painter,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    isEnabled: Boolean = true,
    isExternalLink: Boolean = false,
    contentColor: Color = UniversalTheme.colorScheme.icon.primary,
    focusColor: Color = UniversalTheme.colorScheme.stroke.border,
) {
    val formattedContentDescription = if (isExternalLink) {
        stringResource(
            id = UniversalString.external_link_format,
            formatArgs = arrayOf(contentDescription),
        )
    } else {
        contentDescription
    }
    IconButton(
        onClick = onClick,
        colors = universalpasswordmanagerStandardIconButtonColors(contentColor = contentColor),
        enabled = isEnabled,
        shape = UniversalTheme.shapes.button,
        interactionSource = interactionSource,
        modifier = modifier
            .interactiveBorder(
                interactionSource = interactionSource,
                shape = UniversalTheme.shapes.button,
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
private fun UniversalStandardIconButton_preview() {
    UniversalTheme {
        UniversalStandardIconButton(
            vectorIconRes = UniversalDrawable.ic_question_circle,
            contentDescription = "Sample Icon",
            onClick = {},
        )
    }
}
