package com.universalpasswordmanager.ui.platform.components.row

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.universalpasswordmanager.ui.platform.base.util.cardStyle
import com.universalpasswordmanager.ui.platform.base.util.mirrorIfRtl
import com.universalpasswordmanager.ui.platform.components.badge.NotificationBadge
import com.universalpasswordmanager.ui.platform.components.icon.UniversalIcon
import com.universalpasswordmanager.ui.platform.components.icon.model.IconData
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Reusable row with push icon built in.
 *
 * @param text The displayable text.
 * @param onClick The callback when the row is clicked.
 * @param cardStyle The [CardStyle] to be applied to this row.
 * @param modifier The modifier for this composable.
 * @param description The optional displayable description text.
 * @param leadingIcon An optional leading icon.
 * @param notificationCount The optional notification count to be displayed.
 */
@Composable
fun UniversalPushRow(
    text: String,
    onClick: () -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
    description: String? = null,
    leadingIcon: IconData? = null,
    notificationCount: Int = 0,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 60.dp)
            .cardStyle(
                cardStyle = cardStyle,
                onClick = onClick,
                role = Role.Button,
                paddingStart = leadingIcon?.let { 12.dp } ?: 16.dp,
                paddingEnd = 20.dp,
                paddingTop = 6.dp,
                paddingBottom = 6.dp,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .defaultMinSize(minHeight = 48.dp)
                .weight(weight = 1f),
        ) {
            leadingIcon?.let {
                UniversalIcon(
                    iconData = it,
                    tint = UniversalTheme.colorScheme.icon.primary,
                    modifier = Modifier.size(size = 24.dp),
                )
                Spacer(modifier = Modifier.width(width = 12.dp))
            }
            Column {
                Text(
                    text = text,
                    style = UniversalTheme.typography.bodyLarge,
                    color = UniversalTheme.colorScheme.text.primary,
                    modifier = Modifier.fillMaxWidth(),
                )
                description?.let {
                    Text(
                        text = it,
                        style = UniversalTheme.typography.bodyMedium,
                        color = UniversalTheme.colorScheme.text.secondary,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
        TrailingContent(notificationCount = notificationCount)
    }
}

@Composable
private fun TrailingContent(
    notificationCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.defaultMinSize(minHeight = 48.dp),
    ) {
        val notificationBadgeVisible = notificationCount > 0
        NotificationBadge(
            notificationCount = notificationCount,
            isVisible = notificationBadgeVisible,
        )
        if (notificationBadgeVisible) {
            Spacer(modifier = Modifier.width(12.dp))
        }
        Icon(
            painter = rememberVectorPainter(id = UniversalDrawable.ic_chevron_right),
            contentDescription = null,
            tint = UniversalTheme.colorScheme.icon.primary,
            modifier = Modifier
                .mirrorIfRtl()
                .size(size = 16.dp),
        )
    }
}

@Preview
@Composable
private fun UniversalPushRow_preview() {
    UniversalTheme {
        Column {
            UniversalPushRow(
                text = "Plain Row",
                onClick = { },
                cardStyle = CardStyle.Top(),
            )
            UniversalPushRow(
                text = "Icon Row",
                onClick = { },
                cardStyle = CardStyle.Middle(),
                leadingIcon = IconData.Local(iconRes = UniversalDrawable.ic_vault),
            )
            UniversalPushRow(
                text = "Notification Row",
                onClick = { },
                cardStyle = CardStyle.Bottom,
                notificationCount = 3,
            )
        }
    }
}
