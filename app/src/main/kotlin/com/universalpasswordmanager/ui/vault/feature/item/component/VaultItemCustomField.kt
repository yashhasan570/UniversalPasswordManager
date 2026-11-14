package com.universalpasswordmanager.ui.vault.feature.item.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.universalpasswordmanager.ui.platform.components.button.UniversalStandardIconButton
import com.universalpasswordmanager.ui.platform.components.field.UniversalPasswordField
import com.universalpasswordmanager.ui.platform.components.field.UniversalTextField
import com.universalpasswordmanager.ui.platform.components.icon.model.IconData
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.toggle.UniversalSwitch
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.ui.vault.feature.item.VaultItemState

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
            UniversalSwitch(
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
                UniversalPasswordField(
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
                        UniversalStandardIconButton(
                            vectorIconRes = UniversalDrawable.ic_copy,
                            contentDescription = stringResource(id = UniversalString.copy),
                            onClick = { onCopyCustomHiddenField(customField.value) },
                            modifier = Modifier.testTag("CustomFieldCopyValueButton"),
                        )
                    },
                    cardStyle = cardStyle,
                    modifier = modifier.testTag("ViewCustomHiddenField"),
                )
            } else {
                UniversalPasswordField(
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
            UniversalTextField(
                label = customField.name,
                value = customField.vaultLinkedFieldType.label.invoke(),
                leadingIconData = IconData.Local(
                    iconRes = UniversalDrawable.ic_linked,
                    contentDescription = UniversalString.field_type_linked.asText(),
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
            UniversalTextField(
                label = customField.name,
                value = customField.value,
                onValueChange = { },
                readOnly = true,
                singleLine = false,
                textFieldTestTag = "CustomFieldValue",
                actions = {
                    if (customField.isCopyable) {
                        UniversalStandardIconButton(
                            vectorIconRes = UniversalDrawable.ic_copy,
                            contentDescription = stringResource(id = UniversalString.copy),
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
