package com.universalpasswordmanager.ui.platform.feature.settings.autofill.blockautofill

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
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalTextButton
import com.universalpasswordmanager.ui.platform.components.dialog.util.maxDialogHeight
import com.universalpasswordmanager.ui.platform.components.divider.UniversalHorizontalDivider
import com.universalpasswordmanager.ui.platform.components.field.UniversalTextField
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

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
                    color = UniversalTheme.colorScheme.background.primary,
                    shape = UniversalTheme.shapes.dialog,
                ),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                    .fillMaxWidth(),
                text = stringResource(
                    id = if (isEdit) {
                        UniversalString.edit_blocked_uri
                    } else {
                        UniversalString.new_blocked_uri
                    },
                ),
                color = UniversalTheme.colorScheme.text.primary,
                style = UniversalTheme.typography.headlineSmall,
            )
            if (scrollState.canScrollBackward) {
                UniversalHorizontalDivider()
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(scrollState),
            ) {
                UniversalTextField(
                    label = stringResource(id = UniversalString.enter_uri),
                    isError = errorMessage != null,
                    supportingText = errorMessage ?: stringResource(
                        id = UniversalString.format_x_separate_multiple_ur_is_with_a_comma,
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
                UniversalHorizontalDivider()
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp, top = 24.dp, bottom = 24.dp, end = 24.dp),
            ) {
                UniversalTextButton(
                    label = stringResource(id = UniversalString.cancel),
                    onClick = onCancelClick,
                )

                UniversalFilledButton(
                    label = stringResource(id = UniversalString.save),
                    onClick = { onSaveClick(uri) },
                    modifier = Modifier.testTag(tag = "BlockedUriSaveButton"),
                )
            }
        }
    }
}
