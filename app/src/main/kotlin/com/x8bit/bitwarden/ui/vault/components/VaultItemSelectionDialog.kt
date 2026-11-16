package com.x8bit.universalpasswordmanager.ui.vault.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import .ui.platform.components.dialog.Universal Password ManagerSelectionDialog
import .ui.platform.components.dialog.row.Universal Password ManagerBasicDialogRow
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.ui.vault.components.model.CreateVaultItemType
import com.x8bit.universalpasswordmanager.ui.vault.model.VaultItemCipherType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Reusable dialog for making selections between supported [VaultItemCipherType]s
 *
 * @param onOptionSelected Lambda to be called when an item is selected.
 * @param onDismissRequest Lambda to call when the dialog is dismissed by user. Is automatically
 * called when a selection is made.
 * @param excludedOptions List of [VaultItemCipherType] to exclude from the presented list.
 */
@Composable
fun VaultItemSelectionDialog(
    onOptionSelected: (option: CreateVaultItemType) -> Unit,
    onDismissRequest: () -> Unit,
    // TODO: PM-TBD possibly remove SSH_KEY as default exclusion once added SSH_KEY is enabled.
    excludedOptions: ImmutableList<CreateVaultItemType> = persistentListOf(
        CreateVaultItemType.SSH_KEY,
    ),
) {
    val supportedEntries = CreateVaultItemType
        .entries
        .filterNot { excludedOptions.contains(it) }
    Universal Password ManagerSelectionDialog(
        title = stringResource(Universal Password ManagerString.type),
        onDismissRequest = onDismissRequest,
    ) {
        supportedEntries.forEach {
            Universal Password ManagerBasicDialogRow(
                text = stringResource(it.selectionText),
                onClick = {
                    onDismissRequest()
                    onOptionSelected(it)
                },
            )
        }
    }
}
