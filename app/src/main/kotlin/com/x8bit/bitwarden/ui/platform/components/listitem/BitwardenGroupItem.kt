package com.x8bit.universalpasswordmanager.ui.platform.components.listitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.cardStyle
import .ui.platform.components.icon.Universal Password ManagerIcon
import .ui.platform.components.icon.model.IconData
import .ui.platform.components.model.CardStyle
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * A reusable composable function that displays a group item.
 * The list item consists of a start icon, label, sub-label, supporting label, and an end icon.
 *
 * @param label The main text label to be displayed in the group item.
 * @param supportingLabel The secondary supporting text label to be displayed beside the label.
 * @param startIcon The [IconData] object used to draw the icon at the start of the group item.
 * @param onClick A lambda function that is invoked when the group is clicked.
 * @param cardStyle Indicates the type of card style to be applied.
 * @param modifier The [Modifier] to be applied to the [Row] composable that holds the list item.
 * @param subLabel The secondary text label to be displayed in the group item.
 * @param endIcon The [IconData] object used to draw the icon at the end of the group item.
 */
@Composable
fun Universal Password ManagerGroupItem(
    label: String,
    supportingLabel: String,
    startIcon: IconData.Local,
    onClick: () -> Unit,
    cardStyle: CardStyle?,
    modifier: Modifier = Modifier,
    subLabel: String? = null,
    endIcon: IconData.Local? = null,
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 60.dp)
            .cardStyle(
                cardStyle = cardStyle,
                onClick = onClick,
                role = Role.Button,
                paddingHorizontal = 16.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Universal Password ManagerIcon(
            iconData = startIcon,
            tint = Universal Password ManagerTheme.colorScheme.icon.primary,
            modifier = Modifier
                .defaultMinSize(minHeight = 36.dp)
                .size(size = 24.dp),
        )

        Column(modifier = Modifier.weight(weight = 1f)) {
            Text(
                text = label,
                style = Universal Password ManagerTheme.typography.bodyLarge,
                color = Universal Password ManagerTheme.colorScheme.text.primary,
                modifier = Modifier.fillMaxWidth(),
            )
            subLabel?.let {
                Spacer(modifier = Modifier.height(height = 2.dp))
                Text(
                    text = it,
                    style = Universal Password ManagerTheme.typography.bodyMedium,
                    color = Universal Password ManagerTheme.colorScheme.text.secondary,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        Text(
            text = supportingLabel,
            style = Universal Password ManagerTheme.typography.labelSmall,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
        )
        endIcon?.let {
            Universal Password ManagerIcon(
                iconData = it,
                tint = Universal Password ManagerTheme.colorScheme.icon.primary,
                modifier = Modifier
                    .defaultMinSize(minHeight = 36.dp)
                    .size(size = 24.dp),
            )
        }
    }
}

@Preview
@Composable
private fun Universal Password ManagerGroupItem_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerGroupItem(
            label = "Sample Label",
            supportingLabel = "5",
            startIcon = IconData.Local(
                iconRes = Universal Password ManagerDrawable.ic_file_text,
                contentDescription = null,
                testTag = "Test Tag 1",
            ),
            endIcon = IconData.Local(
                iconRes = Universal Password ManagerDrawable.ic_locked,
                contentDescription = null,
                testTag = "Test Tag 2",
            ),
            onClick = {},
            subLabel = "Sample Subtext",
            cardStyle = CardStyle.Full,
        )
    }
}
