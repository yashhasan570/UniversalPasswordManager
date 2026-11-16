package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.autofill.blockautofill

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.dialog.util.maxDialogHeight
import .ui.platform.components.divider.Universal Password ManagerHorizontalDivider
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.model.CardStyle
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * A dialog for adding a blocked URI.
 */
@Suppress("LongMethod")
@Composable
fun AddEditBlockedUriDialog(
    uri: String,
    isEdit: Boolean,
    errorMessage: String?,
    onUriChange: (String) -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        val configuration = LocalConfiguration.current
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .semantics { testTagsAsResourceId = true }
                .requiredHeightIn(
                    max = configuration.maxDialogHeight,
                )
                // This background is necessary for the dialog to not be transparent.
                .background(
                    color = Universal Password ManagerTheme.colorScheme.background.primary,
                    shape = Universal Password ManagerTheme.shapes.dialog,
                ),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                    .fillMaxWidth(),
                text = stringResource(
                    id = if (isEdit) {
                        Universal Password ManagerString.edit_blocked_uri
                    } else {
                        Universal Password ManagerString.new_blocked_uri
                    },
                ),
                color = Universal Password ManagerTheme.colorScheme.text.primary,
                style = Universal Password ManagerTheme.typography.headlineSmall,
            )
            if (scrollState.canScrollBackward) {
                Universal Password ManagerHorizontalDivider()
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(scrollState),
            ) {
                Universal Password ManagerTextField(
                    label = stringResource(id = Universal Password ManagerString.enter_uri),
                    isError = errorMessage != null,
                    supportingText = errorMessage ?: stringResource(
                        id = Universal Password ManagerString.format_x_separate_multiple_ur_is_with_a_comma,
                        "http://domain.com",
                    ),
                    value = uri,
                    onValueChange = onUriChange,
                    keyboardType = KeyboardType.Uri,
                    cardStyle = CardStyle.Full,
                    textFieldTestTag = "BlockedUriEntry",
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .fillMaxWidth(),
                )
            }
            if (scrollState.canScrollForward) {
                Universal Password ManagerHorizontalDivider()
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp, top = 24.dp, bottom = 24.dp, end = 24.dp),
            ) {
                Universal Password ManagerTextButton(
                    label = stringResource(id = Universal Password ManagerString.cancel),
                    onClick = onCancelClick,
                )

                Universal Password ManagerFilledButton(
                    label = stringResource(id = Universal Password ManagerString.save),
                    onClick = { onSaveClick(uri) },
                    modifier = Modifier.testTag(tag = "BlockedUriSaveButton"),
                )
            }
        }
    }
}
