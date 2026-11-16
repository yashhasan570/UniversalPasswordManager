package com.x8bit.universalpasswordmanager.ui.vault.feature.itemlisting

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.base.util.toListItemCardStyle
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.card.Universal Password ManagerActionCard
import .ui.platform.components.card.Universal Password ManagerInfoCalloutCard
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.icon.model.IconData
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.platform.components.dialog.Universal Password ManagerMasterPasswordDialog
import com.x8bit.universalpasswordmanager.ui.platform.components.listitem.Universal Password ManagerGroupItem
import com.x8bit.universalpasswordmanager.ui.platform.components.listitem.Universal Password ManagerListItem
import com.x8bit.universalpasswordmanager.ui.platform.components.listitem.SelectionItemData
import com.x8bit.universalpasswordmanager.ui.vault.feature.itemlisting.handlers.VaultItemListingHandlers
import com.x8bit.universalpasswordmanager.ui.vault.feature.itemlisting.model.ListingItemOverflowAction
import kotlinx.collections.immutable.toImmutableList

/**
 * Content view for the [VaultItemListingScreen].
 */
@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
fun VaultItemListingContent(
    state: VaultItemListingState.ViewState.Content,
    actionCard: VaultItemListingState.ActionCardState?,
    policyDisablesSend: Boolean,
    showAddTotpBanner: Boolean,
    vaultItemListingHandlers: VaultItemListingHandlers,
    modifier: Modifier = Modifier,
) {
    var overflowSpeedBumpAction: ListingItemOverflowAction? by rememberSaveable {
        mutableStateOf(null)
    }

    overflowSpeedBumpAction?.let { action ->
        action
            .speedBump
            ?.let { speedBump ->
                Universal Password ManagerTwoButtonDialog(
                    twoButtonDialogData = speedBump,
                    onConfirmClick = {
                        overflowSpeedBumpAction = null
                        vaultItemListingHandlers.overflowItemClick(action)
                    },
                    onDismissClick = { overflowSpeedBumpAction = null },
                    onDismissRequest = { overflowSpeedBumpAction = null },
                )
            }
            ?: run {
                // If we somehow get here and there is no speed bump, then we should keep on going.
                overflowSpeedBumpAction = null
                vaultItemListingHandlers.overflowItemClick(action)
            }
    }

    var masterPasswordRepromptData by remember { mutableStateOf<MasterPasswordRepromptData?>(null) }
    masterPasswordRepromptData?.let { data ->
        Universal Password ManagerMasterPasswordDialog(
            onConfirmClick = { password ->
                masterPasswordRepromptData = null
                vaultItemListingHandlers.masterPasswordRepromptSubmit(password, data)
            },
            onDismissRequest = {
                masterPasswordRepromptData = null
            },
        )
    }

    LazyColumn(
        modifier = modifier,
    ) {
        actionCard?.let {
            item(key = "action_card") {
                Spacer(modifier = Modifier.height(height = 12.dp))
                ActionCard(
                    actionCardState = it,
                    vaultItemListingHandlers = vaultItemListingHandlers,
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .animateItem(),
                )
                Spacer(modifier = Modifier.height(height = 12.dp))
            }
        }

        if (showAddTotpBanner) {
            item(key = "auth_key_callout") {
                Spacer(modifier = Modifier.height(height = 12.dp))
                Universal Password ManagerInfoCalloutCard(
                    text = stringResource(
                        id = Universal Password ManagerString.add_this_authenticator_key_to_a_login,
                    ),
                    modifier = Modifier
                        .animateItem()
                        .standardHorizontalMargin()
                        .fillMaxWidth(),
                )
            }
        }

        if (policyDisablesSend) {
            item(key = "sends_disabled_callout") {
                Spacer(modifier = Modifier.height(height = 12.dp))
                Universal Password ManagerInfoCalloutCard(
                    text = stringResource(id = Universal Password ManagerString.send_disabled_warning),
                    modifier = Modifier
                        .animateItem()
                        .standardHorizontalMargin()
                        .fillMaxWidth(),
                )
            }
        }

        if (state.displayCollectionList.isNotEmpty()) {
            item(key = "collections_header") {
                Spacer(modifier = Modifier.height(height = 12.dp))
                Universal Password ManagerListHeaderText(
                    label = stringResource(id = Universal Password ManagerString.shared_folders),
                    supportingLabel = state.displayCollectionList.count().toString(),
                    modifier = Modifier
                        .animateItem()
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .padding(horizontal = 16.dp),
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
            }

            itemsIndexed(
                items = state.displayCollectionList,
                key = { _, collection -> "collection_${collection.id}" },
            ) { index, collection ->
                Universal Password ManagerGroupItem(
                    startIcon = IconData.Local(iconRes = Universal Password ManagerDrawable.ic_shared_folder),
                    label = collection.name,
                    supportingLabel = collection.count.toString(),
                    onClick = { vaultItemListingHandlers.collectionClick(collection.id) },
                    cardStyle = state
                        .displayCollectionList
                        .toListItemCardStyle(index = index, dividerPadding = 56.dp),
                    modifier = Modifier
                        .animateItem()
                        .fillMaxWidth()
                        .standardHorizontalMargin(),
                )
            }
        }

        if (state.displayFolderList.isNotEmpty()) {
            item(key = "folders_header") {
                Spacer(modifier = Modifier.height(height = 12.dp))
                Universal Password ManagerListHeaderText(
                    label = stringResource(id = Universal Password ManagerString.my_folders),
                    supportingLabel = state.displayFolderList.count().toString(),
                    modifier = Modifier
                        .animateItem()
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .padding(horizontal = 16.dp),
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
            }

            itemsIndexed(
                items = state.displayFolderList,
                key = { _, folder -> "folder_${folder.id}" },
            ) { index, folder ->
                Universal Password ManagerGroupItem(
                    startIcon = IconData.Local(iconRes = Universal Password ManagerDrawable.ic_folder),
                    label = folder.name,
                    supportingLabel = folder.count.toString(),
                    onClick = { vaultItemListingHandlers.folderClick(folder.id) },
                    cardStyle = state
                        .displayFolderList
                        .toListItemCardStyle(index = index, dividerPadding = 56.dp),
                    modifier = Modifier
                        .animateItem()
                        .fillMaxWidth()
                        .standardHorizontalMargin(),
                )
            }
        }

        if (state.displayItemList.isNotEmpty()) {
            item(key = "items_header") {
                Spacer(modifier = Modifier.height(height = 12.dp))
                Universal Password ManagerListHeaderText(
                    label = stringResource(id = Universal Password ManagerString.items),
                    supportingLabel = state.displayItemList.size.toString(),
                    modifier = Modifier
                        .animateItem()
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .padding(horizontal = 16.dp),
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
            }
            itemsIndexed(
                items = state.displayItemList,
                key = { _, item -> "item_${item.id}" },
            ) { index, it ->
                Universal Password ManagerListItem(
                    startIcon = it.iconData,
                    startIconTestTag = it.iconTestTag,
                    label = it.title.invoke(),
                    labelTestTag = it.titleTestTag,
                    secondSupportingLabel = it.secondSubtitle,
                    secondSupportingLabelTestTag = it.secondSubtitleTestTag,
                    supportingLabel = it.subtitle,
                    supportingLabelTestTag = it.subtitleTestTag,
                    optionsTestTag = it.optionsTestTag,
                    onClick = {
                        if (it.isAutofill && it.shouldShowMasterPasswordReprompt) {
                            masterPasswordRepromptData = MasterPasswordRepromptData.Autofill(
                                cipherId = it.id,
                            )
                        } else if (it.shouldShowMasterPasswordReprompt) {
                            masterPasswordRepromptData = MasterPasswordRepromptData.ViewItem(
                                id = it.id,
                                itemType = it.itemType,
                            )
                        } else {
                            vaultItemListingHandlers.itemClick(it.id, it.itemType)
                        }
                    },
                    trailingLabelIcons = it.extraIconList,
                    selectionDataList = it
                        .overflowOptions
                        .map { option ->
                            SelectionItemData(
                                text = option.title(),
                                contentDescription = option.contentDescription(),
                                onClick = {
                                    when (option) {
                                        is ListingItemOverflowAction.SendAction -> {
                                            if (option.speedBump != null) {
                                                overflowSpeedBumpAction = option
                                            } else {
                                                vaultItemListingHandlers.overflowItemClick(option)
                                            }
                                        }

                                        is ListingItemOverflowAction.VaultAction -> {
                                            if (option.requiresPasswordReprompt &&
                                                it.shouldShowMasterPasswordReprompt
                                            ) {
                                                masterPasswordRepromptData =
                                                    MasterPasswordRepromptData.OverflowItem(
                                                        action = option,
                                                    )
                                            } else if (option.speedBump != null) {
                                                overflowSpeedBumpAction = option
                                            } else {
                                                vaultItemListingHandlers.overflowItemClick(option)
                                            }
                                        }
                                    }
                                },
                            )
                        }
                        // Only show options if allowed
                        .filter { !policyDisablesSend }
                        .toImmutableList(),
                    cardStyle = state
                        .displayItemList
                        .toListItemCardStyle(index = index, dividerPadding = 56.dp),
                    modifier = Modifier
                        .animateItem()
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .testTag("VaultItemCell"),
                )
            }
        }

        item(key = "bottom_padding") {
            Spacer(modifier = Modifier.height(88.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun ActionCard(
    actionCardState: VaultItemListingState.ActionCardState,
    vaultItemListingHandlers: VaultItemListingHandlers,
    modifier: Modifier = Modifier,
) {
    when (actionCardState) {
        VaultItemListingState.ActionCardState.PremiumSubscription -> {
            Universal Password ManagerActionCard(
                cardTitle = stringResource(id = Universal Password ManagerString.your_premium_subscription_ended),
                cardSubtitle = stringResource(
                    id = Universal Password ManagerString
                        .to_regain_access_to_your_archive_restart_your_premium_subscription,
                ),
                actionButton = Universal Password ManagerButtonData(
                    label = Universal Password ManagerString.restart_premium.asText(),
                    onClick = vaultItemListingHandlers.upgradeToPremiumClick,
                ),
                modifier = modifier,
            )
        }
    }
}
