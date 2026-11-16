package .ui.platform.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import .core.util.persistentListOfNotNull
import .ui.platform.base.util.cardStyle
import .ui.platform.base.util.nullableTestTag
import .ui.platform.components.button.model.Universal Password ManagerHelpButtonData
import .ui.platform.components.divider.Universal Password ManagerHorizontalDivider
import .ui.platform.components.field.color.universalpasswordmanagerTextFieldButtonColors
import .ui.platform.components.field.color.universalpasswordmanagerTextFieldColors
import .ui.platform.components.model.CardStyle
import .ui.platform.components.row.Universal Password ManagerRowOfActions
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * A button which uses a read-only text field for layout and style purposes.
 */
@Composable
fun Universal Password ManagerTextSelectionButton(
    label: String,
    selectedOption: String?,
    onClick: () -> Unit,
    cardStyle: CardStyle?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    supportingText: String? = null,
    helpData: Universal Password ManagerHelpButtonData? = null,
    insets: PaddingValues = PaddingValues(),
    textFieldTestTag: String? = null,
    semanticRole: Role = Role.Button,
    actionsPadding: PaddingValues = PaddingValues(end = 4.dp),
    actions: @Composable RowScope.() -> Unit = {},
) {
    Universal Password ManagerTextSelectionButton(
        label = label,
        selectedOption = selectedOption,
        onClick = onClick,
        cardStyle = cardStyle,
        modifier = modifier,
        enabled = enabled,
        helpData = helpData,
        insets = insets,
        textFieldTestTag = textFieldTestTag,
        semanticRole = semanticRole,
        actionsPadding = actionsPadding,
        actions = actions,
        supportingContent = supportingText?.let {
            {
                Text(
                    text = it,
                    style = Universal Password ManagerTheme.typography.bodySmall,
                    color = Universal Password ManagerTheme.colorScheme.text.secondary,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
    )
}

/**
 *
 * A button which uses a read-only text field for layout and style purposes.
 */
@Suppress("LongMethod")
@Composable
fun Universal Password ManagerTextSelectionButton(
    label: String,
    selectedOption: String?,
    onClick: () -> Unit,
    cardStyle: CardStyle?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showChevron: Boolean = true,
    helpData: Universal Password ManagerHelpButtonData? = null,
    insets: PaddingValues = PaddingValues(),
    textFieldTestTag: String? = null,
    semanticRole: Role = Role.Button,
    actionsPadding: PaddingValues = PaddingValues(end = 4.dp),
    supportingContent: @Composable (ColumnScope.() -> Unit)?,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Column(
        modifier = modifier
            .defaultMinSize(minHeight = 60.dp)
            .semantics {
                role = semanticRole
                contentDescription = "${selectedOption?.let { "$it. " }.orEmpty()}$label"
                customActions = persistentListOfNotNull(
                    helpData?.let {
                        CustomAccessibilityAction(
                            label = it.contentDescription,
                            action = {
                                it.onClick()
                                true
                            },
                        )
                    },
                )
            }
            .cardStyle(
                cardStyle = cardStyle,
                paddingTop = 6.dp,
                paddingBottom = 0.dp,
                onClick = onClick,
                clickEnabled = enabled,
            )
            .padding(paddingValues = insets),
    ) {
        TextField(
            textStyle = Universal Password ManagerTheme.typography.bodyLarge,
            readOnly = true,
            enabled = false,
            label = {
                Row {
                    Text(
                        text = label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    helpData?.let {
                        Spacer(modifier = Modifier.width(8.dp))
                        Universal Password ManagerHelpIconButton(
                            helpData = it,
                            modifier = Modifier.size(size = 16.dp),
                        )
                    }
                }
            },
            trailingIcon = {
                Universal Password ManagerRowOfActions(
                    modifier = Modifier.padding(paddingValues = actionsPadding),
                    actions = {
                        if (showChevron) {
                            Icon(
                                painter = rememberVectorPainter(
                                    id = Universal Password ManagerDrawable.ic_chevron_down,
                                ),
                                contentDescription = null,
                                modifier = Modifier.minimumInteractiveComponentSize(),
                            )
                        }
                        actions()
                    },
                )
            },
            value = selectedOption.orEmpty(),
            onValueChange = {},
            colors = if (enabled) {
                universalpasswordmanagerTextFieldButtonColors()
            } else {
                universalpasswordmanagerTextFieldColors()
            },
            modifier = Modifier
                .semantics { this.hideFromAccessibility() }
                .nullableTestTag(tag = textFieldTestTag)
                .fillMaxWidth(),
        )
        supportingContent
            ?.let { content ->
                Spacer(modifier = Modifier.height(height = 6.dp))
                Universal Password ManagerHorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 48.dp)
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    content = content,
                )
            }
            ?: Spacer(modifier = Modifier.height(height = cardStyle?.let { 6.dp } ?: 0.dp))
    }
}

@Preview
@Composable
private fun Universal Password ManagerTextSelectionButton_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerTextSelectionButton(
            label = "Folder",
            selectedOption = "No Folder",
            onClick = {},
            cardStyle = CardStyle.Full,
        )
    }
}
