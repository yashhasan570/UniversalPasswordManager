package com.x8bit.universalpasswordmanager.ui.vault.feature.item.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.text.Universal Password ManagerHyperTextLink
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.Text
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.VaultItemState
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.handlers.VaultCommonItemTypeHandlers

/**
 * Displays the common item history for the vault item screen.
 *
 * @param commonState The common state containing item history.
 * @param vaultCommonItemTypeHandlers Provides the handlers required for the item history.
 * @param loginPasswordRevisionDate The revision date for the login password (Login Cipher
 * specific).
 */
@Suppress("LongMethod")
fun LazyListScope.vaultItemHistory(
    commonState: VaultItemState.ViewState.Content.Common,
    vaultCommonItemTypeHandlers: VaultCommonItemTypeHandlers,
    loginPasswordRevisionDate: Text?,
) {
    item(key = "lastUpdated") {
        Spacer(modifier = Modifier.height(height = 16.dp))
        Text(
            text = commonState.lastUpdated(),
            style = Universal Password ManagerTheme.typography.bodySmall,
            color = Universal Password ManagerTheme.colorScheme.text.secondary,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 12.dp)
                .animateItem()
                .testTag(tag = "CipherItemLastUpdated"),
        )
    }
    item(key = "created") {
        Spacer(modifier = Modifier.height(height = 4.dp))
        Text(
            text = commonState.created(),
            style = Universal Password ManagerTheme.typography.bodySmall,
            color = Universal Password ManagerTheme.colorScheme.text.secondary,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 12.dp)
                .animateItem()
                .testTag(tag = "CipherItemCreated"),
        )
    }
    loginPasswordRevisionDate?.let { revisionDate ->
        item(key = "revisionDate") {
            Spacer(modifier = Modifier.height(height = 4.dp))
            Text(
                text = revisionDate(),
                style = Universal Password ManagerTheme.typography.bodySmall,
                color = Universal Password ManagerTheme.colorScheme.text.secondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .padding(horizontal = 12.dp)
                    .animateItem(),
            )
        }
    }
    commonState.passwordHistoryCount?.let { passwordHistoryCount ->
        item(key = "passwordHistoryCount") {
            Spacer(modifier = Modifier.height(height = 4.dp))
            Universal Password ManagerHyperTextLink(
                annotatedResId = Universal Password ManagerString.password_history_count,
                args = arrayOf(passwordHistoryCount.toString()),
                annotationKey = "passwordHistory",
                accessibilityString = stringResource(id = Universal Password ManagerString.password_history),
                onClick = vaultCommonItemTypeHandlers.onPasswordHistoryClick,
                style = Universal Password ManagerTheme.typography.labelMedium,
                modifier = Modifier
                    .wrapContentWidth()
                    .standardHorizontalMargin()
                    .padding(horizontal = 12.dp)
                    .animateItem(),
            )
        }
    }
}
