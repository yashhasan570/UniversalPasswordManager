package com.x8bit.universalpasswordmanager.ui.vault.feature.addedit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.dialog.Universal Password ManagerSelectionDialog
import .ui.platform.components.dialog.Universal Password ManagerTextEntryDialog
import .ui.platform.components.dialog.row.Universal Password ManagerBasicDialogRow
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.model.CustomFieldType
import kotlinx.collections.immutable.ImmutableList

/**
 * A UI element that is used by the user to add a custom field item.
 *
 * @param options The types that are to be chosen by the user.
 * @param onFinishNamingClick Invoked when the user finishes naming the item.
 */
@Composable
fun VaultAddEditCustomFieldsButton(
    onFinishNamingClick: (CustomFieldType, String) -> Unit,
    options: ImmutableList<CustomFieldType>,
    modifier: Modifier = Modifier,
) {
    var shouldShowChooserDialog by remember { mutableStateOf(false) }
    var shouldShowNameDialog by remember { mutableStateOf(false) }

    var customFieldType: CustomFieldType by remember { mutableStateOf(CustomFieldType.TEXT) }
    var customFieldName: String by remember { mutableStateOf("") }

    if (shouldShowChooserDialog) {
        Universal Password ManagerSelectionDialog(
            title = stringResource(id = Universal Password ManagerString.select_type_field),
            onDismissRequest = { shouldShowChooserDialog = false },
        ) {
            options.forEach { type ->
                Universal Password ManagerBasicDialogRow(
                    text = type.typeText.invoke(),
                    onClick = {
                        shouldShowChooserDialog = false
                        shouldShowNameDialog = true
                        customFieldType = type
                    },
                )
            }
        }
    }

    if (shouldShowNameDialog) {
        Universal Password ManagerTextEntryDialog(
            title = stringResource(id = Universal Password ManagerString.custom_field_name),
            textFieldLabel = stringResource(id = Universal Password ManagerString.name),
            onDismissRequest = { shouldShowNameDialog = false },
            onConfirmClick = {
                shouldShowNameDialog = false
                customFieldName = it
                onFinishNamingClick(customFieldType, customFieldName)
            },
        )
    }
    val focusManager = LocalFocusManager.current
    Universal Password ManagerOutlinedButton(
        label = stringResource(id = Universal Password ManagerString.add_field),
        onClick = {
            // Clear any current focused item such as an unrelated text field.
            focusManager.clearFocus()
            shouldShowChooserDialog = true
        },
        modifier = modifier.testTag("NewCustomFieldButton"),
    )
}
