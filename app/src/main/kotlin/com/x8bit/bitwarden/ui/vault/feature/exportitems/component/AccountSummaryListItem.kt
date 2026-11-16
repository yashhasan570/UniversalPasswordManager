package com.x8bit.universalpasswordmanager.ui.vault.feature.exportitems.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.cardStyle
import .ui.platform.base.util.hexToColor
import .ui.platform.base.util.toSafeOverlayColor
import .ui.platform.base.util.toUnscaledTextUnit
import .ui.platform.components.model.CardStyle
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.platform.theme.type.universalpasswordmanagerFontFamily
import com.x8bit.universalpasswordmanager.ui.vault.feature.exportitems.model.AccountSelectionListItem

/**
 * A composable that displays an account summary list item.
 *
 * @param item The account selection list item to display.
 * @param cardStyle The card style to apply to the list item.
 * @param modifier The modifier to apply to the list item.
 * @param clickable Whether the list item should be clickable.
 */
@Suppress("LongMethod")
@Composable
fun AccountSummaryListItem(
    item: AccountSelectionListItem,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
    clickable: Boolean,
    onClick: (userId: String) -> Unit = {},
) {
    Row(
        modifier = modifier
            .testTag("AccountSummaryListItem")
            .defaultMinSize(minHeight = 60.dp)
            .clickable(
                onClick = { onClick(item.userId) },
                enabled = clickable,
            )
            .cardStyle(
                cardStyle = cardStyle,
                paddingStart = 16.dp,
                paddingEnd = 4.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(24.dp),
        ) {
            Icon(
                painter = rememberVectorPainter(
                    id = Universal Password ManagerDrawable.ic_account_initials_container,
                ),
                contentDescription = null,
                tint = item.avatarColorHex.hexToColor(),
                modifier = Modifier.size(24.dp),
            )
            Text(
                text = item.initials,
                style = TextStyle(
                    fontSize = 11.dp.toUnscaledTextUnit(),
                    lineHeight = 13.dp.toUnscaledTextUnit(),
                    fontFamily = universalpasswordmanagerFontFamily,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = item.avatarColorHex.hexToColor().toSafeOverlayColor(),
            )
        }

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = item.email,
                style = Universal Password ManagerTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.testTag("AccountEmailLabel"),
            )

            if (item.isItemRestricted) {
                Text(
                    text = stringResource(
                        Universal Password ManagerString.import_restricted_unable_to_import_credit_cards,
                    ),
                    style = Universal Password ManagerTheme.typography.bodyMedium,
                    color = Universal Password ManagerTheme.colorScheme.text.secondary,
                    modifier = Modifier.testTag("AccountRestrictedLabel"),
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))
    }
}
