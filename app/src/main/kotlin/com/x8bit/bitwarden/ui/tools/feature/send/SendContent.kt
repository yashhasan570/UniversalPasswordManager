package com.x8bit.universalpasswordmanager.ui.tools.feature.send

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.base.util.toListItemCardStyle
import .ui.platform.components.card.Universal Password ManagerInfoCalloutCard
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.icon.model.IconData
import .ui.platform.components.model.CardStyle
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.ui.platform.components.listitem.Universal Password ManagerGroupItem
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.handlers.SendHandlers
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.model.UpgradedToPremiumCardData
import com.x8bit.universalpasswordmanager.ui.vault.feature.itemlisting.model.ListingItemOverflowAction

private const val SEND_TYPES_COUNT: Int = 2

/**
 * Content view for the [SendScreen].
 */
@Suppress("LongMethod")
@Composable
fun SendContent(
    policyDisablesSend: Boolean,
    shouldShowTypesSection: Boolean,
    state: SendState.ViewState.Content,
    upgradedToPremiumCardData: UpgradedToPremiumCardData?,
    sendHandlers: SendHandlers,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        item {
            Spacer(modifier = Modifier.height(height = 12.dp))
        }
        upgradedToPremiumCardData?.let {
            item {
                UpgradedToPremiumActionCard(
                    onActionClick = it.onCardClick,
                    onDismissClick = it.onCardDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin(),
                )
                Spacer(modifier = Modifier.height(height = 16.dp))
            }
        }
        if (policyDisablesSend) {
            item {
                Universal Password ManagerInfoCalloutCard(
                    text = stringResource(id = Universal Password ManagerString.send_disabled_warning),
                    modifier = Modifier
                        .testTag("SendOptionsPolicyInEffectLabel")
                        .standardHorizontalMargin()
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
            }
        }

        if (shouldShowTypesSection) {
            item {
                Universal Password ManagerListHeaderText(
                    label = stringResource(id = Universal Password ManagerString.types),
                    supportingLabel = SEND_TYPES_COUNT.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .padding(horizontal = 16.dp),
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
            }

            item {
                Universal Password ManagerGroupItem(
                    label = stringResource(id = Universal Password ManagerString.type_text),
                    supportingLabel = state.textTypeCount.toString(),
                    startIcon = IconData.Local(iconRes = Universal Password ManagerDrawable.ic_file_text),
                    onClick = sendHandlers.onTextTypeClick,
                    cardStyle = CardStyle.Top(dividerPadding = 56.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("SendTextFilter")
                        .standardHorizontalMargin(),
                )
            }

            item {
                Universal Password ManagerGroupItem(
                    label = stringResource(id = Universal Password ManagerString.type_file),
                    supportingLabel = state.fileTypeCount.toString(),
                    startIcon = IconData.Local(iconRes = Universal Password ManagerDrawable.ic_file),
                    onClick = sendHandlers.onFileTypeClick,
                    cardStyle = CardStyle.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("SendFileFilter")
                        .standardHorizontalMargin(),
                )
            }

            item {
                Spacer(modifier = Modifier.height(height = 16.dp))
            }
        }

        item {
            Universal Password ManagerListHeaderText(
                label = stringResource(id = Universal Password ManagerString.all_sends),
                supportingLabel = state.sendItems.size.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
        }

        itemsIndexed(state.sendItems) { index, it ->
            SendListItem(
                startIcon = IconData.Local(it.type.iconRes),
                label = it.name,
                supportingLabel = it.deletionDate,
                trailingLabelIcons = it.iconList,
                showMoreOptions = !policyDisablesSend,
                onClick = { sendHandlers.onSendClick(it) },
                onOverflowAction = { action ->
                    when (action) {
                        is ListingItemOverflowAction.SendAction.CopyUrlClick -> {
                            sendHandlers.onCopySendClick(it)
                        }

                        is ListingItemOverflowAction.SendAction.DeleteClick -> {
                            sendHandlers.onDeleteSendClick(it)
                        }

                        is ListingItemOverflowAction.SendAction.EditClick -> {
                            sendHandlers.onEditSendClick(it)
                        }

                        is ListingItemOverflowAction.SendAction.RemovePasswordClick -> {
                            sendHandlers.onRemovePasswordClick(it)
                        }

                        is ListingItemOverflowAction.SendAction.ShareUrlClick -> {
                            sendHandlers.onShareSendClick(it)
                        }

                        is ListingItemOverflowAction.SendAction.ViewClick -> {
                            sendHandlers.onViewSendClick(it)
                        }
                    }
                },
                overflowOptions = it.overflowItems,
                cardStyle = state
                    .sendItems
                    .toListItemCardStyle(index = index, dividerPadding = 56.dp),
                modifier = Modifier
                    .testTag("SendCell")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
        }

        item {
            Spacer(modifier = Modifier.height(88.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}
