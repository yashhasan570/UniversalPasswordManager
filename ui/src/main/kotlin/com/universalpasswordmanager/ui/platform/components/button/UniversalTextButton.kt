package com.universalpasswordmanager.ui.platform.components.button

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.universalpasswordmanager.ui.platform.base.util.nullableTestTag
import com.universalpasswordmanager.ui.platform.components.button.color.bitwardenTextButtonColors
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalButtonData
import com.universalpasswordmanager.ui.platform.components.util.throttledClick
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.platform.theme.animation.focusAnimationSpec

/**
 * Represents a Universal-styled [TextButton].
 *
 * @param buttonData The data for the button.
 * @param modifier The [Modifier] to be applied to the button.
 * @param contentColor The color for the label text and icon.
 */
@Composable
fun UniversalTextButton(
    buttonData: UniversalButtonData,
    modifier: Modifier = Modifier,
    contentColor: Color = UniversalTheme.colorScheme.outlineButton.foreground,
) {
    UniversalTextButton(
        label = buttonData.label(),
        onClick = buttonData.onClick,
        icon = buttonData.icon,
        isExternalLink = buttonData.isExternalLink,
        isEnabled = buttonData.isEnabled,
        contentColor = contentColor,
        modifier = modifier.nullableTestTag(tag = buttonData.testTag),
    )
}

/**
 * Represents a Universal-styled [TextButton].
 *
 * @param label The label for the button.
 * @param onClick The callback when the button is clicked.
 * @param modifier The [Modifier] to be applied to the button.
 * @param interactionSource A [MutableInteractionSource] for observing and emitting interactions
 * for this component.
 * @param icon The icon for the button.
 * @param isEnabled Whether the button is enabled.
 * @param isExternalLink Indicates that this button launches an external link.
 * @param contentColor The color for the label text and icon.
 */
@Composable
fun UniversalTextButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    icon: Painter? = null,
    isEnabled: Boolean = true,
    isExternalLink: Boolean = false,
    contentColor: Color = UniversalTheme.colorScheme.outlineButton.foreground,
) {
    val formattedContentDescription = if (isExternalLink) {
        stringResource(
            id = UniversalString.external_link_format,
            formatArgs = arrayOf(label),
        )
    } else {
        label
    }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val colorAlpha by animateFloatAsState(
        targetValue = if (isFocused) 1f else 0f,
        animationSpec = focusAnimationSpec(),
    )
    TextButton(
        modifier = modifier.semantics(mergeDescendants = true) {
            contentDescription = formattedContentDescription
        },
        onClick = throttledClick(onClick = onClick),
        enabled = isEnabled,
        contentPadding = PaddingValues(
            top = 10.dp,
            bottom = 10.dp,
            start = 12.dp,
            end = if (icon == null) 12.dp else 16.dp,
        ),
        colors = bitwardenTextButtonColors(contentColor = contentColor),
        border = BorderStroke(width = 2.dp, color = contentColor.copy(alpha = colorAlpha)),
        interactionSource = interactionSource,
    ) {
        icon?.let {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = contentColor,
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = label,
            style = UniversalTheme.typography.labelLarge,
            modifier = Modifier.semantics { hideFromAccessibility() },
        )
    }
}

@Preview
@Composable
private fun UniversalTextButton_preview() {
    Column {
        UniversalTextButton(
            label = "Label",
            onClick = {},
            isEnabled = true,
        )
        UniversalTextButton(
            label = "Label",
            onClick = {},
            isEnabled = false,
        )
    }
}
