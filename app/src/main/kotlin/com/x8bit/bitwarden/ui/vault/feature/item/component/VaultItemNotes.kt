package com.x8bit.universalpasswordmanager.ui.vault.feature.item.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.button.Universal Password ManagerStandardIconButton
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.model.CardStyle
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.handlers.VaultCommonItemTypeHandlers

/**
 * Displays the common notes field for the vault item screen.
 *
 * @param notes The notes.
 * @param vaultCommonItemTypeHandlers Provides the handlers required for the notes.
 * @param showHeader Indicates whether to show the header.
 */
fun LazyListScope.vaultItemNotes(
    notes: String?,
    vaultCommonItemTypeHandlers: VaultCommonItemTypeHandlers,
    showHeader: Boolean = true,
) {
    notes ?: return
    item(key = "notes") {
        if (showHeader) {
            Spacer(modifier = Modifier.height(height = 16.dp))
            Universal Password ManagerListHeaderText(
                label = stringResource(id = Universal Password ManagerString.additional_options),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .padding(horizontal = 16.dp)
                    .animateItem(),
            )
        }
        Spacer(modifier = Modifier.height(height = 8.dp))
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.notes),
            value = notes,
            onValueChange = { },
            readOnly = true,
            singleLine = false,
            actions = {
                Universal Password ManagerStandardIconButton(
                    vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                    contentDescription = stringResource(id = Universal Password ManagerString.copy_notes),
                    onClick = vaultCommonItemTypeHandlers.onCopyNotesClick,
                    modifier = Modifier.testTag(tag = "CipherNotesCopyButton"),
                )
            },
            textFieldTestTag = "CipherNotesLabel",
            cardStyle = CardStyle.Full,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .animateItem(),
        )
    }
}
