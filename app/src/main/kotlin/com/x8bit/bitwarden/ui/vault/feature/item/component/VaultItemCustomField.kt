package com.x8bit.universalpasswordmanager.ui.vault.feature.item.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import .ui.platform.components.button.Universal Password ManagerStandardIconButton
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.icon.model.IconData
import .ui.platform.components.model.CardStyle
import .ui.platform.components.toggle.Universal Password ManagerSwitch
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.VaultItemState

/**
 * Custom Field UI common for all item types.
 */
@Suppress("LongMethod")
@Composable
fun CustomField(
    customField: VaultItemState.ViewState.Content.Common.Custom,
    onCopyCustomHiddenField: (String) -> Unit,
    onCopyCustomTextField: (String) -> Unit,
    onShowHiddenFieldClick: (
        VaultItemState.ViewState.Content.Common.Custom.HiddenField,
        Boolean,
    ) -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
) {
    when (customField) {
        is VaultItemState.ViewState.Content.Common.Custom.BooleanField -> {
            Universal Password ManagerSwitch(
                label = customField.name,
                isChecked = customField.value,
                readOnly = true,
                onCheckedChange = { },
                cardStyle = cardStyle,
                modifier = modifier.testTag("ViewCustomBooleanField"),
            )
        }

        is VaultItemState.ViewState.Content.Common.Custom.HiddenField -> {
            if (customField.isCopyable) {
                Universal Password ManagerPasswordField(
                    label = customField.name,
                    value = customField.value,
                    showPasswordChange = { onShowHiddenFieldClick(customField, it) },
                    showPassword = customField.isVisible,
                    onValueChange = { },
                    readOnly = true,
                    singleLine = false,
                    showPasswordTestTag = "CustomFieldShowPasswordButton",
                    passwordFieldTestTag = "CustomFieldValue",
                    actions = {
                        Universal Password ManagerStandardIconButton(
                            vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                            contentDescription = stringResource(id = Universal Password ManagerString.copy),
                            onClick = { onCopyCustomHiddenField(customField.value) },
                            modifier = Modifier.testTag("CustomFieldCopyValueButton"),
                        )
                    },
                    cardStyle = cardStyle,
                    modifier = modifier.testTag("ViewCustomHiddenField"),
                )
            } else {
                Universal Password ManagerPasswordField(
                    label = customField.name,
                    value = customField.value,
                    showPasswordChange = { onShowHiddenFieldClick(customField, it) },
                    showPassword = customField.isVisible,
                    onValueChange = { },
                    readOnly = true,
                    singleLine = false,
                    cardStyle = cardStyle,
                    modifier = modifier,
                )
            }
        }

        is VaultItemState.ViewState.Content.Common.Custom.LinkedField -> {
            Universal Password ManagerTextField(
                label = customField.name,
                value = customField.vaultLinkedFieldType.label.invoke(),
                leadingIconData = IconData.Local(
                    iconRes = Universal Password ManagerDrawable.ic_linked,
                    contentDescription = Universal Password ManagerString.field_type_linked.asText(),
                ),
                onValueChange = { },
                readOnly = true,
                singleLine = false,
                cardStyle = cardStyle,
                textFieldTestTag = "CustomFieldDropdown",
                modifier = modifier.testTag("ViewCustomLinkedField"),
            )
        }

        is VaultItemState.ViewState.Content.Common.Custom.TextField -> {
            Universal Password ManagerTextField(
                label = customField.name,
                value = customField.value,
                onValueChange = { },
                readOnly = true,
                singleLine = false,
                textFieldTestTag = "CustomFieldValue",
                actions = {
                    if (customField.isCopyable) {
                        Universal Password ManagerStandardIconButton(
                            vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                            contentDescription = stringResource(id = Universal Password ManagerString.copy),
                            onClick = { onCopyCustomTextField(customField.value) },
                            modifier = Modifier.testTag("CustomFieldCopyValueButton"),
                        )
                    }
                },
                cardStyle = cardStyle,
                modifier = modifier.testTag("ViewCustomTextField"),
            )
        }
    }
}
