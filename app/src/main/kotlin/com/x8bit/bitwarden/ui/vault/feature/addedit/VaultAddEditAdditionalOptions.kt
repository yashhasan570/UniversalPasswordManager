package com.x8bit.universalpasswordmanager.ui.vault.feature.addedit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import .core.util.persistentListOfNotNull
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.button.model.Universal Password ManagerHelpButtonData
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.header.Universal Password ManagerExpandingHeader
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.model.CardStyle
import .ui.platform.components.toggle.Universal Password ManagerSwitch
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditCommonHandlers
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.model.CustomFieldType

/**
 * The collapsable UI for additional options when adding or editing a cipher.
 */
@Suppress("LongMethod")
fun LazyListScope.vaultAddEditAdditionalOptions(
    itemType: VaultAddEditState.ViewState.Content.ItemType,
    commonState: VaultAddEditState.ViewState.Content.Common,
    commonTypeHandlers: VaultAddEditCommonHandlers,
    isAdditionalOptionsExpanded: Boolean,
    onAdditionalOptionsClick: () -> Unit,
) {
    item {
        Universal Password ManagerExpandingHeader(
            isExpanded = isAdditionalOptionsExpanded,
            onClick = onAdditionalOptionsClick,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )
    }

    if (isAdditionalOptionsExpanded) {
        val isNotes = itemType is VaultAddEditState.ViewState.Content.ItemType.SecureNotes
        if (!isNotes) {
            item(key = "optionalNotes") {
                Universal Password ManagerTextField(
                    singleLine = false,
                    label = stringResource(id = Universal Password ManagerString.notes),
                    value = commonState.notes,
                    onValueChange = commonTypeHandlers.onNotesTextChange,
                    textFieldTestTag = "ItemNotesEntry",
                    cardStyle = CardStyle.Full,
                    modifier = Modifier
                        .animateItem()
                        .fillMaxWidth()
                        .standardHorizontalMargin(),
                )
            }
        }

        if (commonState.isUnlockWithPasswordEnabled) {
            item(key = "MasterPasswordRepromptToggle") {
                Column(
                    modifier = Modifier
                        .animateItem()
                        .fillMaxWidth()
                        .standardHorizontalMargin(),
                ) {
                    if (!isNotes) {
                        Spacer(modifier = Modifier.height(height = 8.dp))
                    }
                    Universal Password ManagerSwitch(
                        label = stringResource(id = Universal Password ManagerString.password_prompt),
                        isChecked = commonState.masterPasswordReprompt,
                        onCheckedChange = commonTypeHandlers.onToggleMasterPasswordReprompt,
                        helpData = Universal Password ManagerHelpButtonData(
                            onClick = commonTypeHandlers.onTooltipClick,
                            contentDescription = stringResource(
                                id = Universal Password ManagerString.master_password_re_prompt_help,
                            ),
                            isExternalLink = true,
                        ),
                        cardStyle = CardStyle.Full,
                        modifier = Modifier
                            .testTag(tag = "MasterPasswordRepromptToggle")
                            .fillMaxWidth(),
                    )
                }
            }
        }

        item(key = "customFieldsHeader") {
            Column(
                modifier = Modifier
                    .animateItem()
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            ) {
                Spacer(modifier = Modifier.height(height = 16.dp))
                Universal Password ManagerListHeaderText(
                    label = stringResource(id = Universal Password ManagerString.custom_fields),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }
        }

        itemsIndexed(
            items = commonState.customFieldData,
            key = { _, customItem -> "customField_${customItem.itemId}" },
        ) { index, customItem ->
            Column(
                modifier = Modifier
                    .animateItem()
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            ) {
                Spacer(modifier = Modifier.height(height = 8.dp))
                VaultAddEditCustomField(
                    customField = customItem,
                    onCustomFieldValueChange = commonTypeHandlers.onCustomFieldValueChange,
                    onCustomFieldAction = commonTypeHandlers.onCustomFieldActionSelect,
                    showMoveUpAction = index > 0,
                    showMoveDownAction = index < commonState.customFieldData.lastIndex,
                    onHiddenVisibilityChanged = commonTypeHandlers.onHiddenFieldVisibilityChange,
                    supportedLinkedTypes = itemType.vaultLinkedFieldTypes,
                    cardStyle = CardStyle.Full,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(modifier = Modifier.height(height = 8.dp))
        }

        item(key = "addCustomFieldButton") {
            Column(
                modifier = Modifier
                    .animateItem()
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            ) {
                Spacer(modifier = Modifier.height(height = 8.dp))
                VaultAddEditCustomFieldsButton(
                    onFinishNamingClick = commonTypeHandlers.onAddNewCustomFieldClick,
                    options = persistentListOfNotNull(
                        CustomFieldType.TEXT,
                        CustomFieldType.HIDDEN,
                        CustomFieldType.BOOLEAN,
                        CustomFieldType.LINKED.takeIf {
                            itemType.vaultLinkedFieldTypes.isNotEmpty()
                        },
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
