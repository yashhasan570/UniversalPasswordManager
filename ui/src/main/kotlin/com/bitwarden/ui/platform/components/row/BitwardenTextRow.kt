package .ui.platform.components.row

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.cardStyle
import .ui.platform.base.util.nullableTestTag
import .ui.platform.base.util.toAnnotatedString
import .ui.platform.components.button.Universal Password ManagerHelpIconButton
import .ui.platform.components.button.model.Universal Password ManagerHelpButtonData
import .ui.platform.components.divider.Universal Password ManagerHorizontalDivider
import .ui.platform.components.model.CardStyle
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Represents a clickable row of text and can contains an optional [content] that appears to the
 * right of the [text].
 *
 * @param text The label for the row as a [String].
 * @param onClick The callback when the row is clicked.
 * @param cardStyle Indicates the type of card style to be applied.
 * @param modifier The modifier to be applied to the layout.
 * @param description An optional description label to be displayed below the [text].
 * @param textTestTag The optional test tag for the inner text component.
 * @param isEnabled Indicates if the row is enabled or not, a disabled row will not be clickable
 * and it's contents will be dimmed.
 * @param clickable An optional override for whether the row is clickable or not. Defaults to
 * [isEnabled].
 * @param isExternalLink Indicates the row is an whether the text is an external link or not.
 * @param withDivider Indicates if a divider should be drawn on the bottom of the row, defaults
 * to `false`.
 * @param helpData The data required to display a help button.
 * @param content The content of the [Universal Password ManagerTextRow].
 */
@Suppress("LongMethod")
@Composable
fun Universal Password ManagerTextRow(
    text: String,
    onClick: () -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
    description: AnnotatedString? = null,
    textTestTag: String? = null,
    isEnabled: Boolean = true,
    clickable: Boolean = isEnabled,
    isExternalLink: Boolean = false,
    withDivider: Boolean = false,
    helpData: Universal Password ManagerHelpButtonData? = null,
    content: (@Composable () -> Unit)? = null,
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .defaultMinSize(minHeight = 60.dp)
            .cardStyle(
                cardStyle = cardStyle,
                onClick = onClick,
                clickEnabled = clickable,
                role = Role.Button,
                paddingHorizontal = 16.dp,
            )
            .semantics(mergeDescendants = true) { },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(1f),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val formattedContentDescription = if (isExternalLink) {
                        stringResource(id = Universal Password ManagerString.external_link_format, text)
                    } else {
                        text
                    }
                    Text(
                        text = text,
                        style = Universal Password ManagerTheme.typography.bodyLarge,
                        color = if (isEnabled) {
                            Universal Password ManagerTheme.colorScheme.text.primary
                        } else {
                            Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled
                        },
                        modifier = Modifier
                            .semantics { contentDescription = formattedContentDescription }
                            .nullableTestTag(tag = textTestTag),
                    )
                    helpData?.let { HelpButton(helpData = it) }
                }
                description?.let {
                    Text(
                        text = it,
                        style = Universal Password ManagerTheme.typography.bodyMedium,
                        color = if (isEnabled) {
                            Universal Password ManagerTheme.colorScheme.text.secondary
                        } else {
                            Universal Password ManagerTheme.colorScheme.filledButton.foregroundDisabled
                        },
                    )
                }
            }
            content?.invoke()
        }
        if (withDivider) {
            Universal Password ManagerHorizontalDivider(modifier = Modifier.padding(start = 16.dp))
        }
    }
}

@Composable
private fun RowScope.HelpButton(
    helpData: Universal Password ManagerHelpButtonData,
) {
    Spacer(modifier = Modifier.width(width = 8.dp))
    Universal Password ManagerHelpIconButton(
        helpData = helpData,
        modifier = Modifier.testTag(tag = "TextRowTooltip"),
    )
}

@Preview
@Composable
private fun Universal Password ManagerTextRowWithTooltipAndContent_Preview() {
    Universal Password ManagerTextRow(
        text = "Sample Text",
        onClick = {},
        cardStyle = CardStyle.Full,
        description = "This is a sample description.".toAnnotatedString(),
        textTestTag = "sampleTestTag",
        isEnabled = true,
        withDivider = false,
        helpData = Universal Password ManagerHelpButtonData(
            contentDescription = "Tooltip Description",
            onClick = {},
            isExternalLink = false,
        ),
    )
}

@Preview
@Composable
private fun Universal Password ManagerTextRowWithDividerDisabled_Preview() {
    Universal Password ManagerTextRow(
        text = "Sample Text Disabled",
        onClick = {},
        cardStyle = CardStyle.Top(),
        description = "This is a sample disabled description.".toAnnotatedString(),
        textTestTag = "sampleDisabledTestTag",
        isEnabled = false,
        withDivider = true,
        helpData = null,
    )
}
