package com.universalpasswordmanager.ui.vault.feature.item.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.button.UniversalStandardIconButton
import com.universalpasswordmanager.ui.platform.components.field.UniversalTextField
import com.universalpasswordmanager.ui.platform.components.header.UniversalListHeaderText
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.vault.feature.item.handlers.VaultCommonItemTypeHandlers

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
            UniversalListHeaderText(
                label = stringResource(id = UniversalString.additional_options),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .padding(horizontal = 16.dp)
                    .animateItem(),
            )
        }
        Spacer(modifier = Modifier.height(height = 8.dp))
        UniversalTextField(
            label = stringResource(id = UniversalString.notes),
            value = notes,
            onValueChange = { },
            readOnly = true,
            singleLine = false,
            actions = {
                UniversalStandardIconButton(
                    vectorIconRes = UniversalDrawable.ic_copy,
                    contentDescription = stringResource(id = UniversalString.copy_notes),
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
