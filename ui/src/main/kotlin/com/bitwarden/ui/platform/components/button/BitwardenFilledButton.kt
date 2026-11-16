package .ui.platform.components.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.cardStyle
import .ui.platform.base.util.nullableTestTag
import .ui.platform.components.button.color.universalpasswordmanagerFilledButtonColors
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.model.CardStyle
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.components.util.throttledClick
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Represents a Universal Password Manager-styled filled [Button].
 *
 * @param buttonData The data for the button.
 * @param modifier The [Modifier] to be applied to the button.
 * @param interactionSource A [MutableInteractionSource] for observing and emitting interactions
 * for this component.
 * @param colors The colors for the button.
 * @param cardStyle The optional card style to surround the button.
 * @param cardInsets The internal insets for the card, only applied when the [cardStyle] is not
 * `null`.
 */
@Composable
fun Universal Password ManagerFilledButton(
    buttonData: Universal Password ManagerButtonData,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: ButtonColors = universalpasswordmanagerFilledButtonColors(),
    cardStyle: CardStyle? = null,
    cardInsets: PaddingValues = PaddingValues(horizontal = 16.dp),
) {
    Universal Password ManagerFilledButton(
        label = buttonData.label(),
        onClick = buttonData.onClick,
        icon = buttonData.icon,
        isExternalLink = buttonData.isExternalLink,
        isEnabled = buttonData.isEnabled,
        cardStyle = cardStyle,
        colors = colors,
        cardInsets = cardInsets,
        interactionSource = interactionSource,
        modifier = modifier.nullableTestTag(tag = buttonData.testTag),
    )
}

/**
 * Represents a Universal Password Manager-styled filled [Button].
 *
 * @param label The label for the button.
 * @param onClick The callback when the button is clicked.
 * @param modifier The [Modifier] to be applied to the button.
 * @param interactionSource A [MutableInteractionSource] for observing and emitting interactions
 * for this component.
 * @param icon The icon for the button.
 * @param isEnabled Whether the button is enabled.
 * @param isExternalLink Indicates that this button launches an external link.
 * @param colors The colors for the button.
 * @param cardStyle The optional card style to surround the button.
 * @param cardInsets The internal insets for the card, only applied when the [cardStyle] is not
 * `null`.
 */
@Composable
fun Universal Password ManagerFilledButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    icon: Painter? = null,
    isEnabled: Boolean = true,
    isExternalLink: Boolean = false,
    colors: ButtonColors = universalpasswordmanagerFilledButtonColors(),
    cardStyle: CardStyle? = null,
    cardInsets: PaddingValues = PaddingValues(horizontal = 16.dp),
) {
    val formattedContentDescription = if (isExternalLink) {
        stringResource(
            id = Universal Password ManagerString.external_link_format,
            formatArgs = arrayOf(label),
        )
    } else {
        label
    }
    Universal Password ManagerBaseButton(
        modifier = modifier
            .semantics(mergeDescendants = true) {
                contentDescription = formattedContentDescription
            }
            .cardStyle(cardStyle = cardStyle, padding = cardInsets),
        onClick = throttledClick(onClick = onClick),
        enabled = isEnabled,
        contentPadding = PaddingValues(
            top = 10.dp,
            bottom = 10.dp,
            start = if (icon == null) 24.dp else 16.dp,
            end = 24.dp,
        ),
        shape = Universal Password ManagerTheme.shapes.button,
        colors = colors,
        focusColor = colors.contentColor,
        interactionSource = interactionSource,
    ) {
        icon?.let {
            Icon(
                painter = icon,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = label,
            style = Universal Password ManagerTheme.typography.labelLarge,
            modifier = Modifier.semantics { hideFromAccessibility() },
        )
    }
}

@Preview
@Composable
private fun Universal Password ManagerFilledButton_preview() {
    Column {
        Universal Password ManagerFilledButton(
            label = "Label",
            onClick = {},
            icon = null,
            isEnabled = true,
        )
        Universal Password ManagerFilledButton(
            label = "Label",
            onClick = {},
            icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_question_circle),
            isEnabled = true,
        )
        Universal Password ManagerFilledButton(
            label = "Label",
            onClick = {},
            icon = null,
            isEnabled = false,
        )
        Universal Password ManagerFilledButton(
            label = "Label",
            onClick = {},
            icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_question_circle),
            isEnabled = false,
        )
    }
}
